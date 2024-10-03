package ru.nsu.kochanov;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Пример выражения: 3 + (2 * x)
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        // Печать выражения
        System.out.println("Выражение: " + e.print()); // Ожидается: (3+(2*x))

        // Производная выражения по x
        Expression derivative = e.derivative("x");
        System.out.println("Производная: " + derivative.print()); // Ожидается: (0+((0*x)+(2*1)))

        // Вычисление значения при x = 10
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        int result = e.eval(variables);
        System.out.println("Результат при x=10: " + result); // Ожидается: 23
    }
}