package comp254.lab5;

import java.util.ArrayList;
import java.util.List;

/**
 * COMP 254 - Lab Exercise
 * Computes and prints element + subtree height for every position
 * using postorder traversal.
 *
 * Author: Cem Besli
 */
public class HeightCalculator {

    interface Position<E> {
        E getElement() throws IllegalStateException;
    }

    interface Tree<E> {
        Position<E> root();
        Position<E> parent(Position<E> p) throws IllegalArgumentException;
        Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;
        int numChildren(Position<E> p) throws IllegalArgumentException;
        boolean isInternal(Position<E> p) throws IllegalArgumentException;
        boolean isExternal(Position<E> p) throws IllegalArgumentException;
        boolean isRoot(Position<E> p) throws IllegalArgumentException;
        int size();
        boolean isEmpty();
    }

    interface BinaryTree<E> extends Tree<E> {
        Position<E> left(Position<E> p) throws IllegalArgumentException;
        Position<E> right(Position<E> p) throws IllegalArgumentException;
        Position<E> sibling(Position<E> p) throws IllegalArgumentException;
    }

    static class LinkedBinaryTree<E> implements BinaryTree<E> {

        protected static class Node<E> implements Position<E> {
            private E element;
            private Node<E> parent;
            private Node<E> left;
            private Node<E> right;

            public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
                element = e;
                parent = above;
                left = leftChild;
                right = rightChild;
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

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public Position<E> root() {
            return root;
        }

        @Override
        public Position<E> parent(Position<E> p) throws IllegalArgumentException {
            Node<E> node = validate(p);
            return node.getParent();
        }

        @Override
        public Position<E> left(Position<E> p) throws IllegalArgumentException {
            Node<E> node = validate(p);
            return node.getLeft();
        }

        @Override
        public Position<E> right(Position<E> p) throws IllegalArgumentException {
            Node<E> node = validate(p);
            return node.getRight();
        }

        @Override
        public Position<E> sibling(Position<E> p) throws IllegalArgumentException {
            Position<E> parent = parent(p);
            if (parent == null) return null;
            if (p == left(parent))
                return right(parent);
            else
                return left(parent);
        }

        @Override
        public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
            List<Position<E>> snapshot = new ArrayList<>(2);
            if (left(p) != null)
                snapshot.add(left(p));
            if (right(p) != null)
                snapshot.add(right(p));
            return snapshot;
        }

        @Override
        public int numChildren(Position<E> p) throws IllegalArgumentException {
            int count = 0;
            if (left(p) != null) count++;
            if (right(p) != null) count++;
            return count;
        }

        @Override
        public boolean isInternal(Position<E> p) throws IllegalArgumentException {
            return numChildren(p) > 0;
        }

        @Override
        public boolean isExternal(Position<E> p) throws IllegalArgumentException {
            return numChildren(p) == 0;
        }

        @Override
        public boolean isRoot(Position<E> p) throws IllegalArgumentException {
            return p == root();
        }

        public Position<E> addRoot(E e) throws IllegalStateException {
            if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
            root = new Node<>(e, null, null, null);
            size = 1;
            return root;
        }

        public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
            Node<E> parent = validate(p);
            if (parent.getLeft() != null)
                throw new IllegalArgumentException("p already has a left child");
            Node<E> child = new Node<>(e, parent, null, null);
            parent.setLeft(child);
            size++;
            return child;
        }

        public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
            Node<E> parent = validate(p);
            if (parent.getRight() != null)
                throw new IllegalArgumentException("p already has a right child");
            Node<E> child = new Node<>(e, parent, null, null);
            parent.setRight(child);
            size++;
            return child;
        }

        public static <E> void parenthesize(Tree<E> T, Position<E> p) {
            System.out.print(p.getElement());
            if (T.isInternal(p)) {
                boolean firstTime = true;
                for (Position<E> c : T.children(p)) {
                    System.out.print(firstTime ? " (" : ", ");
                    firstTime = false;
                    parenthesize(T, c);
                }
                System.out.print(")");
            }
        }
    }


    public static <E> int printElementAndHeight(LinkedBinaryTree<E> T, Position<E> p) {
        if (p == null) return -1;

        int leftHeight = printElementAndHeight(T, T.left(p));
        int rightHeight = printElementAndHeight(T, T.right(p));

        int height;
        if (leftHeight == -1 && rightHeight == -1) {
            height = 0;
        } else {
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        System.out.println("Element: " + p.getElement() + "  Height: " + height);
        return height;
    }

    public static void main(String[] args) {

        // Tree 1
        System.out.println("=== Tree 1 ===");
        LinkedBinaryTree<String> lbt = new LinkedBinaryTree<>();
        Position<String> root = lbt.addRoot("LANGUAGE");

        Position<String> softwarePosition = lbt.addLeft(root, "Software");
        lbt.addRight(root, "java");
        lbt.addLeft(softwarePosition, "SET");
        lbt.addRight(softwarePosition, "IG");

        System.out.println("Parenthesized:");
        LinkedBinaryTree.parenthesize(lbt, root);
        System.out.println("\n");

        System.out.println("Postorder: Element and Subtree Height");
        System.out.println("OUOUOUOUOUOUOUOUOU");
        printElementAndHeight(lbt, lbt.root());

        // Tree 2
        System.out.println("\n=== Tree 2 ===");
        LinkedBinaryTree<String> lbt2 = new LinkedBinaryTree<>();
        Position<String> root2 = lbt2.addRoot("+");

        Position<String> xLeft = lbt2.addLeft(root2, "X");
        Position<String> xRight = lbt2.addRight(root2, "X");

        lbt2.addLeft(xLeft, "2");
        Position<String> minusRight = lbt2.addRight(xLeft, "-");

        lbt2.addLeft(xRight, "3");
        lbt2.addRight(xRight, "b");

        lbt2.addLeft(minusRight, "a");
        lbt2.addRight(minusRight, "1");

        System.out.println("Parenthesized:");
        LinkedBinaryTree.parenthesize(lbt2, root2);
        System.out.println("\n");

        System.out.println("Postorder: Element and Subtree Height");
        System.out.println("TESTTESTTESTTESTTEST");
        printElementAndHeight(lbt2, lbt2.root());
    }
}