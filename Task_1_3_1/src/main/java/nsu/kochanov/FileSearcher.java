package nsu.kochanov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is java.
 */
public class FileSearcher {
    private final FileReader fileReader;
    private final String searchString;

    public FileSearcher(String fileName, String searchString) {
        try {
            this.fileReader = new FileReader(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error opening file: " + fileName, e);
        }
        this.searchString = searchString;
    }

    /**
     * This is java.
     */
    public List<Integer> findOccurrences() throws IOException {
        List<Integer> occurrences = new ArrayList<>();
        int bufferSize = searchString.length();
        StringBuilder buffer = new StringBuilder();

        int index = 0;
        int readChar;

        // Инициализируем буфер
        for (int i = 0; i < bufferSize; i++) {
            readChar = fileReader.readNextChar();
            if (readChar == -1) break;
            buffer.append((char) readChar);
        }

        while (buffer.length() == bufferSize) {
            if (buffer.toString().equals(searchString)) {
                occurrences.add(index);
            }

            index++;
            buffer.deleteCharAt(0);

            readChar = fileReader.readNextChar();
            if (readChar != -1) {
                buffer.append((char) readChar);
            } else {
                break;
            }
        }

        fileReader.close();
        return occurrences;
    }
}
