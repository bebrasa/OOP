package ru.nsu.kochanov;

import java.util.Map;

/**
 * Этот класс для сложения.
 */

public class Add extends Expression {
    Expression left;
    Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "+" + right.print() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    @Override
    public int eval(Map<String, Integer> values) throws MyException {
        return left.eval(values) + right.eval(values);
    }
}
