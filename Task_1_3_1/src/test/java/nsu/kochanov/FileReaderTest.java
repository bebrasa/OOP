package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * This test class for testing FileReader.
 */
class FileReaderTest {

    @Test
    void testReadNextChar() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Hello, World!");
        }

        FileReader fileReader = new FileReader(tempFile.getAbsolutePath());
        try {
            assertEquals('H', fileReader.readNextChar());
            assertEquals('e', fileReader.readNextChar());
            assertEquals('l', fileReader.readNextChar());
            assertEquals('l', fileReader.readNextChar());
            assertEquals('o', fileReader.readNextChar());
            assertEquals(',', fileReader.readNextChar());
            assertEquals(' ', fileReader.readNextChar());
            assertEquals('W', fileReader.readNextChar());
            assertEquals('o', fileReader.readNextChar());
            assertEquals('r', fileReader.readNextChar());
            assertEquals('l', fileReader.readNextChar());
            assertEquals('d', fileReader.readNextChar());
            assertEquals('!', fileReader.readNextChar());
            assertEquals(-1, fileReader.readNextChar());
        } finally {
            fileReader.close();
        }

        assertDoesNotThrow(fileReader::close);
    }
}