package ru.nsu.kochanov;

/**
 * Task_1_1_1 HeapSort.
 */
public class HeapSort {
    /**
     * Sorting array elements in ascending order. First, the unordered sequence becomes a heap.
     * Next, sorting is performed. The first (maximum) element is exchanged with the last element,
     * so the finished.
     * sequence is formed at the tail of the array.
     *
     * @param arr The array of integers to sort.
     */
    // Метод сортировки
    static void sort(int[] arr) {
        int n = arr.length;

        // Построение кучи (Heapify)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

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

    /**
     * The largest element of parent(arr[i]) or children(arr[2*i+1], arr[2*i+2]),
     * appears at the root of the current subtree.
     *
     * @param arr The array of integers.
     * @param n   The length of array.
     * @param i   the index of element to be heapify.
     */
    // Метод для приведения поддерева с корнем в узле i к куче
    static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Если левый дочерний элемент больше корня
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // Если правый дочерний элемент больше, чем наибольший элемент на данный момент
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // Если наибольший элемент не корень
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Рекурсивно heapify поддерево
            heapify(arr, n, largest);
        }
    }

    /**
     * Main method - entry point for the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Пример массива для сортировки
        int[] arr = {12, 11, 13, 5, 6, 7};
        sort(arr);
    }
}