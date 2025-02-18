package nsu.kochanov;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This is javadoc for prime checker.
 */

public class PrimeChecker {

    /**
     * This func checks number is prime or not.
     */
    public static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        if (num == 2) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Slow checking.
     */
    public static boolean hasNonPrimeSequential(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    private static volatile boolean foundNonPrime = false;

    /**
     * Check with threads.
     */

    public static boolean hasNonPrimeParallel(int[] numbers, int numThreads) {
        foundNonPrime = false;
        int chunkSize = (int) Math.ceil((double) numbers.length / numThreads);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            threads.add(new Thread(() -> {
                int start = threadIndex * chunkSize;
                int end = Math.min(start + chunkSize, numbers.length);
                for (int j = start; j < end; j++) {
                    if (foundNonPrime) {
                        return;
                    }
                    if (isPrime(numbers[j])) {
                        foundNonPrime = true;
                        return;
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            try {
                thread.start();
            } catch (OutOfMemoryError e) {
                System.err.println("out of memory");
            }
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return foundNonPrime;
    }

    /**
     * Very fast checking.
     */
    public static boolean hasNonPrimeParallelStream(int[] numbers) {
        return IntStream.of(numbers).parallel().anyMatch(num -> !PrimeChecker.isPrime(num));
    }
}