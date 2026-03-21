package exercise2;

public class Exercise2 {

    public interface Stack<E> {
        int size(); boolean isEmpty(); void push(E e); E top(); E pop();
    }

    public static class LinkedStack<E> implements Stack<E> {
        private static class Node<E> { E element; Node<E> next; Node(E e, Node<E> n){element=e; next=n;} }
        private Node<E> top = null;
        private int size = 0;
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public void push(E e) { top = new Node<>(e, top); size++; }
        public E top() { return isEmpty() ? null : top.element; }
        public E pop() { if (isEmpty()) return null; E a=top.element; top=top.next; size--; return a; }
        public String toString() {
            StringBuilder sb = new StringBuilder("["); Node<E> w = top;
            while (w != null) { sb.append(w.element); if (w.next!=null) sb.append(", "); w=w.next; }
            return sb.append("] <-- top is left").toString();
        }
    }
    public static <E> void transfer(Stack<E> S, Stack<E> T) {
        while (!S.isEmpty()) {
            T.push(S.pop());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 2: transfer(S, T) ===\n");
        LinkedStack<Integer> S = new LinkedStack<>(), T = new LinkedStack<>();
        for (int i = 1; i <= 5; i++) S.push(i);

        System.out.println("Before: S=" + S + "  T=" + T);
        transfer(S, T);
        System.out.println("After:  S=" + S + "  T=" + T);
        System.out.println("Top of T (should be 1): " + T.top());

        System.out.print("Popping T: ");
        while (!T.isEmpty()) System.out.print(T.pop() + " ");
        System.out.println();
    }
}
