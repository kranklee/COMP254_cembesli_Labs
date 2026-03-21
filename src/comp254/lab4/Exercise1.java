package exercise1;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Exercise1 {


    public interface Position<E> {
        E getElement() throws IllegalStateException;
    }


    public interface PositionalList<E> extends Iterable<E> {
        int size(); boolean isEmpty();
        Position<E> first(); Position<E> last();
        Position<E> before(Position<E> p) throws IllegalArgumentException;
        Position<E> after(Position<E> p) throws IllegalArgumentException;
        Position<E> addFirst(E e); Position<E> addLast(E e);
        Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException;
        Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException;
        E set(Position<E> p, E e) throws IllegalArgumentException;
        E remove(Position<E> p) throws IllegalArgumentException;
        Iterator<E> iterator();
        Iterable<Position<E>> positions();
    }


    public static class LinkedPositionalList<E> implements PositionalList<E> {

        private static class Node<E> implements Position<E> {
            private E element;
            private Node<E> prev;
            private Node<E> next;
            public Node(E e, Node<E> p, Node<E> n) { element = e; prev = p; next = n; }
            public E getElement() throws IllegalStateException {
                if (next == null) throw new IllegalStateException("Position no longer valid");
                return element;
            }
            public Node<E> getPrev() { return prev; }
            public Node<E> getNext() { return next; }
            public void setElement(E e) { element = e; }
            public void setPrev(Node<E> p) { prev = p; }
            public void setNext(Node<E> n) { next = n; }
        }

        private Node<E> header;
        private Node<E> trailer;
        private int size = 0;

        public LinkedPositionalList() {
            header = new Node<>(null, null, null);
            trailer = new Node<>(null, header, null);
            header.setNext(trailer);
        }

        private Node<E> validate(Position<E> p) throws IllegalArgumentException {
            if (!(p instanceof Node)) throw new IllegalArgumentException("Invalid p");
            Node<E> node = (Node<E>) p;
            if (node.getNext() == null) throw new IllegalArgumentException("p is no longer in the list");
            return node;
        }

        private Position<E> position(Node<E> node) {
            if (node == header || node == trailer) return null;
            return node;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public Position<E> first() { return position(header.getNext()); }
        public Position<E> last() { return position(trailer.getPrev()); }
        public Position<E> before(Position<E> p) { return position(validate(p).getPrev()); }
        public Position<E> after(Position<E> p) { return position(validate(p).getNext()); }

        private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
            Node<E> newest = new Node<>(e, pred, succ);
            pred.setNext(newest); succ.setPrev(newest); size++;
            return newest;
        }

        public Position<E> addFirst(E e) { return addBetween(e, header, header.getNext()); }
        public Position<E> addLast(E e) { return addBetween(e, trailer.getPrev(), trailer); }
        public Position<E> addBefore(Position<E> p, E e) { Node<E> n=validate(p); return addBetween(e, n.getPrev(), n); }
        public Position<E> addAfter(Position<E> p, E e) { Node<E> n=validate(p); return addBetween(e, n, n.getNext()); }
        public E set(Position<E> p, E e) { Node<E> n=validate(p); E old=n.getElement(); n.setElement(e); return old; }

        public E remove(Position<E> p) {
            Node<E> node = validate(p);
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            size--;
            E answer = node.getElement();
            node.setElement(null); node.setNext(null); node.setPrev(null);
            return answer;
        }


        public int indexOf(Position<E> p) {
            validate(p);
            int index = 0;
            Position<E> walk = first();
            while (walk != null) {
                if (walk == p) return index;
                walk = after(walk);
                index++;
            }
            return -1;
        }

        private class PositionIterator implements Iterator<Position<E>> {
            private Position<E> cursor = first(), recent = null;
            public boolean hasNext() { return cursor != null; }
            public Position<E> next() { if(cursor==null) throw new NoSuchElementException(); recent=cursor; cursor=after(cursor); return recent; }
            public void remove() { if(recent==null) throw new IllegalStateException(); LinkedPositionalList.this.remove(recent); recent=null; }
        }
        public Iterable<Position<E>> positions() { return () -> new PositionIterator(); }
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                Iterator<Position<E>> it = new PositionIterator();
                public boolean hasNext() { return it.hasNext(); }
                public E next() { return it.next().getElement(); }
            };
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            Node<E> walk = header.getNext();
            while (walk != trailer) {
                sb.append(walk.getElement()); walk = walk.getNext();
                if (walk != trailer) sb.append(", ");
            }
            return sb.append(")").toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: indexOf(p) ===\n");
        LinkedPositionalList<String> list = new LinkedPositionalList<>();
        Position<String> pA = list.addLast("A"), pB = list.addLast("B"),
                pC = list.addLast("C"), pD = list.addLast("D"),
                pE = list.addLast("E");

        System.out.println("List: " + list);
        System.out.println("indexOf(A)=" + list.indexOf(pA) + " indexOf(C)=" + list.indexOf(pC) + " indexOf(E)=" + list.indexOf(pE));

        list.remove(pC);
        System.out.println("\nAfter removing C: " + list);
        System.out.println("indexOf(D)=" + list.indexOf(pD));

        Position<String> pF = list.addAfter(pB, "F");
        System.out.println("\nAfter adding F after B: " + list);
        System.out.println("indexOf(F)=" + list.indexOf(pF));
    }
}
