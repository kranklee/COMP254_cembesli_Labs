package exercise3;

public class Exercise3 {
    public static class SinglyLinkedList<E> {

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
        public E first() { if (isEmpty()) return null; return head.getElement(); }
        public E last() { if (isEmpty()) return null; return tail.getElement(); }

        public void addFirst(E e) {
            head = new Node<>(e, head);
            if (size == 0) tail = head;
            size++;
        }

        public void addLast(E e) {
            Node<E> newest = new Node<>(e, null);
            if (isEmpty()) head = newest; else tail.setNext(newest);
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


        public void concatenate(SinglyLinkedList<E> other) {
            if (other.isEmpty()) return;
            if (this.isEmpty()) this.head = other.head;
            else this.tail.setNext(other.head);
            this.tail = other.tail;
            this.size += other.size;
            other.head = null;
            other.tail = null;
            other.size = 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            Node<E> walk = head;
            while (walk != null) {
                sb.append(walk.getElement());
                if (walk != tail) sb.append(", ");
                walk = walk.getNext();
            }
            return sb.append(")").toString();
        }
    }

    public static class LinkedQueue<E> {
        private SinglyLinkedList<E> list = new SinglyLinkedList<>();
        public int size() { return list.size(); }
        public boolean isEmpty() { return list.isEmpty(); }
        public void enqueue(E e) { list.addLast(e); }
        public E first() { return list.first(); }
        public E dequeue() { return list.removeFirst(); }

        public void concatenate(LinkedQueue<E> Q2) {
            list.concatenate(Q2.list);
        }

        public String toString() { return list.toString(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 3: concatenate(Q2) ===\n");

        LinkedQueue<Integer> Q1 = new LinkedQueue<>(), Q2 = new LinkedQueue<>();
        Q1.enqueue(10); Q1.enqueue(20); Q1.enqueue(30);
        Q2.enqueue(40); Q2.enqueue(50); Q2.enqueue(60);

        System.out.println("Before: Q1=" + Q1 + " size=" + Q1.size());
        System.out.println("Before: Q2=" + Q2 + " size=" + Q2.size());
        Q1.concatenate(Q2);
        System.out.println("\nAfter:  Q1=" + Q1 + " size=" + Q1.size());
        System.out.println("After:  Q2=" + Q2 + " size=" + Q2.size() + " (empty)");

        System.out.print("\nDequeue Q1: ");
        while (!Q1.isEmpty()) System.out.print(Q1.dequeue() + " ");

        System.out.println("\n\n--- Empty queue test ---");
        LinkedQueue<String> emptyQ = new LinkedQueue<>(), dataQ = new LinkedQueue<>();
        dataQ.enqueue("A"); dataQ.enqueue("B");
        emptyQ.concatenate(dataQ);
        System.out.println("Empty + data = " + emptyQ + " size=" + emptyQ.size());
    }
}
