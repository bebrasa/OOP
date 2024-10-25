package ru.nsu.kochanov;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Этот класс используется для хранения графа,
 * используя список смежности.
 */

public class AdjacencyListGraph implements Graph {
    private final Map<Integer, List<Integer>> adjList = new HashMap<>();

    @Override
    public void addVertex(int v) {
        adjList.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void removeVertex(int v) {
        adjList.remove(v);
        for (List<Integer> neighbours : adjList.values())
        {
            neighbours.remove(Integer.valueOf(v));
        }
    }

    @Override
    public void addEdge(int v1, int v2) {
        adjList.get(v1).add(v2);
    }

    @Override
    public void removeEdge(int v1, int v2) {
        adjList.get(v1).remove((Integer) v2);
    }

    @Override
    public List<Integer> getNeighbors(int v) {
        return adjList.getOrDefault(v, new ArrayList<>());
    }

    @Override
    public void readFromTerminal(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        System.out.println("Введите количество вершин и рёбер:");
        int n = scanner.nextInt();  // количество вершин
        int m = scanner.nextInt();  // количество рёбер

        for (int i = 0; i < n; i++) {
            addVertex(i);
        }

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
        if (!(obj instanceof AdjacencyListGraph)) {
            return false;
        }
        AdjacencyListGraph other = (AdjacencyListGraph) obj;
        return adjList.equals(other.adjList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int vertex : adjList.keySet()) {
            sb.append(vertex).append(": ").append(adjList.get(vertex)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Integer> topologicalSort() {
        System.out.println("топ сорт на списке смежности:");
        Map<Integer, Boolean> visited = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int vertex : adjList.keySet()) {
            if (!visited.getOrDefault(vertex, false)) {
                topologicalSortUtil(vertex, visited, stack);
            }
        }
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void topologicalSortUtil(int v, Map<Integer, Boolean> visited, Stack<Integer> stack) {
        visited.put(v, true);
        for (int neighbor : adjList.getOrDefault(v, new ArrayList<>())) {
            if (!visited.getOrDefault(neighbor, false)) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }
        stack.push(v);
    }
}