package ru.nsu.kochanov;

import java.util.Map;

public class Sub extends Expression {
    Expression left, right;

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }

    @Override
    public int eval(Map<String, Integer> values) {
        return left.eval(values) - right.eval(values);
    }

    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }
}
