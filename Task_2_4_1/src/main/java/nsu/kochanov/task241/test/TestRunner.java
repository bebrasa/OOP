package nsu.kochanov.task241.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

/**
 * Класс для запуска тестов JUnit.
 */
public class TestRunner {

    /**
     * Класс для хранения результатов выполнения тестов.
     */
    public static class TestResults {
        /** Общее количество тестов. */
        public int totalTests;
        
        /** Количество успешно пройденных тестов. */
        public int passedTests;
        
        /** Количество проваленных тестов. */
        public int failedTests;
        
        /** Количество пропущенных тестов. */
        public int skippedTests;
        
        /** Список сообщений об ошибках. */
        public List<String> failures = new ArrayList<>();

        /**
         * Создает новый экземпляр результатов тестов.
         */
        public TestResults() {}
    }

    /**
     * Запускает тесты в указанной директории.
     *
     * @param sourceDir путь к директории с тестами
     * @return результаты выполнения тестов
     */
    public TestResults runTests(String sourceDir) {
        TestResults results = new TestResults();
        try {
            // Ищем все build.gradle файлы в подпапках
            List<Path> gradleFiles = new ArrayList<>();
            Files.walk(Path.of(sourceDir))
                    .filter(path -> path.getFileName().toString().equals("build.gradle"))
                    .forEach(gradleFiles::add);

            if (gradleFiles.isEmpty()) {
                results.failures.add("Не найдено build.gradle файлов в директории: " + sourceDir);
                return results;
            }

            for (Path gradleFile : gradleFiles) {
                File projectDir = gradleFile.getParent().toFile();
                System.out.println("Запуск тестов в директории: " + projectDir.getAbsolutePath());

                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(projectDir);
                processBuilder.command("./gradlew", "test");
                
                // Перенаправляем вывод в файлы
                File outputFile = new File(projectDir, "test-output.txt");
                File errorFile = new File(projectDir, "test-error.txt");
                processBuilder.redirectOutput(outputFile);
                processBuilder.redirectError(errorFile);

                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                // Читаем результаты тестов
                if (outputFile.exists()) {
                    String output = new String(Files.readAllBytes(outputFile.toPath()));
                    parseTestResults(output, results);
                }

                // Читаем ошибки
                if (errorFile.exists()) {
                    String errors = new String(Files.readAllBytes(errorFile.toPath()));
                    if (!errors.isEmpty()) {
                        results.failures.add("Ошибки при запуске тестов в " 
                                + projectDir.getAbsolutePath() + ":\n" + errors);
                    }
                }

                // Очищаем временные файлы
                outputFile.delete();
                errorFile.delete();
            }

        } catch (IOException e) {
            results.failures.add("Ошибка при запуске тестов: " + e.getMessage());
        } catch (Exception e) {
            results.failures.add("Непредвиденная ошибка при запуске тестов: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Разбирает результаты тестов из вывода Gradle.
     *
     * @param output строка с выводом Gradle
     * @param results объект для сохранения результатов
     */
    private void parseTestResults(String output, TestResults results) {
        // Парсим результаты тестов из вывода Gradle
        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("Tests run:")) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    if (part.contains("Tests run:")) {
                        results.totalTests = Integer.parseInt(part.split(":")[1].trim());
                    } else if (part.contains("Failures:")) {
                        results.failedTests = Integer.parseInt(part.split(":")[1].trim());
                        results.passedTests = results.totalTests - results.failedTests;
                    } else if (part.contains("Skipped:")) {
                        results.skippedTests = Integer.parseInt(part.split(":")[1].trim());
                    }
                }
            }
        }
    }
}