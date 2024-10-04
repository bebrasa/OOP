package ru.nsu.kochanov;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MulTest {
    @Test
    void testEval(){
        Expression left = new Number(5);
        Expression right = new Variable("x");

        Mul mul = new Mul(left, right);
        Map<String, Integer> values = Map.of("x", 2);
        assertEquals(10, mul.eval(values));
    }
}