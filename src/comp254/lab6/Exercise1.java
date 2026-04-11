package comp254.lab6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Exercise 1: AbstractHashMap with configurable load factor
 * Experiments on ChainHashMap and ProbeHashMap with varying load factors
 *
 * Student: Cem Besli
 * Course: COMP-254
 */

// -------- Entry interface --------
interface MapInterface<K,V> {
    int size();
    boolean isEmpty();
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    Iterable<K> keySet();
    Iterable<V> values();
    Iterable<MapEntry<K,V>> entrySet();
}

// -------- MapEntry class --------
class MapEntry<K,V> {
    private K k;
    private V v;
    public MapEntry(K key, V value) { k = key; v = value; }
    public K getKey() { return k; }
    public V getValue() { return v; }
    protected void setKey(K key) { k = key; }
    protected V setValue(V value) {
        V old = v;
        v = value;
        return old;
    }
    public String toString() { return "<" + k + ", " + v + ">"; }
}

// -------- AbstractMap --------
abstract class AbstractMap<K,V> implements MapInterface<K,V> {
    public boolean isEmpty() { return size() == 0; }

    private class KeyIterator implements Iterator<K> {
        private Iterator<MapEntry<K,V>> entries = entrySet().iterator();
        public boolean hasNext() { return entries.hasNext(); }
        public K next() { return entries.next().getKey(); }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    private class KeyIterable implements Iterable<K> {
        public Iterator<K> iterator() { return new KeyIterator(); }
    }

    public Iterable<K> keySet() { return new KeyIterable(); }

    private class ValueIterator implements Iterator<V> {
        private Iterator<MapEntry<K,V>> entries = entrySet().iterator();
        public boolean hasNext() { return entries.hasNext(); }
        public V next() { return entries.next().getValue(); }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    private class ValueIterable implements Iterable<V> {
        public Iterator<V> iterator() { return new ValueIterator(); }
    }

    public Iterable<V> values() { return new ValueIterable(); }
}

// -------- UnsortedTableMap --------
class UnsortedTableMap<K,V> extends AbstractMap<K,V> {
    private ArrayList<MapEntry<K,V>> table = new ArrayList<>();

    private int findIndex(K key) {
        int n = table.size();
        for (int j = 0; j < n; j++)
            if (table.get(j).getKey().equals(key))
                return j;
        return -1;
    }

    public int size() { return table.size(); }

    public V get(K key) {
        int j = findIndex(key);
        if (j == -1) return null;
        return table.get(j).getValue();
    }

    public V put(K key, V value) {
        int j = findIndex(key);
        if (j == -1) {
            table.add(new MapEntry<>(key, value));
            return null;
        } else
            return table.get(j).setValue(value);
    }

    public V remove(K key) {
        int j = findIndex(key);
        if (j == -1) return null;
        int n = table.size();
        V answer = table.get(j).getValue();
        if (j != n - 1)
            table.set(j, table.get(n - 1));
        table.remove(n - 1);
        return answer;
    }

    private class EntryIterator implements Iterator<MapEntry<K,V>> {
        private int j = 0;
        public boolean hasNext() { return j < table.size(); }
        public MapEntry<K,V> next() {
            if (j == table.size()) throw new NoSuchElementException();
            return table.get(j++);
        }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    private class EntryIterable implements Iterable<MapEntry<K,V>> {
        public Iterator<MapEntry<K,V>> iterator() { return new EntryIterator(); }
    }

    public Iterable<MapEntry<K,V>> entrySet() { return new EntryIterable(); }
}

// -------- AbstractHashMap (modified with configurable load factor) --------
abstract class AbstractHashMap<K,V> extends AbstractMap<K,V> {
    protected int n = 0;
    protected int capacity;
    private int prime;
    private long scale, shift;
    protected double maxLoadFactor;       // configurable max load factor

    public AbstractHashMap(int cap, int p, double maxLoad) {
        prime = p;
        capacity = cap;
        maxLoadFactor = maxLoad;
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        createTable();
    }

    public AbstractHashMap(int cap, double maxLoad) { this(cap, 109345121, maxLoad); }
    public AbstractHashMap(int cap) { this(cap, 109345121, 0.5); }
    public AbstractHashMap() { this(17, 109345121, 0.5); }
    public AbstractHashMap(double maxLoad) { this(17, 109345121, maxLoad); }

    public int size() { return n; }

    public V get(K key) { return bucketGet(hashValue(key), key); }

    public V remove(K key) { return bucketRemove(hashValue(key), key); }

    public V put(K key, V value) {
        V answer = bucketPut(hashValue(key), key, value);
        if (n > capacity * maxLoadFactor)       // keep load <= maxLoadFactor
            resize(2 * capacity - 1);
        return answer;
    }

    public double getLoadFactor() { return (double) n / capacity; }
    public double getMaxLoadFactor() { return maxLoadFactor; }
    public int getCapacity() { return capacity; }

    public void setMaxLoadFactor(double maxLoad) {
        if (maxLoad <= 0 || maxLoad > 1.0)
            throw new IllegalArgumentException("Load factor must be between 0 and 1");
        this.maxLoadFactor = maxLoad;
    }

    private int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    private void resize(int newCap) {
        ArrayList<MapEntry<K,V>> buffer = new ArrayList<>(n);
        for (MapEntry<K,V> e : entrySet())
            buffer.add(e);
        capacity = newCap;
        createTable();
        n = 0;
        for (MapEntry<K,V> e : buffer)
            put(e.getKey(), e.getValue());
    }

    protected abstract void createTable();
    protected abstract V bucketGet(int h, K k);
    protected abstract V bucketPut(int h, K k, V v);
    protected abstract V bucketRemove(int h, K k);
}

// -------- ChainHashMap --------
class ChainHashMap<K,V> extends AbstractHashMap<K,V> {
    private UnsortedTableMap<K,V>[] table;

    public ChainHashMap() { super(); }
    public ChainHashMap(int cap) { super(cap); }
    public ChainHashMap(int cap, double maxLoad) { super(cap, maxLoad); }
    public ChainHashMap(double maxLoad) { super(maxLoad); }

    @SuppressWarnings({"unchecked"})
    protected void createTable() {
        table = (UnsortedTableMap<K,V>[]) new UnsortedTableMap[capacity];
    }

    protected V bucketGet(int h, K k) {
        UnsortedTableMap<K,V> bucket = table[h];
        if (bucket == null) return null;
        return bucket.get(k);
    }

    protected V bucketPut(int h, K k, V v) {
        if (table[h] == null)
            table[h] = new UnsortedTableMap<>();
        int oldSize = table[h].size();
        V answer = table[h].put(k, v);
        n += (table[h].size() - oldSize);
        return answer;
    }

    protected V bucketRemove(int h, K k) {
        UnsortedTableMap<K,V> bucket = table[h];
        if (bucket == null) return null;
        int oldSize = bucket.size();
        V answer = bucket.remove(k);
        n -= (oldSize - bucket.size());
        return answer;
    }

    public Iterable<MapEntry<K,V>> entrySet() {
        ArrayList<MapEntry<K,V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++)
            if (table[h] != null)
                for (MapEntry<K,V> entry : table[h].entrySet())
                    buffer.add(entry);
        return buffer;
    }
}

// -------- ProbeHashMap --------
class ProbeHashMap<K,V> extends AbstractHashMap<K,V> {
    private MapEntry<K,V>[] table;
    private MapEntry<K,V> DEFUNCT = new MapEntry<>(null, null);

    public ProbeHashMap() { super(); }
    public ProbeHashMap(int cap) { super(cap); }
    public ProbeHashMap(int cap, double maxLoad) { super(cap, maxLoad); }
    public ProbeHashMap(double maxLoad) { super(maxLoad); }

    @SuppressWarnings({"unchecked"})
    protected void createTable() {
        table = (MapEntry<K,V>[]) new MapEntry[capacity];
    }

    private boolean isAvailable(int j) {
        return (table[j] == null || table[j] == DEFUNCT);
    }

    private int findSlot(int h, K k) {
        int avail = -1;
        int j = h;
        do {
            if (isAvailable(j)) {
                if (avail == -1) avail = j;
                if (table[j] == null) break;
            } else if (table[j].getKey().equals(k))
                return j;
            j = (j + 1) % capacity;
        } while (j != h);
        return -(avail + 1);
    }

    protected V bucketGet(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) return null;
        return table[j].getValue();
    }

    protected V bucketPut(int h, K k, V v) {
        int j = findSlot(h, k);
        if (j >= 0)
            return table[j].setValue(v);
        table[-(j + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    protected V bucketRemove(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) return null;
        V answer = table[j].getValue();
        table[j] = DEFUNCT;
        n--;
        return answer;
    }

    public Iterable<MapEntry<K,V>> entrySet() {
        ArrayList<MapEntry<K,V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++)
            if (!isAvailable(h))
                buffer.add(table[h]);
        return buffer;
    }
}

// -------- Main Test Class --------
public class Exercise1 {

    public static long testChainHashMap(int n, double maxLoad) {
        ChainHashMap<Integer, Integer> map = new ChainHashMap<>(11, maxLoad);
        Random rand = new Random(254);
        long startTime = System.nanoTime();
        for (int i = 0; i < n; i++) {
            int key = rand.nextInt(n * 10);
            map.put(key, i);
        }
        rand = new Random(254);
        for (int i = 0; i < n; i++) {
            int key = rand.nextInt(n * 10);
            map.get(key);
        }
        long finalTime = System.nanoTime();
        return finalTime - startTime;
    }

    public static long testProbeHashMap(int n, double maxLoad) {
        ProbeHashMap<Integer, Integer> map = new ProbeHashMap<>(11, maxLoad);
        Random rand = new Random(254);
        long startTime = System.nanoTime();
        for (int i = 0; i < n; i++) {
            int key = rand.nextInt(n * 10);
            map.put(key, i);
        }
        rand = new Random(254);
        for (int i = 0; i < n; i++) {
            int key = rand.nextInt(n * 10);
            map.get(key);
        }
        long finalTime = System.nanoTime();
        return finalTime - startTime;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: HashMap Load Factor Experiment ===");
        System.out.println("Student: Cem Besli");
        System.out.println();

        double[] loadFactors = {0.3, 0.5, 0.7, 0.85};
        int[] sizes = {500, 2000, 8000, 20000};

        // Part 1: demonstrate configurable load factor
        System.out.println("--- Part 1: Configurable Load Factor Demo ---");
        ChainHashMap<String, Integer> demo = new ChainHashMap<>(13, 0.7);
        System.out.println("Created ChainHashMap with maxLoad = " + demo.getMaxLoadFactor());
        demo.put("java", 10);
        demo.put("python", 20);
        demo.put("react", 30);
        demo.put("mongo", 40);
        demo.put("graphql", 50);
        demo.put("nodejs", 60);
        System.out.println("After 6 inserts: size = " + demo.size()
                + ", capacity = " + demo.getCapacity()
                + ", load = " + String.format("%.3f", demo.getLoadFactor()));
        demo.setMaxLoadFactor(0.4);
        System.out.println("Changed maxLoad to " + demo.getMaxLoadFactor());
        System.out.println();

        // Part 2: performance comparison
        System.out.println("--- Part 2: Performance Experiments (put + get, in ms) ---");
        System.out.println();
        System.out.printf("%-12s", "n");
        for (double lf : loadFactors)
            System.out.printf("| Load=%-5.2f ", lf);
        System.out.println();
        System.out.println("-".repeat(60));

        System.out.println("ChainHashMap:");
        for (int n : sizes) {
            System.out.printf("%-12d", n);
            for (double lf : loadFactors) {
                long time = testChainHashMap(n, lf);
                double ms = time / 1_000_000.0;
                System.out.printf("| %-10.2f ", ms);
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("ProbeHashMap:");
        for (int n : sizes) {
            System.out.printf("%-12d", n);
            for (double lf : loadFactors) {
                long time = testProbeHashMap(n, lf);
                double ms = time / 1_000_000.0;
                System.out.printf("| %-10.2f ", ms);
            }
            System.out.println();
        }
        System.out.println();

        // Part 3: capacity comparison
        System.out.println("--- Part 3: Final Capacity After 5000 Inserts ---");
        System.out.printf("%-15s%-20s%-20s%n", "Load Factor", "Chain Capacity", "Probe Capacity");
        System.out.println("-".repeat(55));
        for (double lf : loadFactors) {
            ChainHashMap<Integer, Integer> chain = new ChainHashMap<>(11, lf);
            ProbeHashMap<Integer, Integer> probe = new ProbeHashMap<>(11, lf);
            Random rand = new Random(254);
            for (int i = 0; i < 5000; i++) {
                int key = rand.nextInt(50000);
                chain.put(key, i);
                probe.put(key, i);
            }
            System.out.printf("%-15.2f%-20d%-20d%n", lf, chain.getCapacity(), probe.getCapacity());
        }

        System.out.println();
        System.out.println("=== Experiment Complete ===");
    }
}