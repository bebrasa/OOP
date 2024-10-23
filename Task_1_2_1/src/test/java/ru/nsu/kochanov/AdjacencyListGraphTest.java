package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
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