package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




/**
 * This test class for testing iterator class.
 */

class HashTableIteratorTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>(); // Предположим, что таблица на 5 бакетов
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);
        hashTable.put("key3", 3);
    }

    @Test
    void testHasNextAndNext() {
        HashTableIterator<String, Integer> iterator = new HashTableIterator<>(hashTable);

        assertTrue(iterator.hasNext(), "Iterator should have elements initially");

        Entry<String, Integer> entry1 = iterator.next();
        assertEquals("key1", entry1.key);
        assertEquals(1, entry1.value);

        Entry<String, Integer> entry2 = iterator.next();
        assertEquals("key2", entry2.key);
        assertEquals(2, entry2.value);

        Entry<String, Integer> entry3 = iterator.next();
        assertEquals("key3", entry3.key);
        assertEquals(3, entry3.value);

        assertFalse(iterator.hasNext(), "Iterator should not have elements"
                + " after all entries are visited");
    }

    @Test
    void testNoSuchElementException() {
        HashTableIterator<String, Integer> iterator = new HashTableIterator<>(hashTable);

        // Перебираем все элементы
        iterator.next();
        iterator.next();
        iterator.next();

        // Проверка, что вызов next() после окончания элементов вызывает исключение
        assertThrows(NoSuchElementException.class, iterator::next, "Calling "
                + "next() with no elements left should throw NoSuchElementException");
    }

    @Test
    void testConcurrentModificationException() {
        HashTableIterator<String, Integer> iterator = new HashTableIterator<>(hashTable);

        hashTable.put("key4", 4); // Модифицируем таблицу после создания итератора

        assertThrows(ConcurrentModificationException.class, iterator::next, "Modifying "
                + "hashTable after iterator creation should throw "
                + "ConcurrentModificationException on next()");
    }

    @Test
    void testEmptyTable() {
        HashTable<String, Integer> emptyHashTable = new HashTable<>();
        HashTableIterator<String, Integer> iterator = new HashTableIterator<>(emptyHashTable);

        assertFalse(iterator.hasNext(), "Iterator on"
                + " empty hash table should not have elements");
        assertThrows(NoSuchElementException.class, iterator::next, "Calling"
                + " next() on empty hash table should throw NoSuchElementException");
    }
}