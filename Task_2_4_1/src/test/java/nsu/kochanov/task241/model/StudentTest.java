package nsu.kochanov.task241.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StudentTest {

  @Test
  void testCreateStudent() {
    Student student = new Student();
    assertNotNull(student);
  }

  @Test
  void testSetStudentFields() {
    Student student = new Student();

    student.githubUsername = "student1";
    student.fullName = "Иванов Иван Иванович";
    student.repoUrl = "https://github.com/student1/repo";

    assertEquals("student1", student.githubUsername);
    assertEquals("Иванов Иван Иванович", student.fullName);
    assertEquals("https://github.com/student1/repo", student.repoUrl);
  }

  @Test
  void testStudentFieldsInitiallyNull() {
    Student student = new Student();

    assertNull(student.githubUsername);
    assertNull(student.fullName);
    assertNull(student.repoUrl);
  }
}
