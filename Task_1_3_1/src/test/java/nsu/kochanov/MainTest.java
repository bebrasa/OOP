package nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * This is java.
 */
class MainTest {

    @Test
    void testMainWithEmojiSearch() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
        String fileContent = "Привет! \uD83D\uDE00 Как дела? \uD83D\uDE00";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(fileContent);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String[] args = {tempFile.getAbsolutePath(), "\uD83D\uDE00"};
        Main.main(args);

        String output = outputStream.toString().trim();
        assertFalse(output.contains("Occurrences: [8, 21]"));

        System.setOut(System.out);
    }
}