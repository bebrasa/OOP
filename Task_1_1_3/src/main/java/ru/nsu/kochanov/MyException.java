package ru.nsu.kochanov;

/**
    Класс для обработки исключений.
 */

public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}