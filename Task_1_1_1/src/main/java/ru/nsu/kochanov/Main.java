package ru.nsu.kochanov;

import java.util.Scanner;

public class Main {

    // Метод сортировки
    public void sort(int arr[]) {
        int n = arr.length;

        // Построение кучи (Heapify)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // Извлечение элементов из кучи
        for (int i = n - 1; i >= 0; i--) {
            // Переместить текущий корень в конец
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Вызвать процедуру heapify на уменьшенной куче
            heapify(arr, i, 0);
        }
    }

    // Метод для приведения поддерева с корнем в узле i к куче
    void heapify(int arr[], int n, int i) {
        int largest = i; // Инициализируем наибольший элемент как корень
        int l = 2 * i + 1; // левый = 2*i + 1
        int r = 2 * i + 2; // правый = 2*i + 2

        // Если левый дочерний элемент больше корня
        if (l < n && arr[l] > arr[largest])
            largest = l;

        // Если правый дочерний элемент больше, чем наибольший элемент на данный момент
        if (r < n && arr[r] > arr[largest])
            largest = r;

        // Если наибольший элемент не корень
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Рекурсивно heapify поддерево
            heapify(arr, n, largest);
        }
    }

    // Метод для вывода массива
    static void printArray(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    // Основная программа
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        // Ввод размера массива
        int n = scanner.nextInt();
        int arr[] = new int[n];

        // Ввод элементов массива
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Создание объекта класса Main и вызов сортировки
        Main ob = new Main();
        ob.sort(arr);

        // Вывод отсортированного массива
        printArray(arr);

        scanner.close(); // Закрытие сканера
    }
}