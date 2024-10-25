package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test for IncMatrix class.
 */
class IncidenceMatrixGraphTest {

    private IncidenceMatrixGraph graph;

    @BeforeEach
    void setUp() {
        graph = new IncidenceMatrixGraph(3, 3);
    }

    @Test
    void testAddEdge() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        List<Integer> neighbors0 = graph.getNeighbors(0);
        List<Integer> neighbors1 = graph.getNeighbors(1);
        List<Integer> neighbors2 = graph.getNeighbors(2);

        assertEquals(List.of(1), neighbors0, "Вершина 0 должна иметь соседа 1");
        assertEquals(List.of(2), neighbors1, "Вершина 1 должна иметь соседа 2");
        assertTrue(neighbors2.isEmpty(), "Вершина 2 не должна иметь соседей");
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.removeEdge(0, 1);

        List<Integer> neighbors0 = graph.getNeighbors(0);
        List<Integer> neighbors1 = graph.getNeighbors(1);

        assertTrue(neighbors0.isEmpty(), "Вершина 0 не должна иметь соседей после удаления ребра");
        assertEquals(List.of(2), neighbors1, "Вершина 1 должна иметь соседа 2");
    }

    @Test
    void testRemoveVertex() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.removeVertex(1);

        List<Integer> neighbors0 = graph.getNeighbors(0);
        List<Integer> neighbors1 = graph.getNeighbors(1);

        assertTrue(neighbors0.isEmpty(), "Все связи вершины 1 должны быть удалены");
        assertTrue(neighbors1.isEmpty(), "Все связи удалённой вершины 1 должны быть убраны");
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        List<Integer> neighbors0 = graph.getNeighbors(0);
        List<Integer> neighbors1 = graph.getNeighbors(1);
        List<Integer> neighbors2 = graph.getNeighbors(2);

        assertEquals(List.of(1), neighbors0, "Вершина 0 должна иметь соседа 1");
        assertEquals(List.of(2), neighbors1, "Вершина 1 должна иметь соседа 2");
        assertTrue(neighbors2.isEmpty(), "Вершина 2 не должна иметь соседей");
    }

    @Test
    void testTopologicalSort() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(List.of(0, 1, 2), sorted, "Топологическая сортировка должна"
                 + " вернуть правильный порядок вершин");
    }

    @Test
    void testTopologicalSortWithCycle() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);  // создаём цикл

        Exception exception = assertThrows(IllegalStateException.class,
                () -> graph.topologicalSort());
        assertEquals("Граф содержит цикл, топологическая сортировка невозможна", exception.getMessage(),
                "Топологическая сортировка должна выбросить исключение при наличии цикла");
    }

    @Test
    void testEquals() {
        IncidenceMatrixGraph graph1 = new IncidenceMatrixGraph(3, 3);
        IncidenceMatrixGraph graph2 = new IncidenceMatrixGraph(3, 3);

        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);

        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);

        assertEquals(graph1, graph2, "Графы должны быть равны");
    }

    @Test
    void testToString() {
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        String expected = "0: 1 \n1: 2 \n2: \n";
        assertEquals(expected, graph.toString(), "Метод toString() должен возвращать "
               + "правильное представление графа");
    }
}