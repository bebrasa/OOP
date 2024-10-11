package ru.nsu.kochanov;

import java.util.Stack;

/**
 * Это класс парсера.
 */

public class ExpressionParser {

    // Метод для парсинга строки в Expression
    public static Expression parse(String input) throws MyException {
        // Убираем пробелы из выражения
        input = input.replaceAll("\\s+", "");

        Stack<Expression> expressions = new Stack<>();
        Stack<Character> operators = new Stack<>();

        // Проходим по каждому символу строки
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                // Если это число, строим Number
                int num = 0;
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    num = num * 10 + (input.charAt(i) - '0');
                    i++;
                }
                i--; // возвращаемся на одну позицию назад
                expressions.push(new Number(num));
            } else if (Character.isLetter(c)) {
                // Если это переменная
                StringBuilder varName = new StringBuilder();
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    varName.append(input.charAt(i));
                    i++;
                }
                i--; // возвращаемся на одну позицию назад
                expressions.push(new Variable(varName.toString()));
            } else if (c == '(') {
                // Если это открывающая скобка, просто добавляем оператор
                operators.push(c);
            } else if (c == ')') {
                // Если это закрывающая скобка, выполняем операции до открытия
                while (!operators.isEmpty() && operators.peek() != '(') {
                    expressions.push(applyOperator(operators.pop(), expressions.pop(), expressions.pop()));
                }
                operators.pop(); // Убираем '('
            } else if (isOperator(c)) {
                // Если это оператор, выполняем все операции с приоритетом выше или равным текущему оператору
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    expressions.push(applyOperator(operators.pop(), expressions.pop(), expressions.pop()));
                }
                operators.push(c);
            }
        }

        // Выполняем оставшиеся операции
        while (!operators.isEmpty()) {
            expressions.push(applyOperator(operators.pop(), expressions.pop(), expressions.pop()));
        }

        // Последнее выражение — результат
        return expressions.pop();
    }

    // Применение оператора к двум выражениям
    private static Expression applyOperator(char operator, Expression right, Expression left) throws MyException {
        switch (operator) {
            case '+':
                return new Add(left, right);
            case '-':
                return new Sub(left, right);
            case '*':
                return new Mul(left, right);
            case '/':
                return new Div(left, right);
            default:
                throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }

    // Проверка, является ли символ оператором
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Приоритет операторов
    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return -1;
    }
}