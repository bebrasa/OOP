package nsu.kochanov;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * This is java.
 */
public class FileReader {
    private final BufferedReader reader;

    public FileReader(String fileName) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
    }

    /**
     * This is java.
     */
    public int readNextChar() throws IOException {
        return reader.read();
    }

    /**
     * This is java.
     */
    public void close() throws IOException {
        reader.close();
    }
}
