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
    void SampleTest1(){
        int[] inputArray1 = {-123, 0, -32,29,2,345,-999,-1000,1000};
        int[] expectedArray1 = {-1000,-999,-123,-32,0,2,29,345,1000};
        Main ob = new Main();
        ob.sort(inputArray1);
        assertArrayEquals(expectedArray1, inputArray1);
    }
}