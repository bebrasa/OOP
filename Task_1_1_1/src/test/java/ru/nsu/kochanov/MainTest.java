package ru.nsu.kochanov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void SampleTest(){
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
}