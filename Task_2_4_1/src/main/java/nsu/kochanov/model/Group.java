package nsu.kochanov.model;


import java.util.ArrayList;
import java.util.List;

public class Group {
    public String name;
    public List<Student> students = new ArrayList<>();

    public void student(Student s) {
        students.add(s);
    }
}
