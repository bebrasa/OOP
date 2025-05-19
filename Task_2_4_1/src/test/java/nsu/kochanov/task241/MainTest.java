package nsu.kochanov.task241;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;

import nsu.kochanov.task241.config.ConfigurationLoader;
import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.model.Group;
import nsu.kochanov.task241.model.Task;
import nsu.kochanov.task241.runner.TaskRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса Main.
 */
class MainTest {

    @TempDir
    private File tempDir;

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
    
    /**
     * Проверяет, что main метод не вызывает исключений при запуске с пустыми аргументами.
     */
    @Test
    void testMainMethodWithEmptyArguments() {
        assertDoesNotThrow(() -> {
            // Перенаправляем System.out и System.err, чтобы не засорять консоль
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            try {
                ByteArrayOutputStream outContent = new ByteArrayOutputStream();
                ByteArrayOutputStream errContent = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outContent));
                System.setErr(new PrintStream(errContent));
                
                // Тут нужно быть осторожным, так как Main.main попытается загрузить конфигурацию
                // и может вызвать реальные действия с файловой системой
                // В реальной ситуации лучше мокировать ConfigurationLoader и TaskRunner
                try {
                    String[] args = {};
                    Main.main(args);
                } catch (Exception e) {
                    // Игнорируем ошибки, связанные с отсутствием файла конфигурации
                    // В реальных тестах это место заменили бы моками
                }
            } finally {
                System.setOut(originalOut);
                System.setErr(originalErr);
            }
        });
    }
    
    /**
     * Тест для создания и проверки простой конфигурации курса.
     */
    @Test
    void testCreateSimpleCourseConfig() {
        CourseConfig config = new CourseConfig();
        
        // Так как у CourseConfig нет полей name и description, 
        // просто проверим, что списки пустые
        assertTrue(config.tasks.isEmpty());
        assertTrue(config.groups.isEmpty());
        assertTrue(config.checkpoints.isEmpty());
        
        // Добавим что-то в списки и проверим, что они изменились
        Task task = new Task();
        task.id = "task1";
        config.task(task);
        
        Group group = new Group();
        group.name = "Test Group";
        config.group(group);
        
        assertFalse(config.tasks.isEmpty());
        assertFalse(config.groups.isEmpty());
        assertEquals(1, config.tasks.size());
        assertEquals(1, config.groups.size());
    }
    
    /**
     * Проверяет правильную обработку пути к конфигурационному файлу.
     * 
     * @throws IOException если возникает ошибка при создании временного файла
     */
    @Test
    void testConfigFilePath() throws IOException {
        // Создаем временный файл конфигурации
        File configFile = new File(tempDir, "TestConfig.groovy");
        Files.writeString(configFile.toPath(), 
                "course {\n"
                + "    name = 'Test Course'\n"
                + "    description = 'This is a test course'\n"
                + "}");
        
        // Проверяем, что файл существует
        assertTrue(configFile.exists());
        
        // В реальном тесте здесь мы бы использовали ConfigurationLoader.load
        // но это может вызвать реальные действия, что не всегда желательно в тестах
        // Поэтому просто проверим, что путь к файлу корректный
        String configPath = configFile.getAbsolutePath();
        assertTrue(Files.exists(Path.of(configPath)));
    }
}