package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Этот тестовый класс тестирует разность.
 */

class SubTest {
    @Test
    void testEval() throws MyException {
        Expression left = new Number(10);
        Expression right = new Variable("x");
        Sub sub = new Sub(left, right);
        Map<String, Integer> values = Map.of("x", 5);
        assertEquals(5, sub.eval(values));
    }

    @Test
    void testDerivative() throws MyException {
        Expression left = new Number(15);
        Expression right = new Variable("x");
        Sub sub = new Sub(left, right);
        Expression derivative = sub.derivative("x");
        assertEquals("(0-1)", derivative.print());
    }
}