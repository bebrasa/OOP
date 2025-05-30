package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Этот тестовый класс тестирует сумму.
 */

class AddTest {

    @Test
    void testPrint() {
        Expression left = new Number(2);
        Expression right = new Number(3);
        Add add = new Add(left, right);
        assertEquals("(2+3)", add.print());
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("x");
        Expression right = new Number(4);
        Add add = new Add(left, right);
        Expression derivative = add.derivative("x");
        assertEquals("(1+0)", derivative.print());
    }

    @Test
    void testEval() throws MyException {
        Expression left = new Variable("x");
        Expression right = new Number(4);
        Add add = new Add(left, right);
        Map<String, Integer> values = Map.of("x", 2);
        assertEquals(6, add.eval(values));
    }
}