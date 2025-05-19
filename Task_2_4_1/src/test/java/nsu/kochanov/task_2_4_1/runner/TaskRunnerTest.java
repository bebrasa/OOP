package nsu.kochanov.task_2_4_1.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import nsu.kochanov.task_2_4_1.model.CourseConfig;
import nsu.kochanov.task_2_4_1.model.Group;
import nsu.kochanov.task_2_4_1.model.Student;
import nsu.kochanov.task_2_4_1.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса TaskRunner.
 */
class TaskRunnerTest {

  private CourseConfig config;
  private TaskRunner runner;

  /**
   * Создаёт конфигурацию для тестов перед каждым тестом.
   */
  @BeforeEach
  void setUp() {
    config = new CourseConfig();
    
    // Добавляем тестовую задачу
    Task task = new Task();
    task.id = "test1";
    task.name = "Test Task";
    task.maxScore = 10;
    task.softDeadline = LocalDate.now().minusDays(1);
    task.hardDeadline = LocalDate.now().plusDays(1);
    config.task(task);
    
    // Добавляем тестовую группу и студента
    Group group = new Group();
    group.name = "TestGroup";
    
    Student student = new Student();
    student.fullName = "Test Student";
    student.githubUsername = "testuser";
    student.repoUrl = "https://github.com/testuser/testrepo";
    group.student(student);
    
    config.group(group);
    
    // Создаем экземпляр TaskRunner
    runner = new TaskRunner(config);
  }

  /**
   * Проверяет создание TaskRunner.
   */
  @Test
  void testTaskRunnerCreation() {
    assertNotNull(runner);
  }

  /**
   * Проверяет расчет баллов за задачу.
   * Тест использует приватный метод через рефлексию.
   */
  @Test
  void testCalculateScore() throws Exception {
    // Создаем тестовую задачу
    Task task = new Task();
    task.maxScore = 10;
    task.softDeadline = LocalDate.now().minusDays(2);
    task.hardDeadline = LocalDate.now().minusDays(1);
    
    // Получаем доступ к приватному методу calculateScore через рефлексию
    java.lang.reflect.Method calculateScoreMethod = TaskRunner.class.getDeclaredMethod(
        "calculateScore", 
        Task.class, 
        nsu.kochanov.task_2_4_1.test.TestRunner.TestResults.class,
        java.util.List.class,
        boolean.class);
    calculateScoreMethod.setAccessible(true);
    
    // Создаем результаты тестов
    nsu.kochanov.task_2_4_1.test.TestRunner.TestResults testResults = 
        new nsu.kochanov.task_2_4_1.test.TestRunner.TestResults();
    testResults.totalTests = 10;
    testResults.passedTests = 10;
    
    // Вызываем метод
    double score = (double) calculateScoreMethod.invoke(
        runner, task, testResults, java.util.List.of(), true);
    
    // Проверяем, что баллы рассчитываются с учетом просрочки жесткого дедлайна
    assertEquals(5.0, score, 0.01);
  }

  /**
   * Проверяет, что метод run не выбрасывает исключений.
   */
  @Test
  void testRunDoesNotThrowExceptions() {
    assertDoesNotThrow(() -> {
      CourseConfig testConfig = new CourseConfig();
      TaskRunner testRunner = new TaskRunner(testConfig);
      testRunner.run();
    });
  }
}