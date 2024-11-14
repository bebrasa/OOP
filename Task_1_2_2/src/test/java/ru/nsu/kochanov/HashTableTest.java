package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * This test class for testing hashtable class.
 */

class HashTableTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    void testPutAndGet() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        assertEquals(1, hashTable.get("key1"));
        assertEquals(2, hashTable.get("key2"));
        assertNull(hashTable.get("nonExistingKey"), "Getting a non-existing key should return null");
    }

    @Test
    void testUpdate() {
        hashTable.put("key1", 1);
        hashTable.put("key1", 10);

        assertEquals(10, hashTable.get("key1"), "Updating an existing key should overwrite the value");
    }

    @Test
    void testContainsKey() {
        hashTable.put("key1", 1);

        assertTrue(hashTable.containsKey("key1"));
        assertFalse(hashTable.containsKey("nonExistingKey"), "containsKey should return false for non-existing key");
    }

    @Test
    void testRemove() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        assertEquals(1, hashTable.remove("key1"), "remove should return the value of the removed key");
        assertNull(hashTable.get("key1"), "Removed key should no longer be accessible");
        assertEquals(1, hashTable.size(), "Size should be updated after remove operation");
    }

    @Test
    void testSize() {
        assertEquals(0, hashTable.size(), "Size of a new hash table should be 0");

        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        assertEquals(2, hashTable.size(), "Size should reflect the number of entries in the table");

        hashTable.remove("key1");
        assertEquals(1, hashTable.size(), "Size should be updated after removing an element");
    }

    @Test
    void testRehash() {
        for (int i = 1; i <= 13; i++) {
            hashTable.put("key" + i, i);
        }

        assertEquals(13, hashTable.size(), "Size should match the number of added elements after rehashing");
        assertEquals(16 * 2, hashTable.table.length, "Table size should double after reaching the load factor limit");

        for (int i = 1; i <= 13; i++) {
            assertEquals(i, hashTable.get("key" + i), "Values should be preserved after rehashing");
        }
    }

    @Test
    void testIterator() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);
        hashTable.put("key3", 3);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

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

        assertFalse(iterator.hasNext(), "Iterator should not have elements after all entries are visited");
    }

    @Test
    void testConcurrentModificationException() {
        hashTable.put("key1", 1);
        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        hashTable.put("key2", 2); // Modify hashTable after creating iterator

        assertThrows(ConcurrentModificationException.class, iterator::next, "Modifying hashTable after iterator creation should throw ConcurrentModificationException on next()");
    }

    @Test
    void testEqualsAndToString() {
        HashTable<String, Integer> otherHashTable = new HashTable<>();
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        otherHashTable.put("key1", 1);
        otherHashTable.put("key2", 2);

        assertTrue(hashTable.equals(otherHashTable), "HashTables with same key-value pairs should be equal");
        assertEquals("{key1=1, key2=2}", hashTable.toString(), "toString should display all entries in 'key=value' format");
    }
}