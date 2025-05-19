package nsu.kochanov.task241.compiler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Класс для компиляции Java-проектов с использованием Gradle. */
public class JavaCompiler {

  /**
   * Компилирует Java-проект в указанной директории.
   *
   * @param sourceDir путь к директории с исходным кодом
   * @return true, если компиляция успешна, иначе false
   */
  public boolean compile(String sourceDir) {
    try {
      // Ищем все build.gradle файлы в подпапках
      List<Path> gradleFiles = new ArrayList<>();
      Files.walk(Path.of(sourceDir))
          .filter(path -> path.getFileName().toString().equals("build.gradle"))
          .forEach(gradleFiles::add);

      if (gradleFiles.isEmpty()) {
        System.err.println("Не найдено build.gradle файлов в директории: " + sourceDir);
        return false;
      }

      boolean allCompiled = true;
      for (Path gradleFile : gradleFiles) {
        File projectDir = gradleFile.getParent().toFile();
        System.out.println("Компиляция проекта в директории: " + projectDir.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectDir);
        processBuilder.command("./gradlew", "clean", "compileJava");

        // Перенаправляем вывод в файлы
        File outputFile = new File(projectDir, "compile-output.txt");
        File errorFile = new File(projectDir, "compile-error.txt");
        processBuilder.redirectOutput(outputFile);
        processBuilder.redirectError(errorFile);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        // Проверяем результат компиляции
        if (exitCode != 0) {
          allCompiled = false;
          // Читаем ошибки компиляции
          if (errorFile.exists()) {
            String errors = new String(Files.readAllBytes(errorFile.toPath()));
            System.err.println(
                "Ошибки компиляции в " + projectDir.getAbsolutePath() + ":\n" + errors);
          }
        }

        // Очищаем временные файлы
        outputFile.delete();
        errorFile.delete();
      }

      return allCompiled;
    } catch (Exception e) {
      System.err.println("Ошибка при компиляции: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
}
