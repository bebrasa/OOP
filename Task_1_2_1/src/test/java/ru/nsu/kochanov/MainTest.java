package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test for main class.
 */
class MainTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    void testMainWithValidInput() {
        String input = "3 2\n0 1\n1 2\n";  // 3 вершины, 2 ребра
        provideInput(input);

        Main.main(new String[]{});

        String output = getOutput();

        // Проверяем, что выводится правильно
        assertTrue(output.contains("Матрица смежности:"));
        assertTrue(output.contains("Матрица инцидентности:"));
        assertTrue(output.contains("Список смежности графа:"));
        assertTrue(output.contains("Топологическая сортировка:"));

        // Проверяем наличие ожидаемых результатов топологической сортировки
        assertTrue(output.contains("[0, 1, 2]"), "Топологическая сортировка должна быть [0, 1, 2]");
    }

    @Test
    void testMainWithSingleVertex() {
        String input = "1 0\n";  // 1 вершина, 0 рёбер
        provideInput(input);

        Main.main(new String[]{});

        String output = getOutput();

        // Проверяем вывод
        assertTrue(output.contains("Матрица смежности:"));
        assertTrue(output.contains("Матрица инцидентности:"));
        assertTrue(output.contains("Список смежности графа:"));
        assertTrue(output.contains("Топологическая сортировка:"));

        // В графе с одной вершиной топологическая сортировка должна быть [0]
        assertTrue(output.contains("[0]"));
    }
}