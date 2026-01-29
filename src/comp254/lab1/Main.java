package comp254.lab1;

public class Main {

    public static void main(String[] args) {

        // ================= Exercise 1 =================
        System.out.println("Exercise 1 - DoublyLinkedList");

        DoublyLinkedList<String> d1 = new DoublyLinkedList<>();
        d1.addLast("A");
        d1.addLast("B");
        d1.addLast("C");

        DoublyLinkedList<String> d2 = new DoublyLinkedList<>();
        d2.addLast("D");
        d2.addLast("E");

        System.out.println("L: " + d1);
        System.out.println("M: " + d2);

        // concatenate using only methods we learned
        while (!d2.isEmpty()) {
            d1.addLast(d2.removeFirst());
        }

        System.out.println("L' after concat: " + d1);
        System.out.println();
        // ================= Exercise 2 =================
        System.out.println("Exercise 2 - SinglyLinkedList (basic demo)");

        SinglyLinkedList<String> s = new SinglyLinkedList<>();
        s.addLast("MSP");
        s.addLast("ATL");
        s.addLast("BOS");

        System.out.println("Before: " + s);
        s.addFirst("LAX");
        System.out.println("After addFirst: " + s);
        s.removeFirst();
        System.out.println("After removeFirst: " + s);
        System.out.println();
        // ================= Exercise 3 =================
        System.out.println("Exercise 3 - CircularlyLinkedList clone");

        CircularlyLinkedList<String> c = new CircularlyLinkedList<>();
        c.addFirst("LAX");
        c.addLast("MSP");
        c.addLast("ATL");
        c.addLast("BOS");

        System.out.println("Original: " + c);

        CircularlyLinkedList<String> cloned = c.cloneList();
        System.out.println("Clone:    " + cloned);

        c.removeFirst();
        System.out.println("Original after removeFirst: " + c);
        System.out.println("Clone unchanged:            " + cloned);
    }
}
