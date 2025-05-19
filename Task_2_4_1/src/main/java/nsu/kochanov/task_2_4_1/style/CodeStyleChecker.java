package nsu.kochanov.task_2_4_1.style;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для проверки стиля Java-кода с использованием Google Java Format.
 */
public class CodeStyleChecker {
    private final Formatter formatter;

    /**
     * Создает объект для проверки стиля кода по правилам Google.
     */
    public CodeStyleChecker() {
        JavaFormatterOptions options = JavaFormatterOptions.builder()
                .style(JavaFormatterOptions.Style.GOOGLE)
                .build();
        this.formatter = new Formatter(options);
    }

    /**
     * Проверяет стиль кода в указанной директории.
     *
     * @param directoryPath путь к директории с исходным кодом
     * @return список ошибок стиля кода
     */
    public List<String> checkDirectory(String directoryPath) {
        List<String> errors = new ArrayList<>();
        try {
            Files.walk(Path.of(directoryPath))
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String source = Files.readString(path);
                            formatter.formatSource(source);
                        } catch (FormatterException | IOException e) {
                            errors.add("Ошибка форматирования в файле " + path + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            errors.add("Ошибка при проверке стиля кода: " + e.getMessage());
        }
        return errors;
    }
} 