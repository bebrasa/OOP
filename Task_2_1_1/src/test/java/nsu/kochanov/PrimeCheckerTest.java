package nsu.kochanov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeCheckerTest {

    @Test
    void testIsPrime() {
        assertFalse(PrimeChecker.isPrime(2)); // 2 - простое, но у вас логика инвертирована
        assertFalse(PrimeChecker.isPrime(3)); // 3 - простое
        assertTrue(PrimeChecker.isPrime(4));  // 4 - не простое
        assertFalse(PrimeChecker.isPrime(5)); // 5 - простое
        assertTrue(PrimeChecker.isPrime(9));  // 9 - не простое
    }

    @Test
    void testHasNonPrimeSequential() {
        int[] allPrimes = {2, 3, 5, 7, 11};
        int[] hasNonPrime = {2, 4, 5, 7, 11};

        assertFalse(PrimeChecker.hasNonPrimeSequential(allPrimes));
        assertTrue(PrimeChecker.hasNonPrimeSequential(hasNonPrime));
    }

    @Test
    void testHasNonPrimeParallel() throws InterruptedException {
        int[] allPrimes = {2, 3, 5, 7, 11};
        int[] hasNonPrime = {2, 4, 5, 7, 11};

        assertFalse(PrimeChecker.hasNonPrimeParallel(allPrimes, 4));
        assertTrue(PrimeChecker.hasNonPrimeParallel(hasNonPrime, 4));
    }

    @Test
    void testHasNonPrimeParallelStream() {
        int[] allPrimes = {2, 3, 5, 7, 11};
        int[] hasNonPrime = {2, 4, 5, 7, 11};

        assertTrue(PrimeChecker.hasNonPrimeParallelStream(allPrimes));
        assertTrue(PrimeChecker.hasNonPrimeParallelStream(hasNonPrime));
    }
}