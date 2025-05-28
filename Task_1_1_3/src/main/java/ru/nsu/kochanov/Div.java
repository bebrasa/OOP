package ru.nsu.kochanov;

import java.io.IOException;
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
    public int eval(Map<String, Integer> values) throws MyException {
        int rightValue = right.eval(values); // Сначала вычисляем правую часть

        // Проверка на деление на ноль
        if (rightValue == 0) {
            throw new MyException("Деление на 0 невозможно");
        }

        // Если деление возможно, выполняем его
        return left.eval(values) / rightValue;
    }

    @Override
    public Expression derivative(String variable) {
        Expression ld = left.derivative(variable);
        Expression rd = right.derivative(variable);
        Sub result = new Sub(new Mul(ld, right), new Mul(left, rd));
        return new Div(result, new Mul(right, right)); //нужно исключените
    }
}
