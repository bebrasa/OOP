package nsu.kochanov.task_2_4_1;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nsu.kochanov.task_2_4_1.config.ConfigurationLoader;
import nsu.kochanov.task_2_4_1.model.CourseConfig;
import nsu.kochanov.task_2_4_1.runner.TaskRunner;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Main.
 */
class MainTest {

  /**
   * Проверяет, что класс Main можно инстанцировать.
   */
  @Test
  void testMainClassCanBeInstantiated() {
    Main main = new Main();
    assertNotNull(main);
  }
  
  /**
   * Тест проверяет, что CourseConfig не null.
   */
  @Test
  void testCourseConfigNotNull() {
    CourseConfig config = new CourseConfig();
    assertNotNull(config);
    assertNotNull(config.tasks);
    assertNotNull(config.groups);
    assertNotNull(config.checkpoints);
  }
}