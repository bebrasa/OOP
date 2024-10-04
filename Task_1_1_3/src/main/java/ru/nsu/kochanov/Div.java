package ru.nsu.kochanov;

import java.util.Map;

/**
 * Этот класс для деления.
 */

public class Div extends Expression {
    Expression left;
    Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")"; //нужно исключение добавить на нули
    }

    @Override
    public int eval(Map<String, Integer> values) {
        return left.eval(values) / right.eval(values); // нужно исключение
    }

    @Override
    public Expression derivative(String variable) {
        Expression ld = left.derivative(variable);
        Expression rd = right.derivative(variable);
        Sub result = new Sub(new Mul(ld, right), new Mul(left, rd));
        return new Div(result, new Mul(right, right)); //нужно исключените
    }
}
