package comp254.lab7;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Exercise1 {
    interface Entry<K, V> {
        K getKey();
        V getValue();
    }
    interface Position<E> {
        E getElement() throws IllegalStateException;
    }
    interface Map<K, V> {
        int size();
        boolean isEmpty();
        V get(K key);
        V put(K key, V value);
        V remove(K key);
        Iterable<K> keySet();
        Iterable<V> values();
        Iterable<Entry<K, V>> entrySet();
    }
    interface SortedMap<K, V> extends Map<K, V> {
        Entry<K, V> firstEntry();
        Entry<K, V> lastEntry();
        Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException;
        Entry<K, V> floorEntry(K key) throws IllegalArgumentException;
        Entry<K, V> lowerEntry(K key) throws IllegalArgumentException;
        Entry<K, V> higherEntry(K key) throws IllegalArgumentException;
        Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException;
    }
    interface Tree<E> extends Iterable<E> {
        Position<E> root();
        Position<E> parent(Position<E> p) throws IllegalArgumentException;
        Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;
        int numChildren(Position<E> p) throws IllegalArgumentException;
        boolean isInternal(Position<E> p) throws IllegalArgumentException;
        boolean isExternal(Position<E> p) throws IllegalArgumentException;
        boolean isRoot(Position<E> p) throws IllegalArgumentException;
        int size();
        boolean isEmpty();
        Iterator<E> iterator();
        Iterable<Position<E>> positions();
    }
    interface BinaryTree<E> extends Tree<E> {
        Position<E> left(Position<E> p) throws IllegalArgumentException;
        Position<E> right(Position<E> p) throws IllegalArgumentException;
        Position<E> sibling(Position<E> p) throws IllegalArgumentException;
    }
    interface Queue<E> {
        int size();
        boolean isEmpty();
        void enqueue(E e);
        E first();
        E dequeue();
    }
    static class SinglyLinkedList<E> {
        private static class Node<E> {
            private E element;
            private Node<E> next;
            public Node(E e, Node<E> n) { element = e; next = n; }
            public E getElement() { return element; }
            public Node<E> getNext() { return next; }
            public void setNext(Node<E> n) { next = n; }
        }
        private Node<E> head = null;
        private Node<E> tail = null;
        private int size = 0;
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public E first() { if (isEmpty()) return null; return head.getElement(); }
        public void addLast(E e) {
            Node<E> newest = new Node<>(e, null);
            if (isEmpty()) head = newest; else tail.setNext(newest);
            tail = newest; size++;
        }
        public E removeFirst() {
            if (isEmpty()) return null;
            E answer = head.getElement();
            head = head.getNext(); size--;
            if (size == 0) tail = null;
            return answer;
        }
    }
    static class LinkedQueue<E> implements Queue<E> {
        private SinglyLinkedList<E> list = new SinglyLinkedList<>();
        public int size() { return list.size(); }
        public boolean isEmpty() { return list.isEmpty(); }
        public void enqueue(E element) { list.addLast(element); }
        public E first() { return list.first(); }
        public E dequeue() { return list.removeFirst(); }
    }
    static class DefaultComparator<E> implements Comparator<E> {
        @SuppressWarnings("unchecked")
        public int compare(E a, E b) throws ClassCastException {
            return ((Comparable<E>) a).compareTo(b);
        }
    }
    static abstract class AbstractTree<E> implements Tree<E> {
        public boolean isInternal(Position<E> p) { return numChildren(p) > 0; }
        public boolean isExternal(Position<E> p) { return numChildren(p) == 0; }
        public boolean isRoot(Position<E> p) { return p == root(); }
        public int numChildren(Position<E> p) {
            int count = 0;
            for (Position<E> child : children(p)) count++;
            return count;
        }
        public int size() {
            int count = 0;
            for (Position<E> p : positions()) count++;
            return count;
        }
        public boolean isEmpty() { return size() == 0; }

        private class ElementIterator implements Iterator<E> {
            Iterator<Position<E>> posIterator = positions().iterator();
            public boolean hasNext() { return posIterator.hasNext(); }
            public E next() { return posIterator.next().getElement(); }
            public void remove() { posIterator.remove(); }
        }
        public Iterator<E> iterator() { return new ElementIterator(); }
        public Iterable<Position<E>> positions() { return preorder(); }

        private void preorderSubtree(Position<E> p, java.util.List<Position<E>> snapshot) {
            snapshot.add(p);
            for (Position<E> c : children(p)) preorderSubtree(c, snapshot);
        }
        public Iterable<Position<E>> preorder() {
            java.util.List<Position<E>> snapshot = new ArrayList<>();
            if (!isEmpty()) preorderSubtree(root(), snapshot);
            return snapshot;
        }

        public Iterable<Position<E>> breadthfirst() {
            java.util.List<Position<E>> snapshot = new ArrayList<>();
            if (!isEmpty()) {
                Queue<Position<E>> fringe = new LinkedQueue<>();
                fringe.enqueue(root());
                while (!fringe.isEmpty()) {
                    Position<E> p = fringe.dequeue();
                    snapshot.add(p);
                    for (Position<E> c : children(p)) fringe.enqueue(c);
                }
            }
            return snapshot;
        }
    }
    static abstract class AbstractBinaryTree<E> extends AbstractTree<E> implements BinaryTree<E> {
        public Position<E> sibling(Position<E> p) {
            Position<E> parent = parent(p);
            if (parent == null) return null;
            if (p == left(parent)) return right(parent);
            else return left(parent);
        }
        public int numChildren(Position<E> p) {
            int count = 0;
            if (left(p) != null) count++;
            if (right(p) != null) count++;
            return count;
        }
        public Iterable<Position<E>> children(Position<E> p) {
            java.util.List<Position<E>> snapshot = new ArrayList<>(2);
            if (left(p) != null) snapshot.add(left(p));
            if (right(p) != null) snapshot.add(right(p));
            return snapshot;
        }
        private void inorderSubtree(Position<E> p, java.util.List<Position<E>> snapshot) {
            if (left(p) != null) inorderSubtree(left(p), snapshot);
            snapshot.add(p);
            if (right(p) != null) inorderSubtree(right(p), snapshot);
        }
        public Iterable<Position<E>> inorder() {
            java.util.List<Position<E>> snapshot = new ArrayList<>();
            if (!isEmpty()) inorderSubtree(root(), snapshot);
            return snapshot;
        }
        public Iterable<Position<E>> positions() { return inorder(); }
    }
    static class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
        protected static class Node<E> implements Position<E> {
            private E element;
            private Node<E> parent;
            private Node<E> left;
            private Node<E> right;
            public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
                element = e; parent = above; left = leftChild; right = rightChild;
            }
            public E getElement() { return element; }
            public Node<E> getParent() { return parent; }
            public Node<E> getLeft() { return left; }
            public Node<E> getRight() { return right; }
            public void setElement(E e) { element = e; }
            public void setParent(Node<E> parentNode) { parent = parentNode; }
            public void setLeft(Node<E> leftChild) { left = leftChild; }
            public void setRight(Node<E> rightChild) { right = rightChild; }
        }
        protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
            return new Node<>(e, parent, left, right);
        }
        protected Node<E> root = null;
        private int size = 0;
        public LinkedBinaryTree() { }

        protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
            if (!(p instanceof Node))
                throw new IllegalArgumentException("Not valid position type");
            Node<E> node = (Node<E>) p;
            if (node.getParent() == node)
                throw new IllegalArgumentException("p is no longer in the tree");
            return node;
        }
        public int size() { return size; }
        public Position<E> root() { return root; }
        public Position<E> parent(Position<E> p) { return validate(p).getParent(); }
        public Position<E> left(Position<E> p) { return validate(p).getLeft(); }
        public Position<E> right(Position<E> p) { return validate(p).getRight(); }

        public Position<E> addRoot(E e) throws IllegalStateException {
            if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
            root = createNode(e, null, null, null); size = 1; return root;
        }
        public Position<E> addLeft(Position<E> p, E e) {
            Node<E> parent = validate(p);
            if (parent.getLeft() != null) throw new IllegalArgumentException("p already has a left child");
            Node<E> child = createNode(e, parent, null, null);
            parent.setLeft(child); size++; return child;
        }
        public Position<E> addRight(Position<E> p, E e) {
            Node<E> parent = validate(p);
            if (parent.getRight() != null) throw new IllegalArgumentException("p already has a right child");
            Node<E> child = createNode(e, parent, null, null);
            parent.setRight(child); size++; return child;
        }
        public E set(Position<E> p, E e) {
            Node<E> node = validate(p);
            E temp = node.getElement(); node.setElement(e); return temp;
        }
        public E remove(Position<E> p) {
            Node<E> node = validate(p);
            if (numChildren(p) == 2) throw new IllegalArgumentException("p has two children");
            Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
            if (child != null) child.setParent(node.getParent());
            if (node == root) root = child;
            else {
                Node<E> parent = node.getParent();
                if (node == parent.getLeft()) parent.setLeft(child);
                else parent.setRight(child);
            }
            size--;
            E temp = node.getElement();
            node.setElement(null); node.setLeft(null); node.setRight(null);
            node.setParent(node);
            return temp;
        }
    }
    static abstract class AbstractMap<K, V> implements Map<K, V> {
        public boolean isEmpty() { return size() == 0; }

        protected static class MapEntry<K, V> implements Entry<K, V> {
            private K k;
            private V v;
            public MapEntry(K key, V value) { k = key; v = value; }
            public K getKey() { return k; }
            public V getValue() { return v; }
            protected void setKey(K key) { k = key; }
            protected V setValue(V value) { V old = v; v = value; return old; }
            public String toString() { return "<" + k + ", " + v + ">"; }
        }

        private class KeyIterator implements Iterator<K> {
            private Iterator<Entry<K, V>> entries = entrySet().iterator();
            public boolean hasNext() { return entries.hasNext(); }
            public K next() { return entries.next().getKey(); }
            public void remove() { throw new UnsupportedOperationException(); }
        }
        private class KeyIterable implements Iterable<K> {
            public Iterator<K> iterator() { return new KeyIterator(); }
        }
        public Iterable<K> keySet() { return new KeyIterable(); }

        private class ValueIterator implements Iterator<V> {
            private Iterator<Entry<K, V>> entries = entrySet().iterator();
            public boolean hasNext() { return entries.hasNext(); }
            public V next() { return entries.next().getValue(); }
            public void remove() { throw new UnsupportedOperationException(); }
        }
        private class ValueIterable implements Iterable<V> {
            public Iterator<V> iterator() { return new ValueIterator(); }
        }
        public Iterable<V> values() { return new ValueIterable(); }
    }
    static abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {
        private Comparator<K> comp;
        protected AbstractSortedMap(Comparator<K> c) { comp = c; }
        protected AbstractSortedMap() { this(new DefaultComparator<K>()); }
        protected int compare(Entry<K, V> a, Entry<K, V> b) { return comp.compare(a.getKey(), b.getKey()); }
        protected int compare(K a, Entry<K, V> b) { return comp.compare(a, b.getKey()); }
        protected int compare(Entry<K, V> a, K b) { return comp.compare(a.getKey(), b); }
        protected int compare(K a, K b) { return comp.compare(a, b); }
        protected boolean checkKey(K key) throws IllegalArgumentException {
            try { return (comp.compare(key, key) == 0); }
            catch (ClassCastException e) { throw new IllegalArgumentException("Incompatible key"); }
        }
    }
    static class TreeMap<K, V> extends AbstractSortedMap<K, V> {

        protected static class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {
            protected static class BSTNode<E> extends Node<E> {
                int aux = 0;
                BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
                    super(e, parent, leftChild, rightChild);
                }
                public int getAux() { return aux; }
                public void setAux(int value) { aux = value; }
            }
            public int getAux(Position<Entry<K, V>> p) { return ((BSTNode<Entry<K, V>>) p).getAux(); }
            public void setAux(Position<Entry<K, V>> p, int value) { ((BSTNode<Entry<K, V>>) p).setAux(value); }
            @Override
            protected Node<Entry<K, V>> createNode(Entry<K, V> e, Node<Entry<K, V>> parent,
                                                   Node<Entry<K, V>> left, Node<Entry<K, V>> right) {
                return new BSTNode<>(e, parent, left, right);
            }
            private void relink(Node<Entry<K, V>> parent, Node<Entry<K, V>> child, boolean makeLeftChild) {
                child.setParent(parent);
                if (makeLeftChild) parent.setLeft(child); else parent.setRight(child);
            }
            public void rotate(Position<Entry<K, V>> p) {
                Node<Entry<K, V>> x = validate(p);
                Node<Entry<K, V>> y = x.getParent();
                Node<Entry<K, V>> z = y.getParent();
                if (z == null) { root = x; x.setParent(null); }
                else relink(z, x, y == z.getLeft());
                if (x == y.getLeft()) { relink(y, x.getRight(), true); relink(x, y, false); }
                else { relink(y, x.getLeft(), false); relink(x, y, true); }
            }
            public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
                Position<Entry<K, V>> y = parent(x);
                Position<Entry<K, V>> z = parent(y);
                if ((x == right(y)) == (y == right(z))) { rotate(y); return y; }
                else { rotate(x); rotate(x); return x; }
            }
        }

        protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

        public TreeMap() { super(); tree.addRoot(null); }
        public TreeMap(Comparator<K> comp) { super(comp); tree.addRoot(null); }

        public int size() { return (tree.size() - 1) / 2; }

        private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
            tree.set(p, entry);
            tree.addLeft(p, null);
            tree.addRight(p, null);
        }

        protected Position<Entry<K, V>> root() { return tree.root(); }
        protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) { return tree.parent(p); }
        protected Position<Entry<K, V>> left(Position<Entry<K, V>> p) { return tree.left(p); }
        protected Position<Entry<K, V>> right(Position<Entry<K, V>> p) { return tree.right(p); }
        protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) { return tree.sibling(p); }
        protected boolean isRoot(Position<Entry<K, V>> p) { return tree.isRoot(p); }
        protected boolean isExternal(Position<Entry<K, V>> p) { return tree.isExternal(p); }
        protected boolean isInternal(Position<Entry<K, V>> p) { return tree.isInternal(p); }
        protected void set(Position<Entry<K, V>> p, Entry<K, V> e) { tree.set(p, e); }
        protected Entry<K, V> remove(Position<Entry<K, V>> p) { return tree.remove(p); }
        private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
            while (!isExternal(p)) {
                int comp = compare(key, p.getElement());
                if (comp == 0)
                    return p;                    // key found
                else if (comp < 0)
                    p = left(p);                 // search left subtree
                else
                    p = right(p);                // search right subtree
            }
            return p;                            // key not found, return the leaf
        }

        protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
            Position<Entry<K, V>> walk = p;
            while (isInternal(walk)) walk = left(walk);
            return parent(walk);
        }

        protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
            Position<Entry<K, V>> walk = p;
            while (isInternal(walk)) walk = right(walk);
            return parent(walk);
        }

        public V get(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            rebalanceAccess(p);
            if (isExternal(p)) return null;
            return p.getElement().getValue();
        }

        public V put(K key, V value) throws IllegalArgumentException {
            checkKey(key);
            Entry<K, V> newEntry = new MapEntry<>(key, value);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isExternal(p)) {
                expandExternal(p, newEntry);
                rebalanceInsert(p);
                return null;
            } else {
                V old = p.getElement().getValue();
                set(p, newEntry);
                rebalanceAccess(p);
                return old;
            }
        }

        public V remove(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isExternal(p)) {
                rebalanceAccess(p);
                return null;
            } else {
                V old = p.getElement().getValue();
                if (isInternal(left(p)) && isInternal(right(p))) {
                    Position<Entry<K, V>> replacement = treeMax(left(p));
                    set(p, replacement.getElement());
                    p = replacement;
                }
                Position<Entry<K, V>> leaf = (isExternal(left(p)) ? left(p) : right(p));
                Position<Entry<K, V>> sib = sibling(leaf);
                remove(leaf);
                remove(p);
                rebalanceDelete(sib);
                return old;
            }
        }

        public Entry<K, V> firstEntry() {
            if (isEmpty()) return null;
            return treeMin(root()).getElement();
        }
        public Entry<K, V> lastEntry() {
            if (isEmpty()) return null;
            return treeMax(root()).getElement();
        }
        public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isInternal(p)) return p.getElement();
            while (!isRoot(p)) {
                if (p == left(parent(p))) return parent(p).getElement();
                else p = parent(p);
            }
            return null;
        }
        public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isInternal(p)) return p.getElement();
            while (!isRoot(p)) {
                if (p == right(parent(p))) return parent(p).getElement();
                else p = parent(p);
            }
            return null;
        }
        public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isInternal(p) && isInternal(left(p))) return treeMax(left(p)).getElement();
            while (!isRoot(p)) {
                if (p == right(parent(p))) return parent(p).getElement();
                else p = parent(p);
            }
            return null;
        }
        public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
            checkKey(key);
            Position<Entry<K, V>> p = treeSearch(root(), key);
            if (isInternal(p) && isInternal(right(p))) return treeMin(right(p)).getElement();
            while (!isRoot(p)) {
                if (p == left(parent(p))) return parent(p).getElement();
                else p = parent(p);
            }
            return null;
        }
        public Iterable<Entry<K, V>> entrySet() {
            ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
            for (Position<Entry<K, V>> p : tree.inorder())
                if (isInternal(p)) buffer.add(p.getElement());
            return buffer;
        }
        public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
            checkKey(fromKey); checkKey(toKey);
            ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
            if (compare(fromKey, toKey) < 0) subMapRecurse(fromKey, toKey, root(), buffer);
            return buffer;
        }
        private void subMapRecurse(K fromKey, K toKey, Position<Entry<K, V>> p, ArrayList<Entry<K, V>> buffer) {
            if (isInternal(p))
                if (compare(p.getElement(), fromKey) < 0)
                    subMapRecurse(fromKey, toKey, right(p), buffer);
                else {
                    subMapRecurse(fromKey, toKey, left(p), buffer);
                    if (compare(p.getElement(), toKey) < 0) {
                        buffer.add(p.getElement());
                        subMapRecurse(fromKey, toKey, right(p), buffer);
                    }
                }
        }

        protected void rebalanceInsert(Position<Entry<K, V>> p) { }
        protected void rebalanceDelete(Position<Entry<K, V>> p) { }
        protected void rebalanceAccess(Position<Entry<K, V>> p) { }
        protected void dump() { dumpRecurse(root(), 0); }
        private void dumpRecurse(Position<Entry<K, V>> p, int depth) {
            String indent = (depth == 0 ? "" : String.format("%" + (2 * depth) + "s", ""));
            if (isExternal(p)) System.out.println(indent + "leaf");
            else { System.out.println(indent + p.getElement()); dumpRecurse(left(p), depth + 1); dumpRecurse(right(p), depth + 1); }
        }
    }
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(6, "A");
        map.put(2, "B");
        map.put(4, "C");
        map.put(1, "D");
        map.put(9, "E");
        map.put(8, "F");
        System.out.println("=== Testing Iterative treeSearch ===");
        System.out.println("get(4): " + map.get(4));
        System.out.println("get(9): " + map.get(9));
        System.out.println("get(1): " + map.get(1));
        System.out.println("get(99): " + map.get(99));
        System.out.println("\nhigherEntry(2): " + map.higherEntry(2));
        System.out.println("\nAll values:");
        for (String value : map.values()) {
            System.out.println(value);
        }
        System.out.println("\nAll entries: " + map.entrySet());
        System.out.println("\nTree structure:");
        map.dump();
        System.out.println("\nAfter removing key 1:");
        map.remove(1);
        map.dump();
        map.put(4, "COMP");
        map.put(11, "SET");
        System.out.println("\nAfter inserting (4,COMP) and (11,SET):");
        map.dump();
        System.out.println("get(11): " + map.get(11));
    }
}