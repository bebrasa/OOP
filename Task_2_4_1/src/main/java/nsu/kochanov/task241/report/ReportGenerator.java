package nsu.kochanov.task241.report;

import java.util.List;

import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.test.TestRunner.TestResults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Класс для генерации отчетов о проверке заданий.
 */
public class ReportGenerator {

    /**
     * Генерирует отчет о проверке задания студента.
     *
     * @param config             конфигурация курса
     * @param studentName        имя студента
     * @param taskId             идентификатор задания
     * @param compilationSuccess успешность компиляции
     * @param styleErrors        список ошибок стиля
     * @param testResults        результаты тестирования
     * @param score              итоговый балл
     */
    public void generateReport(
            CourseConfig config,
            String studentName,
            String taskId,
            boolean compilationSuccess,
            List<String> styleErrors,
            TestResults testResults,
            double score) {
        StringBuilder report = new StringBuilder();

        // Заголовок
        report.append("\n=== Отчет по проверке задания ===\n\n");
        report.append("Студент: ").append(studentName).append("\n");
        report.append("Задание: ").append(taskId).append("\n\n");

        // Результаты компиляции
        report.append("Результаты компиляции:\n");
        report.append(compilationSuccess ? "✓ Компиляция успешна\n" : "✗ Ошибка компиляции\n\n");

        // Проверка стиля
        report.append("Проверка стиля кода:\n");
        if (styleErrors.isEmpty()) {
            report.append("✓ Стиль кода соответствует требованиям\n\n");
        } else {
            report.append("✗ Найдены ошибки в стиле кода:\n");
            for (String error : styleErrors) {
                report.append("  - ").append(error).append("\n");
            }
            report.append("\n");
        }

        // Результаты тестов
        report.append("Результаты тестов:\n");
        report.append(String.format("Всего тестов: %d\n", testResults.totalTests));
        report.append(String.format("Успешно: %d\n", testResults.passedTests));
        report.append(String.format("Провалено: %d\n", testResults.failedTests));
        report.append(String.format("Пропущено: %d\n", testResults.skippedTests));

        if (!testResults.failures.isEmpty()) {
            report.append("\nОшибки тестов:\n");
            for (String failure : testResults.failures) {
                report.append("  - ").append(failure).append("\n");
            }
        }
        report.append("\n");

        // Итоговый балл
        report.append("Итоговый балл: ").append(String.format("%.2f", score)).append("\n");
        report.append("===============================\n");

        System.out.println(report.toString());
    }
}
