package ru.nsu.kochanov;

import java.util.Map;

/**
 * Этот класс для умножения.
 */

public class Mul extends Expression {
    Expression left;
    Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        Expression ld = left.derivative(variable); //производная от левой части
        Expression rd = right.derivative(variable); //производная от правой части

        return new Add(new Mul(ld, right), new Mul(left, rd));
    }

    @Override
    public int eval(Map<String, Integer> values) throws MyException {
        return left.eval(values) * right.eval(values);
    }
}
