package ru.nsu.kochanov;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * This test class for testing entry class.
 */

class EntryTest {

    @Test
    void testEqualsAndHashCode() {
        Entry<String, Integer> entry1 = new Entry<>("key1", 1);
        Entry<String, Integer> entry2 = new Entry<>("key1", 1);
        Entry<String, Integer> entry3 = new Entry<>("key2", 2);

        // Проверка, что entry1 и entry2 равны
        assertEquals(entry1, entry2, "Entries with same key and value should be equal");
        assertEquals(entry1.hashCode(), entry2.hashCode(), "Hash codes of equal entries should be the same");

        // Проверка, что entry1 и entry3 не равны
        assertNotEquals(entry1, entry3, "Entries with different key or value should not be equal");
    }

    @Test
    void testToString() {
        Entry<String, Integer> entry = new Entry<>("key", 42);

        // Проверка правильного формата toString
        assertEquals("key=42", entry.toString(), "toString should return 'key=value' format");
    }

    @Test
    void testNullKeyAndValue() {
        Entry<String, Integer> entry = new Entry<>(null, null);

        // Проверка, что ключ и значение null
        assertNull(entry.key, "Key should be null");
        assertNull(entry.value, "Value should be null");

        // Проверка toString для null значений
        assertEquals("null=null", entry.toString(), "toString should return 'null=null' for null key and value");
    }
}