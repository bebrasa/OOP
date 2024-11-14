package ru.nsu.kochanov;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;




/**
 * This class is Iterator for hashtable.
 */

public class HashTableIterator<K, V> implements Iterator<Entry<K, V>> {
    private final HashTable<K, V> hashTable;
    private int bucketIndex = 0;
    private int entryIndex = -1;
    private int expectedModCount;
    private Entry<K, V> nextEntry;

    /**
     * javadoc.
     */
    public HashTableIterator(HashTable<K, V> hashTable) {
        this.hashTable = hashTable;
        this.expectedModCount = hashTable.modCount;
        advance();
    }

    private void advance() {
        nextEntry = null;
        entryIndex++;
        while (bucketIndex < hashTable.table.length) {
            if (hashTable.table[bucketIndex] != null
                    && entryIndex < hashTable.table[bucketIndex].size()) {
                nextEntry = hashTable.table[bucketIndex].get(entryIndex);
                break;
            }
            bucketIndex++;
            entryIndex = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return nextEntry != null;
    }

    @Override
    public Entry<K, V> next() {
        if (hashTable.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        if (nextEntry == null) {
            throw new NoSuchElementException();
        }
        Entry<K, V> current = nextEntry;
        advance();
        return current;
    }
}