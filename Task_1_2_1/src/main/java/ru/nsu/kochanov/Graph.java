package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Интерфейс графа.
 */
public interface Graph {

    public void addVertex();

    public void removeVertex(int v);


    public void addEdge(Edge e);

    public void removeEdge(Edge e);

    public ArrayList<Integer> neighbors(int v);

    public void read(InputStream input, String regex);

    public void topologicalSort();

    /**
     * Вывести граф.
     */
    public void print();

    public void isEqual();
}
