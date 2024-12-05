package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Этот класс используется для хранения графа,
 * используя матрицу смежности.
 */

public class AdjacencyMatrixGraph implements Graph {
    private int[][] matrix;
    private int numVertices;

    public AdjacencyMatrixGraph(int numVertices) {
        this.numVertices = numVertices;
        matrix = new int[numVertices][numVertices];
    }

    @Override
    public void addVertex(int v) {
        if (v >= matrix.length) {
            int[][] newMatrix = new int[v + 1][v + 1];
            for (int i = 0; i < matrix.length; i++) {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
            }
            matrix = newMatrix;
            numVertices = v + 1;
        }
    }

    @Override
    public void removeVertex(int v) {
        for (int i = 0; i < numVertices; i++) {
            matrix[v][i] = 0;
            matrix[i][v] = 0;
        }
    }

    @Override
    public void addEdge(int v1, int v2) {
        matrix[v1][v2] = 1;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        matrix[v1][v2] = 0;
    }

    @Override
    public List<Integer> getNeighbors(int v) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (matrix[v][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    @Override
    public void readFromTerminal(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        System.out.println("Введите количество вершин и рёбер:");
        int n = scanner.nextInt();  // количество вершин
        this.numVertices = n;
        matrix = new int[n][n];
        int m = scanner.nextInt();  // количество рёбер

        System.out.println("Введите рёбра:");
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            addEdge(v1, v2);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdjacencyMatrixGraph)) {
            return false;
        }
        AdjacencyMatrixGraph other = (AdjacencyMatrixGraph) obj;
        return Arrays.deepEquals(matrix, other.matrix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Integer> topologicalSort() {
        System.out.println("топ сорт на матрице смежности:");
        boolean[] visited = new boolean[numVertices];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * This method is using for topological sort.
     */
    private void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        for (int i = 0; i < numVertices; i++) {
            if (matrix[v][i] == 1 && !visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }
        stack.push(v);
    }

    /**
     * This method is using for print our Matrix.
     */
    public void printAdjacencyMatrix() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
