package comp254.lab6;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Exercise 2: containsKey(k) method for SortedTableMap
 *
 * Student: Cem Besli
 * Course: COMP-254
 */

class MapEntry2<K,V> {
    private K k;
    private V v;
    public MapEntry2(K key, V value) { k = key; v = value; }
    public K getKey() { return k; }
    public V getValue() { return v; }
    protected V setValue(V value) {
        V old = v;
        v = value;
        return old;
    }
    public String toString() { return "<" + k + ", " + v + ">"; }
}

class SortedTableMap<K,V> {
    private ArrayList<MapEntry2<K,V>> table = new ArrayList<>();
    private Comparator<K> comp;

    public SortedTableMap() { comp = new DefaultComp<>(); }
    public SortedTableMap(Comparator<K> c) { comp = c; }

    private static class DefaultComp<E> implements Comparator<E> {
        @SuppressWarnings("unchecked")
        public int compare(E a, E b) { return ((Comparable<E>) a).compareTo(b); }
    }

    private int findIndex(K key) { return findIndex(key, 0, table.size() - 1); }

    private int findIndex(K key, int low, int high) {
        if (high < low) return high + 1;        // key not found, return insertion point
        int mid = (low + high) / 2;
        int result = comp.compare(key, table.get(mid).getKey());
        if (result == 0)
            return mid;                           // exact match
        else if (result < 0)
            return findIndex(key, low, mid - 1);  // search left half
        else
            return findIndex(key, mid + 1, high); // search right half
    }

    public int size() { return table.size(); }
    public boolean isEmpty() { return table.isEmpty(); }

    public V get(K key) {
        int j = findIndex(key);
        if (j == size() || comp.compare(key, table.get(j).getKey()) != 0)
            return null;
        return table.get(j).getValue();
    }

    public V put(K key, V value) {
        int j = findIndex(key);
        if (j < size() && comp.compare(key, table.get(j).getKey()) == 0)
            return table.get(j).setValue(value);   // key exists, update value
        table.add(j, new MapEntry2<>(key, value)); // insert at sorted position
        return null;
    }

    public V remove(K key) {
        int j = findIndex(key);
        if (j == size() || comp.compare(key, table.get(j).getKey()) != 0)
            return null;
        return table.remove(j).getValue();
    }

    public boolean containsKey(K key) {
        int j = findIndex(key);
        return j < size() && comp.compare(key, table.get(j).getKey()) == 0;
    }

    public Iterable<MapEntry2<K,V>> entrySet() { return table; }
}

public class Exercise2 {

    public static void main(String[] args) {
        System.out.println("=== Exercise 2: containsKey for SortedTableMap ===");
        System.out.println("Student: Cem Besli");
        System.out.println();

        SortedTableMap<Integer, String> map = new SortedTableMap<>();

        map.put(3, "three");
        map.put(1, "one");
        map.put(7, "seven");
        map.put(5, null);       // key with null value
        map.put(9, "nine");
        map.put(2, "two");

        System.out.println("Map size: " + map.size());
        System.out.println();

        System.out.println("--- Testing containsKey ---");
        System.out.println("containsKey(3): " + map.containsKey(3));   // true
        System.out.println("containsKey(1): " + map.containsKey(1));   // true
        System.out.println("containsKey(7): " + map.containsKey(7));   // true
        System.out.println("containsKey(9): " + map.containsKey(9));   // true
        System.out.println();

        System.out.println("containsKey(4): " + map.containsKey(4));   // false
        System.out.println("containsKey(0): " + map.containsKey(0));   // false
        System.out.println("containsKey(10): " + map.containsKey(10)); // false
        System.out.println();

        System.out.println("--- The null value problem ---");
        System.out.println("get(5): " + map.get(5));                   // null
        System.out.println("get(4): " + map.get(4));                   // null
        System.out.println("Both return null, but:");
        System.out.println("containsKey(5): " + map.containsKey(5));   // true  (key exists with null value)
        System.out.println("containsKey(4): " + map.containsKey(4));   // false (key does not exist)
        System.out.println();

        System.out.println("--- After removing key 3 ---");
        map.remove(3);
        System.out.println("containsKey(3): " + map.containsKey(3));   // false
        System.out.println("Map size: " + map.size());

        System.out.println();
        System.out.println("=== Exercise 2 Complete ===");
    }
}