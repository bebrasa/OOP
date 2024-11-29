package nsu.kochanov;

import java.io.IOException;
import java.util.List;

/**
 * This is main class in my program.
 */
public class Main {

    /**
     * This is java.
     */
    public static void main(String[] args) {
        String fileName = "input.txt";
        String searchString = "ami";

        FileSearcher fileSearcher = new FileSearcher(fileName, searchString);
        try {
            List<Integer> occurrences = fileSearcher.findOccurrences();
            System.out.println("Occurrences: " + occurrences);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}