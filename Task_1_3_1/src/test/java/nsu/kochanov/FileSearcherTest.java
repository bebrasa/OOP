package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * This class is testing fileSearcherTest.
 */
class FileSearcherTest {

    @Test
    void testFindOccurrences() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        String fileContent = "абракадабра";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(fileContent);
        }

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "бра");
        List<Long> occurrences = searcher.findOccurrences();

        assertEquals(List.of(1L, 8L), occurrences);
    }

    @Test
    void testFindOccurrencesNoMatch() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        String fileContent = "абракадабра";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(fileContent);
        }

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "тест");
        List<Long> occurrences = searcher.findOccurrences();

        assertTrue(occurrences.isEmpty());
    }

    @Test
    void testFindOccurrencesOverlap() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        String fileContent = "aaaaaa";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(fileContent);
        }

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "aaa");
        List<Long> occurrences = searcher.findOccurrences();

        assertEquals(List.of(0L, 1L, 2L, 3L), occurrences);
    }

    @Test
    void testFindOccurrencesEmptyFile() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "бра");
        List<Long> occurrences = searcher.findOccurrences();

        assertTrue(occurrences.isEmpty());
    }

    @Test
    void testFindOccurrencesLargeFile() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            contentBuilder.append("абракадабра");
        }
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(contentBuilder.toString());
        }

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "даб");
        List<Long> occurrences = searcher.findOccurrences();

        assertEquals(10000L, occurrences.size());
    }

    @Test
    void testFindOccurrencesInLargeFile() throws IOException {
        File tempFile = File.createTempFile("largeTestFile", ".txt");
        tempFile.deleteOnExit();

        String pattern = "😃😃😃😃😃😃😃😃😃😃😇";
        int patternLength = pattern.length();

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            for (long i = 0; i < 5_000_0000; i++) {
                outputStream.write(pattern.getBytes());
                outputStream.write(' ');
            }
        }

        FileSearcher fileSearcher = new FileSearcher(tempFile.getAbsolutePath(), pattern);
        List<Long> occurrences = fileSearcher.findOccurrences();

        assertEquals(5_000_000, occurrences.size());

        for (long i = 0; i < 5_000_000; i++) {
            assertEquals(i * (patternLength + 1), occurrences.get((int) i).longValue());
        }
    }

}