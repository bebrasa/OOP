package nsu.kochanov.task241;

import nsu.kochanov.task241.config.ConfigurationLoader;
import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.runner.TaskRunner;

/**
 * Главный класс приложения для проверки студенческих заданий.
 * Загружает конфигурацию курса и запускает проверку заданий.
 */
public class Main {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        CourseConfig config = ConfigurationLoader.load("CourseConfig.groovy");
        TaskRunner runner = new TaskRunner(config);
        runner.run();
    }
}