package ru.nsu.kochanov;

import java.util.Map;

/**
 * Этот абсрактный класс нужен для объявления методов.
 */

public abstract class Expression {
    public abstract String print();

    public abstract Expression derivative(String variable);

    public abstract int eval(Map<String, Integer> values) throws MyException;
}
