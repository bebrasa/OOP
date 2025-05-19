package nsu.kochanov.task241.compiler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса JavaCompiler.
 */
class JavaCompilerTest {

    @TempDir
    private File tempDir;
    
    /**
     * Проверяет, что компиляция не проходит, если директория не существует.
     */
    @Test
    void testCompileWithNonExistingDirectory() {
        JavaCompiler compiler = new JavaCompiler();
        assertFalse(compiler.compile("/non/existing/directory"));
    }
    
    /**
     * Проверяет, что компиляция не проходит, если директория существует, 
     * но не содержит build.gradle.
     */
    @Test
    void testCompileWithDirectoryWithoutGradle() {
        JavaCompiler compiler = new JavaCompiler();
        assertFalse(compiler.compile(tempDir.getAbsolutePath()));
    }
    
    /**
     * Проверяет, что метод compile корректно работает с директорией, содержащей build.gradle.
     *
     * @throws IOException при ошибке создания файла
     */
    @Test
    void testCompileWithValidDirectory() throws IOException {
        // Создаем build.gradle файл
        File buildGradle = new File(tempDir, "build.gradle");
        Files.writeString(buildGradle.toPath(), "// Пустой build.gradle для теста");
        
        // Для тестирования важно, чтобы метод не упал с исключением
        JavaCompiler compiler = new JavaCompiler();
        // Компиляция не пройдет успешно из-за отсутствия ./gradlew в тестовой директории,
        // но метод должен отработать без исключений
        assertFalse(compiler.compile(tempDir.getAbsolutePath()));
    }
}