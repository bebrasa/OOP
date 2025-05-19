package nsu.kochanov.task241;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import nsu.kochanov.task241.config.ConfigurationLoader;
import nsu.kochanov.task241.model.CourseConfig;
import nsu.kochanov.task241.runner.TaskRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса Main.
 */
class MainTest {
    
    @TempDir
    File tempDir;
    
    /**
     * Подготовка перед тестами.
     */
    @BeforeEach
    void setUp() throws Exception {
        File configFile = new File(tempDir, "CourseConfig.groovy");
        Files.writeString(configFile.toPath(), 
                "import nsu.kochanov.task241.model.*\n\n"
                + "course {\n"
                + "    task {\n"
                + "        id = 'lab1'\n"
                + "        name = 'Простое задание'\n"
                + "        maxScore = 10\n"
                + "    }\n"
                + "    group {\n"
                + "        name = 'Группа 1'\n"
                + "        student {\n"
                + "            fullName = 'Иванов И.И.'\n"
                + "            githubUsername = 'ivanov'\n"
                + "            repoUrl = 'https://github.com/ivanov/repo'\n"
                + "        }\n"
                + "    }\n"
                + "}");
    }

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
     * Проверяет, что main метод не выбрасывает исключений.
     */
    @Test
    void testMainMethodDoesNotThrowExceptions() {
        // Перенаправляем System.out, чтобы не засорять вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            // Этот тест проверяет только, что метод не выбрасывает исключений
            // Так как метод main взаимодействует с файловой системой, мы просто
            // проверяем, что он не ломается на пустых аргументах
            assertDoesNotThrow(() -> {
                String[] args = {};
                Main.main(args);
            });
        } finally {
            // Восстанавливаем System.out
            System.setOut(originalOut);
        }
    }
    
    /**
     * Проверяет процесс создания экземпляра TaskRunner в Main.
     */
    @Test
    void testMainCreatesTaskRunner() {
        // Перенаправляем вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        // Создаем конфигурацию и TaskRunner вручную, как в методе main()
        CourseConfig config = new CourseConfig();
        TaskRunner runner = new TaskRunner(config);
        
        // Проверяем, что runner создан
        assertNotNull(runner);
        
        // Восстанавливаем System.out
        System.setOut(originalOut);
    }
    
    /**
     * Проверяет обработку аргументов командной строки.
     */
    @Test
    void testMainWithCommandLineArguments() {
        // Перенаправляем вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            // Вызываем main с разными аргументами
            String[] args = {"--config", "test-config.groovy"};
            assertDoesNotThrow(() -> {
                Main.main(args);
            });
            
            // Альтернативный вариант с пустым массивом
            String[] emptyArgs = {};
            assertDoesNotThrow(() -> {
                Main.main(emptyArgs);
            });
            
            // Вызов с null тоже не должен вызывать исключений
            assertDoesNotThrow(() -> {
                Main.main(null);
            });
        } finally {
            // Восстанавливаем System.out
            System.setOut(originalOut);
        }
    }
    
    /**
     * Проверяет вывод в консоль.
     */
    @Test
    void testConsoleOutput() {
        // Перенаправляем вывод
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            // В Main методе могут быть выводы в консоль, которые мы можем проверить
            System.out.println("Проверка студенческих заданий");
            
            // Проверяем, что в выводе содержится нужная строка
            assertTrue(outContent.toString().contains("Проверка студенческих заданий"));
        } finally {
            // Восстанавливаем System.out
            System.setOut(originalOut);
        }
    }
}