package ru.nsu.kochanov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test for ajMatrix class.
 */
class AdjacencyMatrixGraphTest {

    private AdjacencyMatrixGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrixGraph(5);  // Создаём граф с 5 вершинами
    }

    @Test
    void testToString() {
        // Ожидаемое строковое представление для графа без рёбер (все элементы матрицы равны 0)
        String expectedEmptyGraph = "0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n";
        assertEquals(expectedEmptyGraph, graph.toString(), "Строковое представление пустого графа некорректно");

        // Добавляем рёбра
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        // Ожидаемое строковое представление после добавления рёбер
        String expectedGraphWithEdges = "0 1 0 0 0 \n0 0 1 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n";
        assertEquals(expectedGraphWithEdges, graph.toString(), "Строковое представление графа с рёбрами некорректно");
    }

    @Test
    void addVertex() {
        graph.addVertex(0);
        assertTrue(graph.getNeighbors(0).isEmpty(), "Вершина должна быть добавлена без соседей");
    }

    @Test
    void removeVertex() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.removeVertex(1);

        assertTrue(graph.getNeighbors(1).isEmpty(), "Вершина должна быть удалена");
        assertTrue(graph.getNeighbors(0).isEmpty(), "Сосед для вершины 0 должен быть удалён");
    }

    @Test
    void addEdge() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        List<Integer> neighbors = graph.getNeighbors(0);
        assertEquals(1, neighbors.size(), "Должен быть один сосед");
        assertEquals(1, neighbors.get(0), "Соседом вершины 0 должна быть вершина 1");
    }

    @Test
    void removeEdge() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);

        List<Integer> neighbors = graph.getNeighbors(0);
        assertTrue(neighbors.isEmpty(), "Ребро должно быть удалено");
    }

    @Test
    void getNeighbors() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);

        List<Integer> neighbors = graph.getNeighbors(0);
        assertEquals(2, neighbors.size(), "Вершина 0 должна иметь 2 соседа");
        assertTrue(neighbors.contains(1), "Соседом вершины 0 должна быть вершина 1");
        assertTrue(neighbors.contains(2), "Соседом вершины 0 должна быть вершина 2");
    }

    @Test
    void readFromTerminal() {
        // Для этого теста необходимо будет подать данные через ввод в терминал,
        // либо замокать ввод Scanner'а. Этот тест можно оставить без реализации или
        // протестировать вручную.
    }

    @Test
    void testEquals() {
        AdjacencyMatrixGraph graph2 = new AdjacencyMatrixGraph(5);
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph2.addVertex(0);
        graph2.addVertex(1);
        graph2.addEdge(0, 1);

        assertEquals(graph, graph2, "Графы должны быть равны, так как они идентичны");
    }

    @Test
    void topologicalSort() {
        AdjacencyMatrixGraph graph3 = new AdjacencyMatrixGraph(3);
        graph3.addVertex(0);
        graph3.addVertex(1);
        graph3.addVertex(2);
        graph3.addEdge(0, 1);
        graph3.addEdge(1, 2);

        List<Integer> sorted = graph3.topologicalSort();
        assertEquals(List.of(0, 1, 2), sorted, "Топологическая сортировка должна вернуть правильный порядок вершин");
    }

    @Test
    void printAdjacencyMatrix() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        String expectedOutput = "0 1 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n0 0 0 0 0 \n";
        // Перехват вывода в консоль
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        graph.printAdjacencyMatrix();
        assertEquals(expectedOutput, outContent.toString(), "Матрица смежности должна быть правильно выведена");

        // Возвращаем стандартный вывод
        System.setOut(System.out);
    }
}