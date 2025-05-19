package nsu.kochanov.task_2_4_1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс конфигурации курса.
 * Содержит списки задач, групп и контрольных точек.
 */
public class CourseConfig {
    /** Список задач курса. */
    public List<Task> tasks = new ArrayList<>();
    
    /** Список групп студентов. */
    public List<Group> groups = new ArrayList<>();
    
    /** Список контрольных точек. */
    public List<Checkpoint> checkpoints = new ArrayList<>();

    /**
     * Добавляет задачу в список задач.
     *
     * @param task задача для добавления
     */
    public void task(Task task) {
        tasks.add(task);
    }

    /**
     * Добавляет группу в список групп.
     *
     * @param group группа для добавления
     */
    public void group(Group group) {
        groups.add(group);
    }

    /**
     * Добавляет контрольную точку в список контрольных точек.
     *
     * @param checkpoint контрольная точка для добавления
     */
    public void checkpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }
}