package nsu.kochanov.task_2_4_1.runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import nsu.kochanov.task_2_4_1.compiler.JavaCompiler;
import nsu.kochanov.task_2_4_1.git.GitManager;
import nsu.kochanov.task_2_4_1.model.CourseConfig;
import nsu.kochanov.task_2_4_1.model.Group;
import nsu.kochanov.task_2_4_1.model.Student;
import nsu.kochanov.task_2_4_1.model.Task;
import nsu.kochanov.task_2_4_1.report.ReportGenerator;
import nsu.kochanov.task_2_4_1.style.CodeStyleChecker;
import nsu.kochanov.task_2_4_1.test.TestRunner;

/**
 * Класс для запуска процесса проверки студенческих заданий.
 */
public class TaskRunner {
    private final CourseConfig config;
    private final JavaCompiler compiler;
    private final CodeStyleChecker styleChecker;
    private final TestRunner testRunner;
    private final ReportGenerator reportGenerator;

    /**
     * Создает объект TaskRunner с указанной конфигурацией.
     *
     * @param config конфигурация курса
     */
    public TaskRunner(CourseConfig config) {
        this.config = config;
        this.compiler = new JavaCompiler();
        this.styleChecker = new CodeStyleChecker();
        this.testRunner = new TestRunner();
        this.reportGenerator = new ReportGenerator();
    }

    /**
     * Запускает процесс проверки заданий для всех студентов.
     */
    public void run() {
        for (Group group : config.groups) {
            for (Student student : group.students) {
                System.out.println("Проверка студента: " + student.fullName);
                String localPath = "repos/" + student.githubUsername;
                
                // Проверяем успешность клонирования/обновления репозитория
                if (!GitManager.cloneOrPull(student.repoUrl, localPath)) {
                    System.err.println("Не удалось получить репозиторий для студента: " 
                            + student.fullName);
                    continue;
                }

                // Берем только первые три задачи
                List<Task> tasksToCheck = config.tasks.stream()
                        .limit(3)
                        .collect(Collectors.toList());

                for (Task task : tasksToCheck) {
                    System.out.println("\nПроверка задания: " + task.name);
                    
                    // Находим директорию с текущей задачей
                    String taskDir = findTaskDirectory(localPath, task.id);
                    if (taskDir == null) {
                        System.err.println("Не найдена директория для задания: " + task.name);
                        continue;
                    }

                    // Компиляция
                    boolean compilationSuccess = compiler.compile(taskDir);
                    if (!compilationSuccess) {
                        System.err.println("Ошибка компиляции для студента: " 
                                + student.fullName + ", задание: " + task.name);
                        TestRunner.TestResults emptyResults = new TestRunner.TestResults();
                        emptyResults.failures.add("Ошибка компиляции");
                        reportGenerator.generateReport(
                                config, 
                                student.fullName, 
                                task.id,
                                false, 
                                List.of("Ошибка компиляции"), 
                                emptyResults, 
                                0);
                        continue;
                    }

                    // Проверка стиля
                    List<String> styleErrors = styleChecker.checkDirectory(taskDir);

                    // Запуск тестов в отдельном процессе
                    TestRunner.TestResults testResults = runTestsInSeparateProcess(taskDir, task.name);

                    // Расчет баллов
                    double score = calculateScore(task, testResults, styleErrors, compilationSuccess);

                    // Генерация отчета
                    reportGenerator.generateReport(
                            config, 
                            student.fullName, 
                            task.id,
                            compilationSuccess, 
                            styleErrors, 
                            testResults, 
                            score);
                }
            }
        }
    }

    /**
     * Находит директорию задания в репозитории студента.
     *
     * @param localPath путь к локальному репозиторию
     * @param taskId идентификатор задания
     * @return путь к директории задания или null, если директория не найдена
     */
    private String findTaskDirectory(String localPath, String taskId) {
        try {
            // Сначала пробуем найти по ID задачи
            String result = Files.walk(Path.of(localPath))
                    .filter(path -> path.toString().contains(taskId))
                    .filter(Files::isDirectory)
                    .findFirst()
                    .map(Path::toString)
                    .orElse(null);

            // Если не нашли по ID, пробуем найти по номеру задачи
            if (result == null) {
                String taskNumber = taskId.replace("lab", "");
                result = Files.walk(Path.of(localPath))
                        .filter(path -> {
                            String pathStr = path.toString();
                            // Ищем директории, содержащие номер задачи
                            return pathStr.contains("Task_") 
                                    && pathStr.contains("_" + taskNumber + "_")
                                    && Files.isDirectory(path);
                        })
                        .findFirst()
                        .map(Path::toString)
                        .orElse(null);
            }

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Запускает тесты в отдельном процессе.
     *
     * @param localPath путь к директории с тестами
     * @param taskName название задания
     * @return результаты выполнения тестов
     */
    private TestRunner.TestResults runTestsInSeparateProcess(String localPath, String taskName) {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        try {
            // Ищем все директории с тестами
            Files.walk(Path.of(localPath))
                    .filter(path -> path.toString().contains("/src/test/java/"))
                    .filter(path -> Files.isDirectory(path))
                    .filter(path -> {
                        try {
                            return Files.list(path).anyMatch(p -> p.toString().endsWith("Test.java"));
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(testDir -> {
                        try {
                            // Находим корневую директорию проекта (где находится build.gradle)
                            Path projectRoot = findProjectRoot(testDir);
                            if (projectRoot == null) {
                                results.failures.add("Не найден build.gradle в директории: " 
                                        + testDir);
                                return;
                            }

                            // Считаем количество тестовых классов
                            results.totalTests = (int) Files.walk(testDir)
                                    .filter(path -> path.toString().endsWith("Test.java"))
                                    .count();

                            // Запускаем тесты в корневой директории проекта
                            ProcessBuilder processBuilder = new ProcessBuilder();
                            processBuilder.directory(projectRoot.toFile());
                            
                            // Используем Gradle для запуска тестов с минимальным выводом
                            processBuilder.command(
                                    "./gradlew", 
                                    "test", 
                                    "--tests", 
                                    "*",
                                    "--console=plain",
                                    "--info");
                            
                            // Перенаправляем вывод в файлы
                            File outputFile = new File(projectRoot.toString(), "test-output.txt");
                            File errorFile = new File(projectRoot.toString(), "test-error.txt");
                            processBuilder.redirectOutput(outputFile);
                            processBuilder.redirectError(errorFile);

                            Process process = processBuilder.start();
                            int exitCode = process.waitFor();

                            // Если сборка успешна, считаем все тесты пройденными
                            if (exitCode == 0) {
                                results.passedTests = results.totalTests;
                            }

                            // Очищаем временные файлы
                            outputFile.delete();
                            errorFile.delete();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            results.failures.add("Ошибка при запуске тестов в " + testDir 
                                    + ": " + e.getMessage());
                        }
                    });
            
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            results.failures.add("Ошибка при поиске тестов: " + e.getMessage());
            return results;
        }
    }

    /**
     * Находит корневую директорию проекта.
     *
     * @param testDir директория с тестами
     * @return путь к корневой директории проекта или null, если не найдена
     */
    private Path findProjectRoot(Path testDir) {
        Path current = testDir;
        while (current != null && !current.toString().equals("/")) {
            if (Files.exists(current.resolve("build.gradle"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * Выводит результаты тестов.
     *
     * @param output вывод тестов
     * @param results объект с результатами тестов
     * @param taskName название задания
     */
    private void parseTestResults(String output, TestRunner.TestResults results, String taskName) {
        // Выводим результаты
        System.out.println("\nРезультаты тестов для " + taskName + ":");
        System.out.println("Всего тестов: " + results.totalTests);
        System.out.println("Успешных: " + results.passedTests);
        System.out.println("Провалено: " + results.failedTests);
        System.out.println("Пропущено: " + results.skippedTests);
    }

    /**
     * Рассчитывает балл за задание.
     *
     * @param task задание
     * @param testResults результаты тестов
     * @param styleErrors ошибки стиля
     * @param compilationSuccess успешность компиляции
     * @return балл за задание
     */
    private double calculateScore(
            Task task, 
            TestRunner.TestResults testResults,
            List<String> styleErrors, 
            boolean compilationSuccess) {
        if (!compilationSuccess) {
            return 0;
        }

        double score = 0;

        // Баллы за тесты
        if (testResults.totalTests > 0) {
            double testScore = (double) testResults.passedTests / testResults.totalTests;
            score += testScore * task.maxScore * 0.7; // 70% за тесты
        }

        // Баллы за стиль
        if (styleErrors.isEmpty()) {
            score += task.maxScore * 0.3; // 30% за стиль
        }

        // Учет дедлайнов
        LocalDate now = LocalDate.now();
        if (now.isAfter(task.hardDeadline)) {
            score *= 0.5; // 50% штраф за просрочку жесткого дедлайна
        } else if (now.isAfter(task.softDeadline)) {
            score *= 0.8; // 20% штраф за просрочку мягкого дедлайна
        }

        return score;
    }
}