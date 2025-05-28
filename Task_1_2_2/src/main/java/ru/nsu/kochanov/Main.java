package ru.nsu.kochanov;

/**
 * This javadoc for main class in my program.
 */

public class Main {
    /**
     * javadoc.
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one")); // Ожидаемый вывод: 1.0

        hashTable.put("two", 2);
        hashTable.put("three", 3);

        System.out.println(hashTable); // Ожидаемый вывод: {one=1.0, two=2, three=3}

        hashTable.remove("two");
        System.out.println(hashTable); // Ожидаемый вывод: {one=1.0, three=3}

        System.out.println(hashTable.containsKey("one")); // Ожидаемый вывод: true
        System.out.println(hashTable.containsKey("two")); // Ожидаемый вывод: false
    }
}