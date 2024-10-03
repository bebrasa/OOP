package ru.nsu.kochanov;

import java.util.Map;

public class Number extends Expression {
    int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public String print() {
        return Integer.toString(value);
    }

    @Override
    public int eval(Map<String, Integer> values) {
        return value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }
}
