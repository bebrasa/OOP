package nsu.kochanov.model;

import java.util.ArrayList;
import java.util.List;

public class CourseConfig {
    public List<Task> tasks = new ArrayList<>();
    public List<Group> groups = new ArrayList<>();
    public List<Checkpoint> checkpoints = new ArrayList<>();

    public void task(Task task) {
        tasks.add(task);
    }

    public void group(Group group) {
        groups.add(group);
    }

    public void checkpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }
}