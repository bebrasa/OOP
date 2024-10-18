package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.ArrayList;

public class AdjacencyMatrixGraph implements Graph {
    private Boolean[][] matrix;
    private int size = 0;

    @Override
    public void addVertex() {
        size++;
        Boolean[][] newMatrix = new Boolean[size][size];
        newMatrix[size - 1][size - 1] = false;
        for (int i = 0; i < size - 1; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, size - 1);
            newMatrix[i][size - 1] = false;
            newMatrix[size - 1][i] = false;
        }
        matrix = newMatrix;
        matrix[size - 1][size - 1] = true;
    }

    @Override
    public void removeVertex(int v) {
        Boolean[][] newMatrix = new Boolean[size - 1][size - 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < v) {
                    if (j < v) {
                        newMatrix[i][j] = matrix[i][j];
                    } else if (j > v) {
                        newMatrix[i][j - 1] = matrix[i][j];
                    }
                } else if (i > v) {
                    if (j < v) {
                        newMatrix[i - 1][j] = matrix[i][j];
                    } else if (j > v) {
                        newMatrix[i - 1][j - 1] = matrix[i][j];
                    }
                }
            }
        }
        size--;
        matrix = newMatrix;
    }

    @Override
    public void addEdge(Edge e) {
        matrix[e.getFrom()][e.getTo()] = true;
    }

    @Override
    public void removeEdge(Edge e) {
        matrix[e.getFrom()][e.getTo()] = false;
    }

    @Override
    public ArrayList<Integer> neighbors(int v) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (matrix[v][i]) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    @Override
    public void read(InputStream input, String regex) {
    }

    @Override
    public void topologicalSort() {
    }

    @Override
    public void print() {
    }

    @Override
    public void isEqual() {

    }
}