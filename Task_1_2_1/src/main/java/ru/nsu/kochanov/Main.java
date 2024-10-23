package ru.nsu.kochanov;

import java.util.List;
import java.util.Scanner;

/**
 * Это мейн класс, используется для инициализации графов,
 * так же для их заполнения и вызыва методов.
 */
public class Main {
    /**
     * Test javadoc, because IDK what to write.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество вершин и рёбер:");
        int n = scanner.nextInt();  // количество вершин
        int m = scanner.nextInt();  // количество рёбер

        System.out.println("Введите рёбра:");
        int[][] edges = new int[m][2];  // хранение рёбер
        for (int i = 0; i < m; i++) {
            edges[i][0] = scanner.nextInt();  // начальная вершина ребра
            edges[i][1] = scanner.nextInt();  // конечная вершина ребра
        }

        // Инициализируем графы на основе трех разных способов хранения
        AdjacencyMatrixGraph adjacencyMatrixGraph = new AdjacencyMatrixGraph(n);
        IncidenceMatrixGraph incidenceMatrixGraph = new IncidenceMatrixGraph(n, m);
        Graph adjacencyListGraph = new AdjacencyListGraph();

        // Добавляем вершины
        for (int i = 0; i < n; i++) {
            adjacencyMatrixGraph.addVertex(i);
            incidenceMatrixGraph.addVertex(i);
            adjacencyListGraph.addVertex(i);
        }

        // Добавляем рёбра для всех графов
        for (int i = 0; i < m; i++) {
            int v1 = edges[i][0];
            int v2 = edges[i][1];
            adjacencyMatrixGraph.addEdge(v1, v2);
            incidenceMatrixGraph.addEdge(v1, v2);
            adjacencyListGraph.addEdge(v1, v2);
        }

        // Выводим матрицу смежности
        System.out.println("Матрица смежности:");
        adjacencyMatrixGraph.printAdjacencyMatrix();

        // Выводим матрицу инцидентности
        System.out.println("Матрица инцидентности:");
        incidenceMatrixGraph.printIncidenceMatrix();

        // Выводим список смежности
        System.out.println("Список смежности графа:");
        System.out.println(adjacencyListGraph);

        // Выводим топологическую сортировку для списка смежности
        System.out.println("Топологическая сортировка:");
        try {
            List<Integer> sorted = adjacencyListGraph.topologicalSort();
            System.out.println(sorted);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}