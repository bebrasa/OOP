package nsu.kochanov;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "input.txt";
        String searchString = "бра";

        FileSearcher fileSearcher = new FileSearcher(fileName, searchString);
        try {
            List<Integer> occurrences = fileSearcher.findOccurrences();
            System.out.println("Occurrences: " + occurrences);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}