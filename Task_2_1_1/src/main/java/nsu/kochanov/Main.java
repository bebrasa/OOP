package nsu.kochanov;

import java.util.Arrays;

/**
 * This is another javadoc.
 */

public class Main {
    /**
     * This is javadoc.
     */
    public static void main(String[] args) {
        int[] arr1 = {6, 8, 7, 13, 5, 9, 4};  // true
        int[] arr2 = {20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053};  // false
        int[] bigArray = new int[100000];
        Arrays.fill(bigArray, 1000003);
        bigArray[99999] = 6;
        long start, end;

        // Последовательное выполнение
        start = System.nanoTime();
        boolean result1 = PrimeChecker.hasNonPrimeSequential(bigArray);
        end = System.nanoTime();
        System.out.println("Последовательный (bR): " + result1 + ", Время: "
                + (end - start) + " нс");

        // Параллельное выполнение с Thread
        start = System.nanoTime();
        boolean result2 = PrimeChecker.hasNonPrimeParallel(bigArray, 6);
        end = System.nanoTime();
        System.out.println("Параллельный (Thread, 6 потока, bR): " + result2 + ", Время: "
                + (end - start) + " нс");

        // Параллельное выполнение с parallelStream
        start = System.nanoTime();
        boolean result3 = PrimeChecker.hasNonPrimeParallelStream(bigArray);
        end = System.nanoTime();
        System.out.println("ParallelStream (bR): " + result3 + ", Время: "
                + (end - start) + " нс");
    }
}