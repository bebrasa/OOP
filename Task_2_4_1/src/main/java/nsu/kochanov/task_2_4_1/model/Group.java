package nsu.kochanov.task_2_4_1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий группу студентов.
 */
public class Group {
    /** Название группы. */
    public String name;
    
    /** Список студентов в группе. */
    public List<Student> students = new ArrayList<>();

    /**
     * Добавляет студента в группу.
     *
     * @param s студент для добавления
     */
    public void student(Student s) {
        students.add(s);
    }
}
