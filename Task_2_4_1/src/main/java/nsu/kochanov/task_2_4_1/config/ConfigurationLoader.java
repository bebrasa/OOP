package nsu.kochanov.task_2_4_1.config;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import nsu.kochanov.task_2_4_1.model.CourseConfig;

/**
 * Класс для загрузки конфигурации курса из Groovy-скрипта.
 */
public class ConfigurationLoader {

    /**
     * Загружает конфигурацию курса из файла Groovy-скрипта.
     *
     * @param configPath путь к файлу конфигурации
     * @return объект конфигурации курса или null в случае ошибки
     */
    public static CourseConfig load(String configPath) {
        Binding binding = new Binding();
        binding.setVariable("course", new CourseConfig());
        GroovyShell shell = new GroovyShell(binding);
        try {
            shell.evaluate(new File(configPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при чтении конфигурационного файла: " + configPath);
            return null;
        }
        return (CourseConfig) binding.getVariable("course");
    }
}