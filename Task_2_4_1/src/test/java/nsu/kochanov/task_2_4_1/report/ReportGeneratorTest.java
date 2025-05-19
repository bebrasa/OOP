package nsu.kochanov.task_2_4_1.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import nsu.kochanov.task_2_4_1.model.CourseConfig;
import nsu.kochanov.task_2_4_1.test.TestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса ReportGenerator.
 */
class ReportGeneratorTest {

    private ReportGenerator reportGenerator;
    private CourseConfig config;
    private TestRunner.TestResults testResults;

    /**
     * Инициализация объектов перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGenerator();
        config = new CourseConfig();
        testResults = new TestRunner.TestResults();
        testResults.totalTests = 10;
        testResults.passedTests = 8;
        testResults.failedTests = 1;
        testResults.skippedTests = 1;
    }

    /**
     * Тестирует создание объекта ReportGenerator.
     */
    @Test
    void testReportGeneratorCreation() {
        assertNotNull(reportGenerator);
    }

    /**
     * Тестирует, что метод generateReport не выбрасывает исключений при успешной компиляции.
     */
    @Test
    void testGenerateReportSuccess() {
        List<String> styleErrors = new ArrayList<>();
        assertDoesNotThrow(() -> 
                reportGenerator.generateReport(
                        config, 
                        "Test Student", 
                        "task1", 
                        true, 
                        styleErrors, 
                        testResults, 
                        9.5));
    }

    /**
     * Тестирует, что метод generateReport не выбрасывает исключений при ошибках компиляции.
     */
    @Test
    void testGenerateReportFailure() {
        List<String> styleErrors = List.of("Ошибка стиля 1", "Ошибка стиля 2");
        testResults.failures.add("Тест 1 не прошел");
        
        assertDoesNotThrow(() -> 
                reportGenerator.generateReport(
                        config, 
                        "Test Student", 
                        "task1", 
                        false, 
                        styleErrors, 
                        testResults, 
                        0.0));
    }
}