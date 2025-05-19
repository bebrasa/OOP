package nsu.kochanov.task_2_4_1.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nsu.kochanov.task_2_4_1.compiler.JavaCompiler;
import nsu.kochanov.task_2_4_1.model.Checkpoint;
import nsu.kochanov.task_2_4_1.model.CourseConfig;
import nsu.kochanov.task_2_4_1.model.Group;
import nsu.kochanov.task_2_4_1.model.Student;
import nsu.kochanov.task_2_4_1.model.Task;
import nsu.kochanov.task_2_4_1.report.ReportGenerator;
import nsu.kochanov.task_2_4_1.style.CodeStyleChecker;
import nsu.kochanov.task_2_4_1.test.TestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Тесты для класса TaskRunner.
 */
class TaskRunnerTest {

    @TempDir
    private File tempDir;
    
    private CourseConfig config;
    private TaskRunner runner;
    private JavaCompiler mockCompiler;
    private CodeStyleChecker mockStyleChecker;
    private TestRunner mockTestRunner;
    private ReportGenerator mockReportGenerator;

    /**
     * Создаёт конфигурацию для тестов перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        config = new CourseConfig();
        
        // Добавляем тестовую задачу
        Task task = new Task();
        task.id = "test1";
        task.name = "Test Task";
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().minusDays(1);
        task.hardDeadline = LocalDate.now().plusDays(1);
        config.task(task);
        
        // Добавляем тестовую группу и студента
        Group group = new Group();
        group.name = "TestGroup";
        
        Student student = new Student();
        student.fullName = "Test Student";
        student.githubUsername = "testuser";
        student.repoUrl = "https://github.com/testuser/testrepo";
        group.student(student);
        
        config.group(group);
        
        // Создаем моки для компонентов TaskRunner
        mockCompiler = mock(JavaCompiler.class);
        mockStyleChecker = mock(CodeStyleChecker.class);
        mockTestRunner = mock(TestRunner.class);
        mockReportGenerator = mock(ReportGenerator.class);
        
        // Создаем экземпляр TaskRunner
        runner = new TaskRunner(config);
    }

    /**
     * Проверяет создание TaskRunner.
     */
    @Test
    void testTaskRunnerCreation() {
        assertNotNull(runner);
    }

    /**
     * Проверяет расчет баллов за задачу при успешной компиляции без ошибок стиля.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testCalculateScoreWithSuccessfulCompilation() throws Exception {
        // Создаем тестовую задачу
        Task task = new Task();
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().minusDays(2);
        task.hardDeadline = LocalDate.now().minusDays(1);
        
        // Получаем доступ к приватному методу calculateScore через рефлексию
        java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                java.util.List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                runner, task, testResults, new ArrayList<String>(), true);
        
        // Проверяем, что баллы рассчитываются с учетом просрочки жесткого дедлайна
        // и успешной компиляции без ошибок стиля (10 * 0.7 + 10 * 0.3) * 0.5 = 5.0
        assertEquals(5.0, score, 0.01);
    }
    
    /**
     * Проверяет расчет баллов за задачу при успешной компиляции, но с ошибками стиля.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testCalculateScoreWithStyleErrors() throws Exception {
        // Создаем тестовую задачу без просрочек дедлайнов
        Task task = new Task();
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().plusDays(5);
        task.hardDeadline = LocalDate.now().plusDays(10);
        
        // Получаем доступ к приватному методу calculateScore через рефлексию
        java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                java.util.List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с непустым списком ошибок стиля и успешной компиляцией
        List<String> styleErrors = Arrays.asList("Style error 1", "Style error 2");
        double score = (double) calculateScoreMethod.invoke(
                runner, task, testResults, styleErrors, true);
        
        // Проверяем, что баллы рассчитываются только за тесты (10 * 0.7 = 7.0)
        assertEquals(7.0, score, 0.01);
    }
    
    /**
     * Проверяет расчет баллов за задачу при неуспешной компиляции.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testCalculateScoreWithFailedCompilation() throws Exception {
        // Создаем тестовую задачу
        Task task = new Task();
        task.maxScore = 10;
        
        // Получаем доступ к приватному методу calculateScore через рефлексию
        java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                java.util.List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        
        // Вызываем метод с неуспешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                runner, task, testResults, new ArrayList<String>(), false);
        
        // Проверяем, что баллы равны 0 при неуспешной компиляции
        assertEquals(0.0, score, 0.01);
    }
    
    /**
     * Проверяет расчет баллов с частичным прохождением тестов.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testCalculateScoreWithPartialTestSuccess() throws Exception {
        // Создаем тестовую задачу без просрочек дедлайнов
        Task task = new Task();
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().plusDays(5);
        task.hardDeadline = LocalDate.now().plusDays(10);
        
        // Получаем доступ к приватному методу calculateScore через рефлексию
        java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                java.util.List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с частичным прохождением (5 из 10)
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 5;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                runner, task, testResults, new ArrayList<String>(), true);
        
        // Проверяем, что баллы рассчитываются верно
        // (10 * 0.5 * 0.7 + 10 * 0.3 = 3.5 + 3.0 = 6.5)
        assertEquals(6.5, score, 0.01);
    }
    
    /**
     * Проверяет расчет баллов при просрочке мягкого дедлайна.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testCalculateScoreWithSoftDeadlineViolation() throws Exception {
        // Создаем тестовую задачу с просрочкой мягкого дедлайна
        Task task = new Task();
        task.maxScore = 10;
        task.softDeadline = LocalDate.now().minusDays(2);
        task.hardDeadline = LocalDate.now().plusDays(3);
        
        // Получаем доступ к приватному методу calculateScore через рефлексию
        java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                java.util.List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                runner, task, testResults, new ArrayList<String>(), true);
        
        // Проверяем, что баллы рассчитываются с учетом штрафа за мягкий дедлайн
        // (10 * 0.7 + 10 * 0.3) * 0.8 = 8.0
        assertEquals(8.0, score, 0.01);
    }
    
    /**
     * Проверяет правильность поиска директории проекта.
     * @throws IOException если происходит ошибка при создании временных файлов
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindProjectRoot() throws IOException, Exception {
        // Создаем директорию с build.gradle
        File projectDir = new File(tempDir, "project");
        projectDir.mkdirs();
        
        File buildGradle = new File(projectDir, "build.gradle");
        Files.writeString(buildGradle.toPath(), "// Empty build.gradle file");
        
        // Создаем вложенную директорию для тестов
        File testDir = new File(projectDir, "src/test/java");
        testDir.mkdirs();
        
        // Получаем доступ к приватному методу findProjectRoot через рефлексию
        java.lang.reflect.Method findProjectRootMethod = TaskRunner.class.getDeclaredMethod(
                "findProjectRoot", Path.class);
        findProjectRootMethod.setAccessible(true);
        
        // Вызываем метод с путем к директории тестов
        Path result = (Path) findProjectRootMethod.invoke(runner, testDir.toPath());
        
        // Проверяем, что найдена корректная директория проекта
        assertNotNull(result);
        assertEquals(projectDir.toPath(), result);
    }
    
    /**
     * Проверяет поведение метода findProjectRoot, когда build.gradle не найден.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindProjectRootWithNoBuildGradle() throws Exception {
        // Создаем директорию без build.gradle
        File projectDir = new File(tempDir, "project_no_gradle");
        projectDir.mkdirs();
        
        // Получаем доступ к приватному методу findProjectRoot через рефлексию
        java.lang.reflect.Method findProjectRootMethod = TaskRunner.class.getDeclaredMethod(
                "findProjectRoot", Path.class);
        findProjectRootMethod.setAccessible(true);
        
        // Вызываем метод
        Path result = (Path) findProjectRootMethod.invoke(runner, projectDir.toPath());
        
        // Проверяем, что результат null, так как build.gradle не найден
        assertNull(result);
    }
    
    /**
     * Проверяет работу метода findTaskDirectory.
     *
     * @throws IOException если происходит ошибка при создании временных файлов
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindTaskDirectory() throws IOException, Exception {
        // Создаем директорию с Task_1_2_3
        File repoDir = new File(tempDir, "repo");
        File taskDir = new File(repoDir, "Task_1_2_3");
        taskDir.mkdirs();
        
        // Получаем доступ к приватному методу findTaskDirectory через рефлексию
        java.lang.reflect.Method findTaskDirectoryMethod = TaskRunner.class.getDeclaredMethod(
                "findTaskDirectory", String.class, String.class);
        findTaskDirectoryMethod.setAccessible(true);
        
        // Вызываем метод
        String result = (String) findTaskDirectoryMethod.invoke(
                runner, repoDir.getAbsolutePath(), "lab2");
        
        // Проверяем, что найдена правильная директория
        assertNotNull(result);
        assertTrue(result.endsWith("Task_1_2_3"));
    }
    
    /**
     * Проверяет метод parseTestResults.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testParseTestResults() throws Exception {
        // Создаем результаты тестов
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Получаем доступ к приватному методу parseTestResults через рефлексию
        java.lang.reflect.Method parseTestResultsMethod = TaskRunner.class.getDeclaredMethod(
                "parseTestResults", String.class, TestRunner.TestResults.class, String.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод
        parseTestResultsMethod.invoke(runner, "Some test output", results, "Test Task");
        
        // Метод просто выводит информацию, нет возвращаемого значения для проверки
        // Подтверждаем, что метод отработал без исключений
        assertDoesNotThrow(() -> {
            parseTestResultsMethod.invoke(runner, "Some test output", results, "Test Task");
        });
    }
    
    /**
     * Проверяет, что метод run не выбрасывает исключений.
     */
    @Test
    void testRunDoesNotThrowExceptions() {
        assertDoesNotThrow(() -> {
            CourseConfig emptyConfig = new CourseConfig();
            TaskRunner testRunner = new TaskRunner(emptyConfig);
            testRunner.run();
        });
    }
    
    /**
     * Проверяет добавление объектов в CourseConfig.
     */
    @Test
    void testCourseConfigAddObjects() {
        CourseConfig config = new CourseConfig();
        
        // Создаем объекты
        Task task = new Task();
        task.name = "New Task";
        
        Group group = new Group();
        group.name = "New Group";
        
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.name = "New Checkpoint";
        
        // Добавляем объекты
        config.task(task);
        config.group(group);
        config.checkpoint(checkpoint);
        
        // Проверяем, что объекты были добавлены
        assertEquals(1, config.tasks.size());
        assertEquals("New Task", config.tasks.get(0).name);
        
        assertEquals(1, config.groups.size());
        assertEquals("New Group", config.groups.get(0).name);
        
        assertEquals(1, config.checkpoints.size());
        assertEquals("New Checkpoint", config.checkpoints.get(0).name);
    }
}