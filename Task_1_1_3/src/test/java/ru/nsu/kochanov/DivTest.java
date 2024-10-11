package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Этот тестовый класс тестирует деление.
 */

class DivTest {
    @Test
    void testPrint() {
        Expression left = new Number(10);
        Expression right = new Number(5);
        Div div = new Div(left, right);
        assertEquals("(10/5)", div.print());
    }

    @Test
    void testEval() throws MyException {
        Expression left = new Number(10);
        Expression right = new Variable("x");
        Div div = new Div(left, right);
        Map<String, Integer> values = Map.of("x", 2);
        assertEquals(5, div.eval(values));
    }

    @Test
    void testDerivative() throws MyException {
        Expression left = new Add(new Number(10), new Variable("x"));
        Expression right = new Mul(new Variable("x"), new Number(5));
        Expression ld = left.derivative("x");
        Expression rd = right.derivative("x");

        Sub sub = new Sub(new Mul(ld, right), new Mul(left, rd));
        Div div = new Div(sub, new Mul(right, right));
        assertEquals("((((0+1)*(x*5))-((10+x)*((1*5)+(x*0))))/((x*5)*(x*5)))", div.print());

    }

    @Test
    void testDerivative2() {
        Expression left = new Variable("x");
        Expression right = new Number(4);
        Div div = new Div(left, right);
        Expression derivative = div.derivative("x");
        assertEquals("(((1*4)-(x*0))/(4*4))", derivative.print());
    }
}