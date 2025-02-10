package nsu.kochanov;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int[] arr1 = {6, 8, 7, 13, 5, 9, 4};  // Должно вернуть true
        int[] arr2 = {20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053};  // Должно вернуть false
        int[] bigArray = IntStream.range(1000004, 500000000).toArray();
        long start, end;

        // Последовательное выполнение
        start = System.nanoTime();
        boolean result1 = PrimeChecker.hasNonPrimeSequential(bigArray);
        end = System.nanoTime();
        System.out.println("Последовательный (arr1): " + result1 + ", Время: " + (end - start) + " нс");

        // Параллельное выполнение с Thread
        start = System.nanoTime();
        boolean result2 = PrimeChecker.hasNonPrimeParallel(bigArray, 4);
        end = System.nanoTime();
        System.out.println("Параллельный (Thread, 4 потока, arr1): " + result2 + ", Время: " + (end - start) + " нс");

        // Параллельное выполнение с parallelStream
        start = System.nanoTime();
        boolean result3 = PrimeChecker.hasNonPrimeParallelStream(bigArray);
        end = System.nanoTime();
        System.out.println("ParallelStream (arr1): " + result3 + ", Время: " + (end - start) + " нс");
    }
}