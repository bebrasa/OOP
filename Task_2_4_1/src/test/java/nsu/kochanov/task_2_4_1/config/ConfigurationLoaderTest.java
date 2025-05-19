package nsu.kochanov.task_2_4_1.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса ConfigurationLoader.
 */
class ConfigurationLoaderTest {

  @TempDir
  private File tempDir;
  private File configFile;

  /**
   * Создаёт временный конфигурационный файл перед каждым тестом.
   * @throws IOException если возникли проблемы при создании файла
   */
  @BeforeEach
  void setUp() throws IOException {
    configFile = new File(tempDir, "testConfig.groovy");
    try (FileWriter writer = new FileWriter(configFile)) {
      writer.write("course.task(new nsu.kochanov.task_2_4_1.model.Task())\n");
      writer.write("course.group(new nsu.kochanov.task_2_4_1.model.Group())\n");
      writer.write("course.checkpoint(new nsu.kochanov.task_2_4_1.model.Checkpoint())\n");
    }
  }

  /**
   * Очищает временные файлы после тестов.
   * @throws IOException если возникли проблемы при удалении файла
   */
  @AfterEach
  void tearDown() throws IOException {
    if (configFile != null && configFile.exists()) {
      Files.delete(configFile.toPath());
    }
  }

  /**
   * Проверяет загрузку конфигурации из существующего файла.
   */
  @Test
  void testLoadFromExistingFile() {
    assertNotNull(ConfigurationLoader.load(configFile.getAbsolutePath()));
  }

  /**
   * Проверяет поведение при попытке загрузить несуществующий файл.
   */
  @Test
  void testLoadFromNonExistingFile() {
    assertNull(ConfigurationLoader.load("non_existing_file.groovy"));
  }
}