package nsu.kochanov.runner;

import nsu.kochanov.git.GitManager;
import nsu.kochanov.model.*;

public class TaskRunner {
    private final CourseConfig config;

    public TaskRunner(CourseConfig config) {
        this.config = config;
    }

    public void run() {
        for (Group group : config.groups) {
            for (Student student : group.students) {
                System.out.println("Проверка студента: " + student.fullName);
                String localPath = "repos/" + student.githubUsername;
                GitManager.cloneOrPull(student.repoUrl, localPath);

                System.out.println("Репозиторий обработан: " + localPath);
            }
        }
    }
}
