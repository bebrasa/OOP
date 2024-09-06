package ru.nsu.kochanov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void sampleTest() {
        // Исходный массив
        int[] inputArray = {5, 3, 8, 1, 2, 7};

        // Ожидаемый отсортированный массив
        int[] expectedArray = {1, 2, 3, 5, 7, 8};

        // Создаем объект Main
        Main ob = new Main();

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
        Main ob = new Main();

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
        Main ob = new Main();

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
        Main ob = new Main();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray3);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray3, inputArray3);
    }

    @Test
    void sampleTest4() {
        int[] inputArray4 = {1,2,3,4,5};
        int[] expectedArray4 = {1,2,3,4,5};

        // Создаем объект Main
        Main ob = new Main();

        // Вызываем метод sort (он изменяет массив на месте)
        ob.sort(inputArray4);

        // Проверяем, что массив отсортирован правильно
        assertArrayEquals(expectedArray4, inputArray4);
    }
}