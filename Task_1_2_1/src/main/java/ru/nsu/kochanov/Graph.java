package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.List;

/**
 * Этот интерфейс используется для определения всех методов,
 * которые будут реализованы во всех классах.
 */

public interface Graph {
    void addVertex(int v);

    void removeVertex(int v);

    void addEdge(int v1, int v2);

    void removeEdge(int v1, int v2);

    List<Integer> getNeighbors(int v);

    void readFromTerminal(InputStream inputStream);  // чтение с терминала

    boolean equals(Object obj);

    String toString();

    List<Integer> topologicalSort();
}
