package nsu.kochanov.task_2_4_1.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса CodeStyleChecker.
 */
class CodeStyleCheckerTest {

    @TempDir
    private File tempDir;

    /**
     * Тестирует создание объекта CodeStyleChecker.
     */
    @Test
    void testCodeStyleCheckerCreation() {
        CodeStyleChecker checker = new CodeStyleChecker();
        assertNotNull(checker);
    }

    /**
     * Тестирует проверку пустой директории.
     */
    @Test
    void testCheckEmptyDirectory() {
        CodeStyleChecker checker = new CodeStyleChecker();
        List<String> errors = checker.checkDirectory(tempDir.getAbsolutePath());
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    /**
     * Тестирует проверку директории с корректным Java файлом.
     *
     * @throws IOException при ошибке создания тестового файла
     */
    @Test
    void testCheckDirectoryWithValidJavaFile() throws IOException {
        // Создаем корректный Java файл
        Path javaFile = tempDir.toPath().resolve("Test.java");
        String content = "package test;\n\n"
                + "public class Test {\n"
                + "    public static void main(String[] args) {\n"
                + "        System.out.println(\"Hello, World!\");\n"
                + "    }\n"
                + "}\n";
        Files.writeString(javaFile, content);

        CodeStyleChecker checker = new CodeStyleChecker();
        List<String> errors = checker.checkDirectory(tempDir.getAbsolutePath());
        
        // Проверяем, что ошибок нет
        assertTrue(errors.isEmpty());
    }

    /**
     * Тестирует поведение при несуществующей директории.
     */
    @Test
    void testCheckNonExistingDirectory() {
        CodeStyleChecker checker = new CodeStyleChecker();
        List<String> errors = checker.checkDirectory("/non/existing/directory");
        
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Ошибка при проверке стиля кода"));
    }
}