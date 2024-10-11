package ru.nsu.kochanov;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Это мейн класс.
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение выражения с консоли
        System.out.println("Введите выражение:");
        String input = scanner.nextLine();

        try {
            // Парсинг выражения
            Expression e = ExpressionParser.parse(input);

            // Печать выражения
            System.out.println("Выражение: " + e.print());

            // Чтение переменных и их значений
            Map<String, Integer> variables = new HashMap<>();
            System.out.println("Введите значения переменных (например, x=10;y=5):");
            String[] varInput = scanner.nextLine().split(";");
            for (String var : varInput) {
                String[] parts = var.split("=");
                variables.put(parts[0], Integer.parseInt(parts[1]));
            }

            // Вычисление значения выражения
            // Выбор переменной для производной
            System.out.println("Введите переменную для дифференцирования:");
            String derivativeVariable = scanner.nextLine();

            // Вычисление производной
            Expression derivative = e.derivative(derivativeVariable);
            System.out.println("Производная: " + derivative.print());

            int result = e.eval(variables);
            System.out.println("Результат: " + result);

        } catch (MyException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
        }
    }
}