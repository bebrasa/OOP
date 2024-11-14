package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.*;

/**
 * Этот класс используется для хранения графа,
 * используя матрицу инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {
    private int[][] incidenceMatrix;
    private int numVertices;
    private int numEdges;
    private int edgeCount = 0;  // счётчик для отслеживания текущего числа рёбер

    /**
     * Конструктор, который принимает количество вершин и рёбер.
     *
     * @param numVertices количество вершин.
     * @param numEdges количество рёбер.
     */
    public IncidenceMatrixGraph(int numVertices, int numEdges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        incidenceMatrix = new int[numVertices][numEdges];
    }

    @Override
    public void addVertex(int v) {
        // Матрица создается заранее и фиксируется в конструкторе,
        // поэтому динамическое добавление вершин не поддерживается.
    }

    @Override
    public void removeVertex(int v) {
        if (v < 0 || v >= numVertices) {
            throw new IllegalArgumentException("Вершина вне диапазона");
        }
        for (int i = 0; i < numEdges; i++) {
            incidenceMatrix[v][i] = 0;  // убираем все связи, связанные с данной вершиной
        }
    }

    @Override
    public void addEdge(int v1, int v2) {
        if (edgeCount >= numEdges) {
            throw new IllegalStateException("Невозможно добавить больше рёбер");
        }
        if (v1 < 0 || v1 >= numVertices || v2 < 0 || v2 >= numVertices) {
            throw new IllegalArgumentException("Вершины вне диапазона");
        }
        incidenceMatrix[v1][edgeCount] = 1;   // начальная вершина
        incidenceMatrix[v2][edgeCount] = -1;  // конечная вершина
        edgeCount++;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        for (int i = 0; i < numEdges; i++) {
            if (incidenceMatrix[v1][i] == 1 && incidenceMatrix[v2][i] == -1) {
                incidenceMatrix[v1][i] = 0;
                incidenceMatrix[v2][i] = 0;
                break;
            }
        }
    }

    @Override
    public List<Integer> getNeighbors(int v) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < numEdges; i++) {
            if (incidenceMatrix[v][i] == 1) {
                for (int j = 0; j < numVertices; j++) {
                    if (incidenceMatrix[j][i] == -1) {
                        neighbors.add(j);
                    }
                }
            }
        }
        return neighbors;
    }

    @Override
    public void readFromTerminal(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        System.out.println("Введите количество вершин и рёбер:");
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        this.numVertices = n;
        this.numEdges = m;
        incidenceMatrix = new int[n][m];
        edgeCount = 0;  // сбрасываем счётчик рёбер

        System.out.println("Введите рёбра:");
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            addEdge(v1, v2);  // используем addEdge для учёта валидации
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IncidenceMatrixGraph that = (IncidenceMatrixGraph) o;
        return numVertices == that.numVertices && numEdges == that.numEdges
                && edgeCount == that.edgeCount
                && Objects.deepEquals(incidenceMatrix, that.incidenceMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(incidenceMatrix), numVertices, numEdges, edgeCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ");
            List<Integer> neighbors = getNeighbors(i);
            for (int neighbor : neighbors) {
                sb.append(neighbor).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Integer> topologicalSort() {
        System.out.println("Топологическая сортировка на матрице инцидентности:");
        int[] inDegree = new int[numVertices];
        for (int i = 0; i < numEdges; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (incidenceMatrix[j][i] == -1) {
                    inDegree[j]++;
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            sorted.add(vertex);

            List<Integer> neighbors = getNeighbors(vertex);
            for (int neighbor : neighbors) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sorted.size() != numVertices) {
            throw new IllegalStateException("Граф содержит цикл,"
                    + " топологическая сортировка невозможна");
        }

        return sorted;
    }

    /**
     * Метод для вывода матрицы инцидентности.
     */
    public void printIncidenceMatrix() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numEdges; j++) {
                System.out.print(incidenceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}