package nsu.kochanov.task241.git;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса GitManager.
 */
class GitManagerTest {

    @TempDir
    private File tempDir;

    /**
     * Тестирует, что клонирование несуществующего репозитория завершается с ошибкой.
     */
    @Test
    void testCloneNonExistingRepo() {
        String nonExistingRepo = "https://github.com/non/existing/repo.git";
        assertFalse(GitManager.cloneOrPull(nonExistingRepo, tempDir.getAbsolutePath()));
    }

    /**
     * Тестирует клонирование с неверным URL.
     */
    @Test
    void testCloneWithInvalidUrl() {
        String invalidUrl = "invalid-url";
        assertFalse(GitManager.cloneOrPull(invalidUrl, tempDir.getAbsolutePath()));
    }

    /**
     * Тестирует, что метод cloneOrPull обрабатывает неверный путь директории.
     */
    @Test
    void testCloneWithInvalidDirectory() {
        String validRepo = "https://github.com/valid/repo.git";
        String invalidDir = "/invalid/directory/that/cannot/be/created";
        assertFalse(GitManager.cloneOrPull(validRepo, invalidDir));
    }

    /**
     * Тест проверяет доступ к статическому методу deleteDirectory.
     *
     * @throws Exception если происходит ошибка при вызове метода через рефлексию
     */
    @Test
    void testDeleteDirectoryMethod() throws Exception {
        // Создаем временную директорию для тестирования
        File testDir = new File(tempDir, "test_delete");
        testDir.mkdir();
        
        // Получаем доступ к приватному методу через рефлексию
        java.lang.reflect.Method deleteDirectoryMethod = 
                GitManager.class.getDeclaredMethod("deleteDirectory", File.class);
        deleteDirectoryMethod.setAccessible(true);
        
        // Проверяем, что метод не выбрасывает исключений
        assertNotNull(deleteDirectoryMethod);
        deleteDirectoryMethod.invoke(null, testDir);
    }
}