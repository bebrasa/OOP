package nsu.kochanov.config;

import nsu.kochanov.model.CourseConfig;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;

public class ConfigurationLoader {
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