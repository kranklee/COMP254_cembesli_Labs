package comp254.lab1;

public class CircularlyLinkedList<E> {

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public E getElement() { return element; }
        public Node<E> getNext() { return next; }
        public void setNext(Node<E> n) { next = n; }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() { }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public E first() {
        if (isEmpty()) return null;
        return tail.getNext().getElement();
    }

    public E last() {
        if (isEmpty()) return null;
        return tail.getElement();
    }

    public void rotate() {
        if (tail != null)
            tail = tail.getNext();
    }

    public void addFirst(E e) {
        if (size == 0) {
            tail = new Node<>(e, null);
            tail.setNext(tail);
        } else {
            Node<E> newest = new Node<>(e, tail.getNext());
            tail.setNext(newest);
        }
        size++;
    }

    public void addLast(E e) {
        addFirst(e);
        tail = tail.getNext();
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        Node<E> head = tail.getNext();
        if (head == tail) tail = null;
        else tail.setNext(head.getNext());
        size--;
        return head.getElement();
    }

    // Exercise 3
    public CircularlyLinkedList<E> cloneList() {
        CircularlyLinkedList<E> other = new CircularlyLinkedList<>();
        if (isEmpty()) return other;

        Node<E> head = tail.getNext();
        Node<E> walk = head;

        do {
            other.addLast(walk.getElement());
            walk = walk.getNext();
        } while (walk != head);

        return other;
    }

    public String toString() {
        if (tail == null) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = tail;
        do {
            walk = walk.getNext();
            sb.append(walk.getElement());
            if (walk != tail) sb.append(", ");
        } while (walk != tail);
        sb.append(")");
        return sb.toString();
    }
}
