package nsu.kochanov;

import nsu.kochanov.config.ConfigurationLoader;
import nsu.kochanov.model.CourseConfig;
import nsu.kochanov.runner.TaskRunner;

public class Main {
    public static void main(String[] args) {
        CourseConfig config = ConfigurationLoader.load("CourseConfig.groovy");
        TaskRunner runner = new TaskRunner(config);
        runner.run();
    }
}