package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;


public class HeapSortTest {

    @Test
    void sampleTest() {
        // Исходный массив
        int[] inputArray = {5, 3, 8, 1, 2, 7};

        // Ожидаемый отсортированный массив
        int[] expectedArray = {1, 2, 3, 5, 7, 8};

        // Создаем объект Main
        HeapSort ob = new HeapSort();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray, inputArray);
    }

    @Test
    void sampleTest1() {
        int[] inputArray1 = {-123, 0, -32, 29, 2, 345, -999, -1000, 1000};
        int[] expectedArray1 = {-1000, -999, -123, -32, 0, 2, 29, 345, 1000};

        // Создаем объект Main
        HeapSort ob = new HeapSort();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray1);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray1, inputArray1);
    }

    @Test
    void sampleTest2() {
        int[] inputArray2 = {};
        int[] expectedArray2 = {};

        // Создаем объект Main
        HeapSort ob = new HeapSort();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray2);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray2, inputArray2);
    }

    @Test
    void sampleTest3() {
        int[] inputArray3 = {1};
        int[] expectedArray3 = {1};

        // Создаем объект Main
        HeapSort ob = new HeapSort();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray3);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray3, inputArray3);
    }

    @Test
    void sampleTest4() {
        int[] inputArray4 = {1, 2, 3, 4, 5};
        int[] expectedArray4 = {1, 2, 3, 4, 5};

        // Создаем объект Main
        HeapSort ob = new HeapSort();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray4);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray4, inputArray4);
    }
    double logn = 0;
    private int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(10000); // Генерируем случайное целое число
        }
        return array;
    }

    // Метод для проверки, что массив отсортирован
    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // Метод для проведения теста сортировки
    private void runSortTest(int arraySize, int repetitions) {
        HeapSort heapSort = new HeapSort();

        for (int i = 0; i < repetitions; i++) {
            // Генерируем новый случайный массив
            int[] array = generateRandomArray(arraySize);

            // Засекаем время до начала сортировки
            Instant start = Instant.now();

            // Сортируем массив
            heapSort.sort(array);

            // Засекаем время после сортировки
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);

            logn = ((arraySize * Math.log(arraySize)) / Math.log(2)) / Math.pow(10,5);
            // Выводим время сортировки и проверяем, что массив отсортирован
            System.out.println("Array size: " + arraySize + ", Time elapsed: " + timeElapsed.toMillis() + " ms " + logn + " NLn(N)");

            // Проверяем, что массив отсортирован
            assertTrue(isSorted(array));
        }
    }

    @Test
    public void testHeapSortPerformance() {
        // Пример замеров для разных размеров массивов и разного количества повторений
        int[] arraySizes = {10000, 50000, 100000, 500000, 1000000}; // Массивы разного размера
        int repetitions = 5; // Количество повторов для каждого размера

        for (int size : arraySizes) {
            runSortTest(size, repetitions); // Проводим тесты для каждого размера массива
        }
    }

}