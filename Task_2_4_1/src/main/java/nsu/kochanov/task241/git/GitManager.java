package nsu.kochanov.task241.git;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Класс для работы с Git-репозиториями.
 */
public class GitManager {

    /**
     * Клонирует или обновляет Git-репозиторий.
     *
     * @param repoUrl   URL Git-репозитория
     * @param targetDir локальная директория для клонирования
     * @return true, если операция успешна, иначе false
     */
    public static boolean cloneOrPull(String repoUrl, String targetDir) {
        try {
            File dir = new File(targetDir);
            ProcessBuilder builder;
            Process process;

            if (dir.exists()) {
                System.out.println("Обновление репозитория: " + targetDir);
                builder = new ProcessBuilder("git", "-C", targetDir, "pull");
                process = builder.start();
            } else {
                System.out.println("Клонирование репозитория: " + repoUrl);
                builder = new ProcessBuilder("git", "clone", repoUrl, targetDir);
                process = builder.start();
            }

            // Читаем вывод процесса
            String output = new String(process.getInputStream().readAllBytes());
            String error = new String(process.getErrorStream().readAllBytes());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("Ошибка при работе с git:");
                System.err.println("Вывод: " + output);
                System.err.println("Ошибки: " + error);
                return false;
            }

            System.out.println("Git операция успешно завершена");
            System.out.println("Вывод: " + output);

            // Удаление папки Task_2_3_1 после клонирования или обновления
            File taskDir = new File(targetDir, "Task_2_3_1");
            if (taskDir.exists()) {
                System.out.println("Удаление директории Task_2_3_1");
                deleteDirectory(taskDir);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при работе с git: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Рекурсивно удаляет директорию и ее содержимое.
     *
     * @param directory директория для удаления
     * @throws IOException при ошибке доступа к файлам
     */
    private static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }
        Files.walkFileTree(
                directory.toPath(),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                            throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                            throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
    }
}
