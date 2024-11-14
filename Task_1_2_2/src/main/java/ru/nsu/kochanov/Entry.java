package ru.nsu.kochanov;

import java.util.Objects;

/**
 * This class for entry the hashtable.
 *
 * @param <K> is for key.
 *
 * @param <V> is for value.

 */

public class Entry<K, V> {
    public K key;
    public V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entry<?, ?> entry = (Entry<?, ?>) obj;
        return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}