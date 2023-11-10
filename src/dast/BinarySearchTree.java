package dast;

import java.awt.RenderingHints;
import java.util.stream.Stream;

/**
 * A binary search tree. See Corman chapter 12.
 *
 * Note that, as in many other cases, the student should be able to implement
 * all methods in both recursive and iterative format. In the tree below, we use
 * both.
 *
 * This class is meant to serve as an introduction to binary trees and not as a
 * complete and functional binary tree.
 *
 * @author Dov Neimand
 */
public class BinarySearchTree<T extends Comparable<T>> {

    
    private class Node extends BSTNode<T>{
    
        public Node(T key) {
            super(key);
        }
    
    };
    
    /**
     * The root node of the tree.
     */
    private BSTNode root;

    /**
     * Is the binary tree empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * An ordered stream of elements in the binary tree.
     *
     * @return An ordered stream of elements in the binary tree.
     */
    public Stream<T> stream() {
        if (isEmpty()) return Stream.of();
        return root.inOrderTreeWalk();
    }

    /**
     * Does the tree contain the given item.
     * Note, we implement this method here rather than in BSTNode to 
     * give an example of iterative/non recursive implementation.
     * @param t The item that may be in the tree.
     * @return True if t is in the tree and false otherwise.
     */
    public boolean contains(T t) {
        if (isEmpty()) return false;
        BSTNode<T> n = root;
        while (!n.isLeaf() && !n.getKey().equals(t))
            n = t.compareTo(n.getKey()) < 0 ? n.getLeft() : n.getRight();
        return n.getKey().equals(t);
    }

    /**
     * Adds an element to the tree.
     *
     * @param t The element to be added.
     */
    public void insert(T t) {
        if (isEmpty()) root = new BSTNode(t);
        else root.insert(t);
    }

    public void delete(T t) {
        if (!isEmpty()) root.delete(t);
    }

    /**
     * Creates an empty binary tree.
     */
    public BinarySearchTree() {
    }

    /**
     * The main method is currently set to test some of the other methods.
     *
     * @param args
     */
    public static void main(String[] args) {
        BinarySearchTree<String> bt = new BinarySearchTree<>();

        bt.insert("Hi!");
        bt.insert("Good morning.");
        bt.insert("Hello World");
        bt.insert("A");
        bt.insert("Gx");
        bt.insert("Gz");

        bt.delete("Good morning.");

        bt.stream().forEach(System.out::println);

        System.out.println(bt.contains("A"));
        System.out.println(bt.contains("B"));

    }
}
