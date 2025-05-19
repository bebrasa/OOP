package nsu.kochanov.task_2_4_1.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса TestRunner.
 */
class TestRunnerTest {

    @TempDir
    private File tempDir;
    
    private TestRunner testRunner;
    private File gradleFile;
    private File testFile;
    private File testOutputFile;
    
    /**
     * Подготовка к тестам.
     *
     * @throws IOException если возникает ошибка при создании файлов
     */
    @BeforeEach
    void setUp() throws IOException {
        testRunner = new TestRunner();
        
        // Создаем файл build.gradle
        gradleFile = new File(tempDir, "build.gradle");
        Files.writeString(gradleFile.toPath(), 
                "plugins {\n    id 'java'\n}\n\n"
                + "repositories {\n    mavenCentral()\n}\n\n"
                + "dependencies {\n"
                + "    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'\n"
                + "    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'\n"
                + "}\n\n"
                + "test {\n    useJUnitPlatform()\n}");
        
        // Создаем структуру директорий
        File srcDir = new File(tempDir, "src");
        File testDir = new File(srcDir, "test");
        File javaDir = new File(testDir, "java");
        javaDir.mkdirs();
        
        // Создаем тестовый файл
        testFile = new File(javaDir, "SampleTest.java");
        Files.writeString(testFile.toPath(), 
                "import org.junit.jupiter.api.Test;\n"
                + "import static org.junit.jupiter.api.Assertions.assertTrue;\n\n"
                + "public class SampleTest {\n"
                + "    @Test\n"
                + "    void testSample() {\n"
                + "        assertTrue(true);\n"
                + "    }\n"
                + "}");
        
        // Создаем файл с результатами тестов для имитации вывода gradle
        testOutputFile = new File(tempDir, "test-output.txt");
        Files.writeString(testOutputFile.toPath(), 
                "Tests run: 5, Failures: 1, Skipped: 2");
    }
    
    /**
     * Тест для проверки создания экземпляра TestRunner.
     */
    @Test
    void testTestRunnerCreation() {
        assertNotNull(testRunner);
    }
    
    /**
     * Тест для проверки создания экземпляра TestResults.
     */
    @Test
    void testTestResultsCreation() {
        TestRunner.TestResults results = new TestRunner.TestResults();
        assertNotNull(results);
        assertEquals(0, results.totalTests);
        assertEquals(0, results.passedTests);
        assertEquals(0, results.failedTests);
        assertEquals(0, results.skippedTests);
        assertNotNull(results.failures);
        assertTrue(results.failures.isEmpty());
    }
    
    /**
     * Тест для проверки парсинга результатов тестов через рефлексию.
     *
     * @throws Exception в случае ошибки при вызове метода через рефлексию
     */
    @Test
    void testParseTestResults() throws Exception {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Получаем доступ к приватному методу
        java.lang.reflect.Method parseTestResultsMethod = 
                TestRunner.class.getDeclaredMethod(
                        "parseTestResults", 
                        String.class, 
                        TestRunner.TestResults.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод с тестовым выводом
        String testOutput = "Tests run: 10, Failures: 2, Skipped: 1";
        parseTestResultsMethod.invoke(testRunner, testOutput, results);
        
        // Проверяем результаты
        assertEquals(10, results.totalTests);
        assertEquals(8, results.passedTests); // 10 - 2 = 8
        assertEquals(2, results.failedTests);
        assertEquals(1, results.skippedTests);
    }
    
    /**
     * Тест для проверки поведения при отсутствии build.gradle.
     */
    @Test
    void testRunTestsWithNoBuildGradle() {
        // Создаем директорию без build.gradle
        File emptyDir = new File(tempDir, "empty");
        emptyDir.mkdirs();
        
        // Запускаем тесты
        TestRunner.TestResults results = testRunner.runTests(emptyDir.getAbsolutePath());
        
        // Проверяем наличие ошибки
        assertFalse(results.failures.isEmpty());
        assertTrue(results.failures.get(0).contains("Не найдено build.gradle"));
    }
    
    /**
     * Тест для проверки поведения в случае несуществующей директории.
     */
    @Test
    void testRunTestsWithNonExistentDirectory() {
        // Запускаем тесты на несуществующей директории
        TestRunner.TestResults results = 
                testRunner.runTests("/non/existent/directory");
        
        // Проверяем результаты
        assertFalse(results.failures.isEmpty());
        assertTrue(results.failures.get(0).contains("Ошибка при запуске тестов"));
    }
    
    /**
     * Тест для проверки корректности чтения и установки значений в TestResults.
     */
    @Test
    void testTestResultsFields() {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Устанавливаем значения
        results.totalTests = 15;
        results.passedTests = 10;
        results.failedTests = 3;
        results.skippedTests = 2;
        results.failures.add("Test failure 1");
        results.failures.add("Test failure 2");
        
        // Проверяем значения
        assertEquals(15, results.totalTests);
        assertEquals(10, results.passedTests);
        assertEquals(3, results.failedTests);
        assertEquals(2, results.skippedTests);
        assertEquals(2, results.failures.size());
        assertEquals("Test failure 1", results.failures.get(0));
        assertEquals("Test failure 2", results.failures.get(1));
    }
}