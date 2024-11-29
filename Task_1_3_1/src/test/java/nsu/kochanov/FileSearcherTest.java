package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
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
        List<Integer> occurrences = searcher.findOccurrences();

        assertEquals(List.of(1, 8), occurrences);
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
        List<Integer> occurrences = searcher.findOccurrences();

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
        List<Integer> occurrences = searcher.findOccurrences();

        assertEquals(List.of(0, 1, 2, 3), occurrences);
    }

    @Test
    void testFindOccurrencesEmptyFile() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        FileSearcher searcher = new FileSearcher(tempFile.getAbsolutePath(), "бра");
        List<Integer> occurrences = searcher.findOccurrences();

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
        List<Integer> occurrences = searcher.findOccurrences();

        assertEquals(10000, occurrences.size());
    }
}