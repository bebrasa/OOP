package ru.nsu.kochanov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

/**
 * Это тесты для парсера.
 */

public class ExpressionParserTest {

    @Test
    public void testParseSimpleAddition() throws MyException {
        Expression e = ExpressionParser.parse("3+5");
        assertEquals("(3+5)", e.print());

        Map<String, Integer> variables = new HashMap<>();
        assertEquals(8, e.eval(variables)); // Ожидаемый результат: 3 + 5 = 8
    }

    @Test
    public void testParseWithVariable() throws MyException {
        Expression e = ExpressionParser.parse("3+x");
        assertEquals("(3+x)", e.print());

        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 7);
        assertEquals(10, e.eval(variables)); // Ожидаемый результат: 3 + 7 = 10
    }

    @Test
    public void testParseWithMultipleOperators() throws MyException {
        Expression e = ExpressionParser.parse("3+2*x-5");
        assertEquals("((3+(2*x))-5)", e.print());

        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 4);
        assertEquals(6, e.eval(variables)); // Ожидаемый результат: 3 + (2*4) - 5 = 6
    }

    @Test
    public void testParseWithParentheses() throws MyException {
        Expression e = ExpressionParser.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", e.print());

        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 4);
        assertEquals(11, e.eval(variables)); // Ожидаемый результат: 3 + (2*4) = 11
    }

    @Test
    public void testParseMultiplicationAndDivision() throws MyException {
        Expression e = ExpressionParser.parse("10/2*3");
        assertEquals("((10/2)*3)", e.print());

        Map<String, Integer> variables = new HashMap<>();
        assertEquals(15, e.eval(variables)); // Ожидаемый результат: (10 / 2) * 3 = 15
    }

    @Test
    public void testParseWithNestedParentheses() throws MyException {
        Expression e = ExpressionParser.parse("(2*(3+(4*x)))");
        assertEquals("(2*(3+(4*x)))", e.print());

        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 2);
        assertEquals(22, e.eval(variables)); // Ожидаемый результат: 2 * (3 + (4*2)) = 22
    }

    @Test
    public void testParseWithZeroMultiplication() throws MyException {
        Expression e = ExpressionParser.parse("0*x");
        assertEquals("(0*x)", e.print());

        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, e.eval(variables)); // Ожидаемый результат: 0 * 10 = 0
    }

    @Test
    public void testParseDivisionByZero() {
        // Проверка обработки ошибки деления на 0
        Exception exception = assertThrows(MyException.class, () -> {
            Expression e = ExpressionParser.parse("10/0");
            Map<String, Integer> variables = new HashMap<>();
            e.eval(variables); // Ожидаемое поведение: выброс исключения
        });

        assertTrue(exception.getMessage().contains("Деление на 0"));
    }
}