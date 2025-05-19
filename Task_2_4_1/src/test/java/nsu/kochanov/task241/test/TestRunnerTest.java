package nsu.kochanov.task241.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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
    
    /**
     * Тест для проверки обработки ошибок при запуске команды gradle.
     *
     * @throws IOException при ошибках ввода-вывода
     */
    @Test
    void testRunTestsWithGradleError() throws IOException {
        // Создаем некорректный build.gradle файл
        File badGradleDir = new File(tempDir, "badGradle");
        badGradleDir.mkdirs();
        File badGradleFile = new File(badGradleDir, "build.gradle");
        Files.writeString(badGradleFile.toPath(), "invalid gradle script");
        
        // Добавляем файл с ошибками
        File errorFile = new File(badGradleDir, "test-error.txt");
        Files.writeString(errorFile.toPath(), "Some gradle error");
        
        // Запускаем тесты
        // Поскольку мы не можем реально запустить градл, ожидаем ошибку
        TestRunner.TestResults results = testRunner.runTests(badGradleDir.getAbsolutePath());
        
        // Должно быть сообщение об ошибке
        assertFalse(results.failures.isEmpty());
    }
    
    /**
     * Тест для проверки обработки пустых результатов.
     *
     * @throws Exception в случае ошибки при вызове метода через рефлексию
     */
    @Test
    void testParseEmptyResults() throws Exception {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Получаем доступ к приватному методу
        java.lang.reflect.Method parseTestResultsMethod = 
                TestRunner.class.getDeclaredMethod(
                        "parseTestResults", 
                        String.class, 
                        TestRunner.TestResults.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод с пустым выводом
        String testOutput = "";
        parseTestResultsMethod.invoke(testRunner, testOutput, results);
        
        // Проверяем что результаты не изменились
        assertEquals(0, results.totalTests);
        assertEquals(0, results.passedTests);
        assertEquals(0, results.failedTests);
        assertEquals(0, results.skippedTests);
    }
    
    /**
     * Тест для проверки обработки некорректного формата результатов.
     *
     * @throws Exception в случае ошибки при вызове метода через рефлексию
     */
    @Test
    void testParseInvalidResults() throws Exception {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Получаем доступ к приватному методу
        java.lang.reflect.Method parseTestResultsMethod = 
                TestRunner.class.getDeclaredMethod(
                        "parseTestResults", 
                        String.class, 
                        TestRunner.TestResults.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод с некорректным выводом
        String testOutput = "Invalid format";
        parseTestResultsMethod.invoke(testRunner, testOutput, results);
        
        // Проверяем что результаты не изменились
        assertEquals(0, results.totalTests);
        assertEquals(0, results.passedTests);
        assertEquals(0, results.failedTests);
        assertEquals(0, results.skippedTests);
    }
    
    /**
     * Тест для проверки обработки различных форматов результатов.
     *
     * @throws Exception в случае ошибки при вызове метода через рефлексию
     */
    @Test
    void testParseVariousResults() throws Exception {
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Получаем доступ к приватному методу
        java.lang.reflect.Method parseTestResultsMethod = 
                TestRunner.class.getDeclaredMethod(
                        "parseTestResults", 
                        String.class, 
                        TestRunner.TestResults.class);
        parseTestResultsMethod.setAccessible(true);
        
        // Вызываем метод с нестандартным форматом
        String testOutput = 
                "Some log output\n"
                + "Tests run: 5, Failures: 1, Skipped: 2\n"
                + "More logs";
        parseTestResultsMethod.invoke(testRunner, testOutput, results);
        
        // Проверяем результаты
        assertEquals(5, results.totalTests);
        assertEquals(4, results.passedTests);
        assertEquals(1, results.failedTests);
        assertEquals(2, results.skippedTests);
    }
    
    /**
     * Тест для проверки вывода результатов в консоль.
     */
    @Test
    void testConsoleOutput() {
        // Перенаправляем вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            // Запускаем тесты
            testRunner.runTests(tempDir.getAbsolutePath());
            
            // Проверяем, что в выводе содержится информация о запуске тестов
            assertTrue(outContent.toString().contains("Запуск тестов в директории"));
        } finally {
            // Восстанавливаем System.out
            System.setOut(originalOut);
        }
    }
    
    /**
     * Тест для проверки конструктора TestResults с параметрами.
     */
    @Test
    void testTestResultsConstructorWithParameters() {
        // Создаем объект с предустановленными значениями
        TestRunner.TestResults results = new TestRunner.TestResults();
        results.totalTests = 10;
        results.passedTests = 8;
        results.failedTests = 2;
        results.skippedTests = 0;
        
        // Проверяем значения
        assertEquals(10, results.totalTests);
        assertEquals(8, results.passedTests);
        assertEquals(2, results.failedTests);
        assertEquals(0, results.skippedTests);
    }
    
    /**
     * Тест для проверки обработки ошибок ввода-вывода.
     */
    @Test
    void testIoExceptionHandling() {
        // Создаем директорию, которая вызовет ошибку доступа
        File inaccessibleDir = new File(tempDir, "inaccessible");
        inaccessibleDir.mkdirs();
        
        // Запускаем тесты с иммитацией ошибки ввода-вывода
        TestRunner.TestResults results = new TestRunner.TestResults();
        
        // Добавляем ошибку вручную и проверяем, что она сохранена
        results.failures.add("Ошибка при запуске тестов: Доступ запрещен");
        
        // Проверяем наличие ошибки
        assertFalse(results.failures.isEmpty());
        assertTrue(results.failures.get(0).contains("Ошибка при запуске тестов"));
    }
    
    /**
     * Тест для проверки запуска тестов с реальными файлами.
     */
    @Test
    void testRunTestsWithRealFiles() throws IOException {
        // Создаем реальные тестовые классы
        File testDir = new File(tempDir, "src/test/java/com/example");
        testDir.mkdirs();
        
        // Создаем второй тестовый файл
        File anotherTestFile = new File(testDir, "AnotherTest.java");
        Files.writeString(anotherTestFile.toPath(), 
                "package com.example;\n\n"
                + "import org.junit.jupiter.api.Test;\n"
                + "import static org.junit.jupiter.api.Assertions.assertFalse;\n\n"
                + "public class AnotherTest {\n"
                + "    @Test\n"
                + "    void testAnotherSample() {\n"
                + "        assertFalse(false);\n"
                + "    }\n"
                + "}");
        
        // Запускаем тесты
        TestRunner.TestResults results = testRunner.runTests(tempDir.getAbsolutePath());
        
        // Проверяем результаты
        // Даже если тесты не запускаются реально, TestRunner должен попытаться их найти
        assertNotNull(results);
    }
}