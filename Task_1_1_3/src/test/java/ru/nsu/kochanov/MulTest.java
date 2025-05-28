package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Этот тестовый класс тестирует произведение.
 */

class MulTest {
    @Test
    void testEval() throws MyException {
        Expression left = new Number(5);
        Expression right = new Variable("x");

        Mul mul = new Mul(left, right);
        Map<String, Integer> values = Map.of("x", 2);
        assertEquals(10, mul.eval(values));
    }
}