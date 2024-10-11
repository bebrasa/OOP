package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Это тест для мейн класса.
 */

public class MainTest {

    @Test
    public void testExpressionPrinting() throws MyException {
        // Создание выражения
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        // Ожидаемое представление выражения
        String expectedPrint = "(3+(2*x))";
        // Проверка
        assertEquals(expectedPrint, e.print());
    }

    @Test
    public void testDerivative() throws MyException{
        // Создание выражения
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        // Вычисление производной
        Expression derivative = e.derivative("x");
        // Ожидаемое значение производной
        String expectedDerivative = "(0+((0*x)+(2*1)))";
        // Проверка
        assertEquals(expectedDerivative, derivative.print());
    }

    @Test
    public void testEvaluation() throws MyException {
        // Создание выражения
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        // Установка переменной x
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        // Ожидаемое значение при x = 10
        int expectedResult = 23;
        // Проверка
        assertEquals(expectedResult, e.eval(variables));
    }
}