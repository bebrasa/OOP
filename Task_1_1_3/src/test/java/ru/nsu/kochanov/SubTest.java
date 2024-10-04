package ru.nsu.kochanov;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubTest {
    @Test
    void testEval(){
        Expression left = new Number(10);
        Expression right = new Variable("x");
        Sub sub = new Sub(left, right);
        Map<String, Integer> values = Map.of("x", 5);
        assertEquals(5,sub.eval(values));
    }

    @Test
    void testDerivative(){
        Expression left = new Number(15);
        Expression right = new Variable("x");
        Sub sub = new Sub(left, right);
        Expression derivative = sub.derivative("x");
        assertEquals("(0-1)", derivative.print());
    }
}