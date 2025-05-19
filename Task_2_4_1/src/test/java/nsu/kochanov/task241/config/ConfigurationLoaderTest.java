package nsu.kochanov.task241.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import nsu.kochanov.task241.model.CourseConfig;
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
    private File validConfigFile;
    private File invalidConfigFile;
    private ByteArrayOutputStream errorOutput;
    private PrintStream originalErr;

    /**
     * Создаёт временные конфигурационные файлы перед каждым тестом
     * и перенаправляет поток ошибок.
     *
     * @throws IOException если возникли проблемы при создании файлов
     */
    @BeforeEach
    void setUp() throws IOException {
        // Сохраняем оригинальный поток ошибок и перенаправляем его
        originalErr = System.err;
        errorOutput = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorOutput));
        
        // Создаем валидный конфигурационный файл
        validConfigFile = new File(tempDir, "validConfig.groovy");
        try (FileWriter writer = new FileWriter(validConfigFile)) {
            writer.write("import nsu.kochanov.task241.model.*\n\n");
            writer.write("course {\n");
            writer.write("    task {\n");
            writer.write("        id = 'lab1'\n");
            writer.write("        name = 'Задание 1'\n");
            writer.write("        maxScore = 10\n");
            writer.write("    }\n");
            writer.write("    group {\n");
            writer.write("        name = 'Группа 1'\n");
            writer.write("        student {\n");
            writer.write("            fullName = 'Иванов И.И.'\n");
            writer.write("            githubUsername = 'ivanov'\n");
            writer.write("            repoUrl = 'https://github.com/ivanov/repo'\n");
            writer.write("        }\n");
            writer.write("    }\n");
            writer.write("}\n");
        }
        
        // Создаем невалидный конфигурационный файл
        invalidConfigFile = new File(tempDir, "invalidConfig.groovy");
        try (FileWriter writer = new FileWriter(invalidConfigFile)) {
            writer.write("This is not a valid Groovy script");
        }
        
        // Файл для совместимости с предыдущими тестами
        configFile = validConfigFile;
    }

    /**
     * Очищает временные файлы после тестов и восстанавливает поток ошибок.
     *
     * @throws IOException если возникли проблемы при удалении файлов
     */
    @AfterEach
    void tearDown() throws IOException {
        if (validConfigFile != null && validConfigFile.exists()) {
            Files.delete(validConfigFile.toPath());
        }
        if (invalidConfigFile != null && invalidConfigFile.exists()) {
            Files.delete(invalidConfigFile.toPath());
        }
        
        // Восстанавливаем оригинальный поток ошибок
        System.setErr(originalErr);
    }

    /**
     * Проверяет загрузку конфигурации из существующего файла.
     */
    @Test
    void testLoadFromExistingFile() {
        CourseConfig config = ConfigurationLoader.load(validConfigFile.getAbsolutePath());
        assertNotNull(config);
    }

    /**
     * Проверяет поведение при попытке загрузить несуществующий файл.
     */
    @Test
    void testLoadFromNonExistingFile() {
        assertNull(ConfigurationLoader.load("non_existing_file.groovy"));
        assertTrue(errorOutput.toString().contains("Ошибка при чтении конфигурационного файла"));
    }
    
    /**
     * Проверяет загрузку невалидного конфигурационного файла.
     */
    @Test
    void testLoadInvalidConfig() {
        assertNull(ConfigurationLoader.load(invalidConfigFile.getAbsolutePath()));
        assertTrue(errorOutput.toString().contains("Ошибка при чтении конфигурационного файла"));
    }
    
    /**
     * Проверяет содержимое загруженной конфигурации.
     */
    @Test
    void testConfigContent() {
        CourseConfig config = ConfigurationLoader.load(validConfigFile.getAbsolutePath());
        
        // Проверяем загрузку задачи
        assertNotNull(config.tasks);
        assertEquals(1, config.tasks.size());
        assertEquals("lab1", config.tasks.get(0).id);
        assertEquals("Задание 1", config.tasks.get(0).name);
        assertEquals(10, config.tasks.get(0).maxScore);
        
        // Проверяем загрузку группы и студента
        assertNotNull(config.groups);
        assertEquals(1, config.groups.size());
        assertEquals("Группа 1", config.groups.get(0).name);
        
        assertNotNull(config.groups.get(0).students);
        assertEquals(1, config.groups.get(0).students.size());
        assertEquals("Иванов И.И.", config.groups.get(0).students.get(0).fullName);
    }
    
    /**
     * Проверяет загрузку пустого конфигурационного файла.
     */
    @Test
    void testLoadEmptyConfig() throws IOException {
        // Создаем пустой конфигурационный файл
        File emptyConfigFile = new File(tempDir, "emptyConfig.groovy");
        try (FileWriter writer = new FileWriter(emptyConfigFile)) {
            writer.write("");
        }
        
        // Загружаем конфигурацию
        CourseConfig config = ConfigurationLoader.load(emptyConfigFile.getAbsolutePath());
        
        // Проверяем, что конфигурация создана, но пуста
        assertNotNull(config);
        assertTrue(config.tasks.isEmpty());
        assertTrue(config.groups.isEmpty());
        assertTrue(config.checkpoints.isEmpty());
        
        // Удаляем временный файл
        Files.delete(emptyConfigFile.toPath());
    }
    
    /**
     * Проверяет загрузку конфигурации с некорректным синтаксисом.
     */
    @Test
    void testLoadConfigWithSyntaxErrors() throws IOException {
        // Создаем файл с синтаксическими ошибками
        File syntaxErrorConfigFile = new File(tempDir, "syntaxErrorConfig.groovy");
        try (FileWriter writer = new FileWriter(syntaxErrorConfigFile)) {
            writer.write("course {\n");
            writer.write("    task {\n");
            writer.write("        id = 'lab1'\n");
            writer.write("        name = 'Задание 1'\n");
            writer.write("        maxScore = 10\n");
            // Пропущена закрывающая скобка
        }
        
        // Загружаем конфигурацию
        assertNull(ConfigurationLoader.load(syntaxErrorConfigFile.getAbsolutePath()));
        assertTrue(errorOutput.toString().contains("Ошибка при чтении конфигурационного файла"));
        
        // Удаляем временный файл
        Files.delete(syntaxErrorConfigFile.toPath());
    }
}