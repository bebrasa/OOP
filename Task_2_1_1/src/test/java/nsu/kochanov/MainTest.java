package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testIsPrime() {
        assertTrue(PrimeChecker.isPrime(3));
        assertFalse(PrimeChecker.isPrime(4));
        assertTrue(PrimeChecker.isPrime(5));
        assertFalse(PrimeChecker.isPrime(9));
        assertTrue(PrimeChecker.isPrime(13));
        assertFalse(PrimeChecker.isPrime(100));
    }

    @Test
    void testHasNonPrimeSequential() {
        int[] arr1 = {6, 8, 7, 13, 5, 9, 4}; // true
        int[] arr2 = {20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053}; // false

        assertTrue(PrimeChecker.hasNonPrimeSequential(arr1));
        assertTrue(PrimeChecker.hasNonPrimeSequential(arr2));
    }

    @Test
    void testHasNonPrimeParallel() throws InterruptedException {
        int[] arr1 = {6, 8, 7, 13, 5, 9, 4}; // true
        int[] arr2 = {20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053}; // false

        assertTrue(PrimeChecker.hasNonPrimeParallel(arr1, 4));
        assertTrue(PrimeChecker.hasNonPrimeParallel(arr2, 4));
    }

    @Test
    void testHasNonPrimeParallelStream() {
        int[] arr1 = {6, 8, 7, 13, 5, 9, 4}; // true
        int[] arr2 = {20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053}; // false

        assertTrue(PrimeChecker.hasNonPrimeParallelStream(arr1));
        assertFalse(PrimeChecker.hasNonPrimeParallelStream(arr2));
    }

    @Test
    void testLargeArray() throws InterruptedException {
        int[] bigArray = new int[100000];
        Arrays.fill(bigArray, 1000003); // Заполняем массив простым числом
        bigArray[99999] = 6; // Последний элемент - составное число

        assertTrue(PrimeChecker.hasNonPrimeSequential(bigArray));
        assertTrue(PrimeChecker.hasNonPrimeParallel(bigArray, 6));
        assertTrue(PrimeChecker.hasNonPrimeParallelStream(bigArray));
    }
}