package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test for adjList class.
 */
class AdjacencyListGraphTest {

    private AdjacencyListGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph();
    }

    @Test
    public void testEquals() {
        // Создаем первый граф и добавляем в него рёбра
        AdjacencyListGraph graph4 = new AdjacencyListGraph();
        graph4.addVertex(1);
        graph4.addVertex(2);
        graph4.addVertex(3);
        graph4.addVertex(4);
        graph4.addEdge(1, 2);
        graph4.addEdge(2, 3);
        graph4.addEdge(3, 4);

        // Создаем второй граф с теми же вершинами и рёбрами
        AdjacencyListGraph graph5 = new AdjacencyListGraph();
        graph5.addVertex(1);
        graph5.addVertex(2);
        graph5.addVertex(3);
        graph5.addVertex(4);
        graph5.addEdge(1, 2);
        graph5.addEdge(2, 3);
        graph5.addEdge(3, 4);

        // Проверяем, что графы равны
        assertEquals(graph4, graph5);

        // Создаем третий граф с отличными рёбрами
        AdjacencyListGraph graph6 = new AdjacencyListGraph();
        graph6.addVertex(1);
        graph6.addVertex(2);
        graph6.addVertex(3);
        graph6.addVertex(4);
        graph6.addEdge(1, 2);
        graph6.addEdge(3, 4); // Нет ребра (2, 3) как в graph1 и graph2

        // Проверяем, что graph1 и graph3 не равны
        assertNotEquals(graph4, graph6);
    }

    @Test
    void testAddVertex() {
        graph.addVertex(0);
        assertTrue(graph.getNeighbors(0).isEmpty(), "Вершина должна быть добавлена без соседей");
    }

    @Test
    void testAddEdge() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        List<Integer> neighbors = graph.getNeighbors(0);
        assertEquals(1, neighbors.size(), "Должен быть один сосед");
        assertEquals(1, neighbors.get(0), "Соседом вершины 0 должна быть вершина 1");
    }

    @Test
    void testRemoveEdge() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);

        List<Integer> neighbors = graph.getNeighbors(0);
        assertTrue(neighbors.isEmpty(), "Ребро должно быть удалено");
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.removeVertex(1);
        assertTrue(graph.getNeighbors(0).isEmpty(), "Сосед для вершины 0 должен быть удалён");
    }

    @Test
    void testTopologicalSort() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(List.of(0, 1, 2), sorted, "Топологическая сортировка "
                + "должна вернуть правильный порядок вершин");
    }
}