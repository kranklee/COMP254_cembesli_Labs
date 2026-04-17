package comp254.lab7;

/**
 * COMP 254 - Lab 7 Exercise
 * Bottom-up merge sort using a queue of queues.
 * Each item starts in its own queue, then pairs are merged
 * repeatedly until one sorted queue remains.
 *
 * Author: Cem Besli
 */
public class Exercise2 {

    // ---- SinglyLinkedList (from course code) ----
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
        public SinglyLinkedList() { }
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public E first() {
            if (isEmpty()) return null;
            return head.getElement();
        }
        public void addLast(E e) {
            Node<E> newest = new Node<>(e, null);
            if (isEmpty()) head = newest;
            else tail.setNext(newest);
            tail = newest;
            size++;
        }
        public E removeFirst() {
            if (isEmpty()) return null;
            E answer = head.getElement();
            head = head.getNext();
            size--;
            if (size == 0) tail = null;
            return answer;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            Node<E> walk = head;
            while (walk != null) {
                sb.append(walk.getElement());
                if (walk != tail) sb.append(", ");
                walk = walk.getNext();
            }
            sb.append(")");
            return sb.toString();
        }
    }

    // ---- Queue interface (from course code) ----
    interface Queue<E> {
        int size();
        boolean isEmpty();
        void enqueue(E e);
        E first();
        E dequeue();
    }

    // ---- LinkedQueue (from course code) ----
    static class LinkedQueue<E> implements Queue<E> {
        private SinglyLinkedList<E> list = new SinglyLinkedList<>();
        public LinkedQueue() { }
        public int size() { return list.size(); }
        public boolean isEmpty() { return list.isEmpty(); }
        public void enqueue(E element) { list.addLast(element); }
        public E first() { return list.first(); }
        public E dequeue() { return list.removeFirst(); }
        public String toString() { return list.toString(); }
    }

    /**
     * Merges two sorted queues into one sorted queue.
     */
    public static <E extends Comparable<E>> LinkedQueue<E> merge(LinkedQueue<E> q1, LinkedQueue<E> q2) {
        LinkedQueue<E> result = new LinkedQueue<>();
        while (!q1.isEmpty() && !q2.isEmpty()) {
            if (q1.first().compareTo(q2.first()) <= 0) {
                result.enqueue(q1.dequeue());
            } else {
                result.enqueue(q2.dequeue());
            }
        }
        // add remaining elements
        while (!q1.isEmpty()) {
            result.enqueue(q1.dequeue());
        }
        while (!q2.isEmpty()) {
            result.enqueue(q2.dequeue());
        }
        return result;
    }

    /**
     * Bottom-up merge sort using a queue of queues.
     * Step 1: place each item in its own single element queue
     * Step 2: put all those queues into a queue of queues
     * Step 3: repeatedly dequeue two queues, merge them, enqueue the result
     * Step 4: when one queue remains, that is the sorted result
     */
    public static <E extends Comparable<E>> LinkedQueue<E> bottomUpMergeSort(E[] items) {
        // queue of queues
        LinkedQueue<LinkedQueue<E>> queueOfQueues = new LinkedQueue<>();

        // step 1: each item goes into its own queue
        for (E item : items) {
            LinkedQueue<E> single = new LinkedQueue<>();
            single.enqueue(item);
            queueOfQueues.enqueue(single);
        }

        // step 2: merge pairs until one queue remains
        while (queueOfQueues.size() > 1) {
            LinkedQueue<E> q1 = queueOfQueues.dequeue();
            LinkedQueue<E> q2 = queueOfQueues.dequeue();
            LinkedQueue<E> merged = merge(q1, q2);
            queueOfQueues.enqueue(merged);
        }

        // the last remaining queue is sorted
        return queueOfQueues.dequeue();
    }

    public static void main(String[] args) {

        // Test 1: Integer array
        System.out.println("=== Test 1: Integer Array ===");
        Integer[] numbers = {55, 12, 94, 7, 33, 61, 28};
        System.out.println("Original: ");
        printArray(numbers);

        LinkedQueue<Integer> sorted1 = bottomUpMergeSort(numbers);
        System.out.println("Sorted:   " + sorted1);

        // Test 2: String array
        System.out.println("\n=== Test 2: String Array ===");
        String[] words = {"Toyota", "BMW", "Honda", "Audi", "Ford"};
        System.out.println("Original: ");
        printArray(words);

        LinkedQueue<String> sorted2 = bottomUpMergeSort(words);
        System.out.println("Sorted:   " + sorted2);

        // Test 3: single element
        System.out.println("\n=== Test 3: Single Element ===");
        Integer[] single = {17};
        System.out.println("Original: ");
        printArray(single);

        LinkedQueue<Integer> sorted3 = bottomUpMergeSort(single);
        System.out.println("Sorted:   " + sorted3);

        // Test 4: already sorted
        System.out.println("\n=== Test 4: Already Sorted ===");
        Integer[] alreadySorted = {10, 20, 30, 40, 50};
        System.out.println("Original: ");
        printArray(alreadySorted);

        LinkedQueue<Integer> sorted4 = bottomUpMergeSort(alreadySorted);
        System.out.println("Sorted:   " + sorted4);

        // Test 5: reverse order
        System.out.println("\n=== Test 5: Reverse Order ===");
        Integer[] reversed = {80, 60, 40, 20, 10};
        System.out.println("Original: ");
        printArray(reversed);

        LinkedQueue<Integer> sorted5 = bottomUpMergeSort(reversed);
        System.out.println("Sorted:   " + sorted5);
    }

    // helper to print an array
    private static <E> void printArray(E[] arr) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append(")");
        System.out.println(sb.toString());
    }
}