package nsu.kochanov.task241.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyDouble;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nsu.kochanov.task241.compiler.JavaCompiler;
import nsu.kochanov.task241.model.Checkpoint;
import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.model.Group;
import nsu.kochanov.task241.model.Student;
import nsu.kochanov.task241.model.Task;
import nsu.kochanov.task241.report.ReportGenerator;
import nsu.kochanov.task241.style.CodeStyleChecker;
import nsu.kochanov.task241.test.TestRunner;
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
    private TaskRunner taskRunner;
    private JavaCompiler mockCompiler;
    private CodeStyleChecker mockStyleChecker;
    private TestRunner mockTestRunner;
    private ReportGenerator mockReportGenerator;

    /**
     * Создаёт конфигурацию для тестов перед каждым тестом.
     * 
     * @throws Exception при ошибке настройки тестов
     */
    @BeforeEach
    void setUp() throws Exception {
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
        
        // Создаем экземпляр TaskRunner
        taskRunner = new TaskRunner(config);
        
        // Создаем моки для компонентов TaskRunner
        mockCompiler = mock(JavaCompiler.class);
        mockStyleChecker = mock(CodeStyleChecker.class);
        mockTestRunner = mock(TestRunner.class);
        mockReportGenerator = mock(ReportGenerator.class);
        
        // Подставляем моки в TaskRunner с помощью рефлексии
        injectMocks();
    }
    
    /**
     * Подставляет моки в поля TaskRunner с помощью рефлексии.
     * 
     * @throws Exception при ошибке доступа к полям
     */
    private void injectMocks() throws Exception {
        Field compilerField = TaskRunner.class.getDeclaredField("compiler");
        compilerField.setAccessible(true);
        compilerField.set(taskRunner, mockCompiler);
        
        Field styleCheckerField = TaskRunner.class.getDeclaredField("styleChecker");
        styleCheckerField.setAccessible(true);
        styleCheckerField.set(taskRunner, mockStyleChecker);
        
        Field testRunnerField = TaskRunner.class.getDeclaredField("testRunner");
        testRunnerField.setAccessible(true);
        testRunnerField.set(taskRunner, mockTestRunner);
        
        Field reportGeneratorField = TaskRunner.class.getDeclaredField("reportGenerator");
        reportGeneratorField.setAccessible(true);
        reportGeneratorField.set(taskRunner, mockReportGenerator);
    }

    /**
     * Проверяет создание TaskRunner.
     */
    @Test
    void testTaskRunnerCreation() {
        assertNotNull(taskRunner);
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
        Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                taskRunner, task, testResults, new ArrayList<String>(), true);
        
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
        Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с непустым списком ошибок стиля и успешной компиляцией
        List<String> styleErrors = Arrays.asList("Style error 1", "Style error 2");
        double score = (double) calculateScoreMethod.invoke(
                taskRunner, task, testResults, styleErrors, true);
        
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
        Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        
        // Вызываем метод с неуспешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                taskRunner, task, testResults, new ArrayList<String>(), false);
        
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
        Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с частичным прохождением (5 из 10)
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 5;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                taskRunner, task, testResults, new ArrayList<String>(), true);
        
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
        Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
                "calculateScore", 
                Task.class, 
                TestRunner.TestResults.class,
                List.class,
                boolean.class);
        calculateScoreMethod.setAccessible(true);
        
        // Создаем результаты тестов с полным прохождением
        TestRunner.TestResults testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 10;
        
        // Вызываем метод с пустым списком ошибок стиля и успешной компиляцией
        double score = (double) calculateScoreMethod.invoke(
                taskRunner, task, testResults, new ArrayList<String>(), true);
        
        // Проверяем, что баллы рассчитываются с учетом просрочки мягкого дедлайна
        // (10 * 0.7 + 10 * 0.3) * 0.8 = 8.0
        assertEquals(8.0, score, 0.01);
    }
    
    /**
     * Проверяет метод findProjectRoot.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindProjectRoot() throws Exception {
        // Создаем структуру директорий с build.gradle
        File projectRoot = new File(tempDir, "project");
        projectRoot.mkdirs();
        File gradleFile = new File(projectRoot, "build.gradle");
        gradleFile.createNewFile();
        
        File srcDir = new File(projectRoot, "src/test/java");
        srcDir.mkdirs();
        
        // Получаем доступ к приватному методу findProjectRoot через рефлексию
        Method findProjectRootMethod = TaskRunner.class.getDeclaredMethod(
                "findProjectRoot", 
                Path.class);
        findProjectRootMethod.setAccessible(true);
        
        // Вызываем метод
        Path result = (Path) findProjectRootMethod.invoke(
                taskRunner, srcDir.toPath());
        
        // Проверяем, что метод нашел корневую директорию проекта
        assertNotNull(result);
        assertEquals(projectRoot.toPath(), result);
    }
    
    /**
     * Проверяет метод findProjectRoot при отсутствии build.gradle.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindProjectRootWithNoBuildGradle() throws Exception {
        // Создаем директорию без build.gradle
        File projectDir = new File(tempDir, "project_no_gradle");
        projectDir.mkdirs();
        
        // Получаем доступ к приватному методу findProjectRoot через рефлексию
        Method findProjectRootMethod = TaskRunner.class.getDeclaredMethod(
                "findProjectRoot", 
                Path.class);
        findProjectRootMethod.setAccessible(true);
        
        // Вызываем метод
        Path result = (Path) findProjectRootMethod.invoke(
                taskRunner, projectDir.toPath());
        
        // Проверяем, что метод вернул null
        assertNull(result);
    }
    
    /**
     * Проверяет метод findTaskDirectory.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testFindTaskDirectory() throws Exception {
        // Создаем структуру директорий с задачей
        File repoDir = new File(tempDir, "repo");
        repoDir.mkdirs();
        
        File taskDir = new File(repoDir, "Task_1_2_3");
        taskDir.mkdirs();
        
        // Получаем доступ к приватному методу findTaskDirectory через рефлексию
        Method findTaskDirectoryMethod = TaskRunner.class.getDeclaredMethod(
                "findTaskDirectory", 
                String.class,
                String.class);
        findTaskDirectoryMethod.setAccessible(true);
        
        // Вызываем метод
        String result = (String) findTaskDirectoryMethod.invoke(
                taskRunner, repoDir.getAbsolutePath(), "lab2");
        
        // Проверяем, что метод нашел директорию задачи
        assertNotNull(result);
        assertTrue(result.contains("Task_1_2_3"));
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
        results.totalTests = 10;
        results.passedTests = 8;
        results.failedTests = 2;
        
        // Получаем доступ к приватному методу parseTestResults через рефлексию
        Method parseTestResultsMethod = TaskRunner.class.getDeclaredMethod(
                "parseTestResults", 
                String.class,
                TestRunner.TestResults.class,
                String.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод
        parseTestResultsMethod.invoke(taskRunner, "Test output", results, "Task 1");
        
        // Нет явного результата для проверки, просто убеждаемся, что метод не вызывает исключений
        assertDoesNotThrow(() -> 
                parseTestResultsMethod.invoke(taskRunner, "Test output", results, "Task 1"));
    }
    
    /**
     * Проверяет, что метод run не выбрасывает исключений.
     */
    @Test
    void testRunDoesNotThrowExceptions() {
        // Настраиваем моки, чтобы они не вызывали реальных операций
        when(mockCompiler.compile(anyString())).thenReturn(true);
        when(mockStyleChecker.checkDirectory(anyString())).thenReturn(new ArrayList<>());
        when(mockTestRunner.runTests(anyString())).thenReturn(new TestRunner.TestResults());
        
        // Проверяем, что метод run не выбрасывает исключений
        assertDoesNotThrow(() -> taskRunner.run());
    }
    
    /**
     * Проверяет методы добавления объектов в CourseConfig.
     */
    @Test
    void testCourseConfigAddObjects() {
        CourseConfig testConfig = new CourseConfig();
        
        // Добавляем объекты
        Task task = new Task();
        task.id = "test_task";
        testConfig.task(task);
        
        Group group = new Group();
        group.name = "test_group";
        testConfig.group(group);
        
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.date = LocalDate.now();
        testConfig.checkpoint(checkpoint);
        
        // Проверяем, что объекты были добавлены
        assertEquals(1, testConfig.tasks.size());
        assertEquals("test_task", testConfig.tasks.get(0).id);
        
        assertEquals(1, testConfig.groups.size());
        assertEquals("test_group", testConfig.groups.get(0).name);
        
        assertEquals(1, testConfig.checkpoints.size());
        assertEquals(LocalDate.now(), testConfig.checkpoints.get(0).date);
    }
    
    /**
     * Проверяет процесс выполнения задания с различными сценариями компиляции.
     */
    @Test
    void testTaskProcessingWithDifferentCompilationScenarios() {
        // Сценарий 1: Успешная компиляция
        when(mockCompiler.compile(anyString())).thenReturn(true);
        when(mockStyleChecker.checkDirectory(anyString())).thenReturn(new ArrayList<>());
        
        TestRunner.TestResults successResults = new TestRunner.TestResults();
        successResults.totalTests = 5;
        successResults.passedTests = 5;
        when(mockTestRunner.runTests(anyString())).thenReturn(successResults);
        
        // Проверяем, что run не выбрасывает исключений
        assertDoesNotThrow(() -> taskRunner.run());
        
        // Сценарий 2: Неуспешная компиляция
        when(mockCompiler.compile(anyString())).thenReturn(false);
        
        // Проверяем, что run не выбрасывает исключений
        assertDoesNotThrow(() -> taskRunner.run());
    }
}