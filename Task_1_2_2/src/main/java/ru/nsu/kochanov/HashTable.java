package ru.nsu.kochanov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

// Основной класс HashTable
public class HashTable<K, V> implements Iterable<Entry<K, V>> {
    protected ArrayList<Entry<K, V>>[] table; // protected для доступа итератором
    private int size;
    protected int modCount; // protected для доступа итератором

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = new ArrayList[16]; // начальная ёмкость
        size = 0;
        modCount = 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                modCount++;
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;
        modCount++;

        if (size >= table.length * 0.75) { // перераспределение при превышении коэффициента загрузки 0.75
            rehash();
        }
    }

    public V get(K key) {
        int index = hash(key);
        if (table[index] == null) return null;

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int index = hash(key);
        if (table[index] == null) return null;

        Iterator<Entry<K, V>> iterator = table[index].iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.key.equals(key)) {
                V value = entry.value;
                iterator.remove();
                size--;
                modCount++;
                return value;
            }
        }
        return null;
    }

    public void update(K key, V value) {
        put(key, value);
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        ArrayList<Entry<K, V>>[] oldTable = table;
        table = new ArrayList[oldTable.length * 2];
        size = 0;

        for (ArrayList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    public boolean equals(HashTable<K, V> other) {
        if (this.size != other.size) return false;

        for (Entry<K, V> entry : this) {
            V otherValue = other.get(entry.key);
            if (!Objects.equals(entry.value, otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<K, V> entry : this) {
            sb.append(entry).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // убираем последнюю запятую
        sb.append("}");
        return sb.toString();
    }
}