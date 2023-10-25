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

    /**
     * Each node has the property that all nodes to the left of it have smaller
     * keys and all nodes to the right have bigger keys.
     */
    private class Node extends BinaryNode<T>{

        /**
         * The constructor for a node. left and right subtrees are by default
         * null.
         *
         * @param key The element to be held at this node.
         */
        public Node(T key) {
            super(key);
        }

        @Override
        public void setRight(T t) {
            super.setRight(new Node(t));
        }
        
        @Override
        public void setLeft(T t) {
            setLeft(new Node(t)); 
        }

        @Override
        public Node getRight() {
            return (Node)super.getRight(); 
        }

        @Override
        public Node getLeft() {
            return (Node)super.getLeft();
        }

        @Override
        public Node getParent() {
            return (Node)super.getParent(); 
        }        
        
        /**
         * The minimum element in the subtree rooted at this node.
         *
         * @return The minimum element in the subtree rooted at this node.
         */
        public Node min() {
            if (!hasLeft()) return this;
            return getLeft().min();
        }

        /**
         * The Maximum element in the subtree rooted at this node.
         *
         * @return The maximum element in the subtree rooted at this node.
         */
        public Node max() {
            Node n = this;
            while (n.hasRight()) n = n.getRight();
            return n;
        }

        /**
         * A stream of all the elements in this tree in order.
         *
         * @return A stream of all the elements in this tree in order.
         */
        public Stream<T> inOrderTreeWalk() {
            
            Stream leftStream = 
                    hasLeft() ? getLeft().inOrderTreeWalk() : Stream.of(),
                   rightStream = 
                    hasRight() ? getRight().inOrderTreeWalk() : Stream.of();

            return Stream.concat(
                    Stream.concat(leftStream, Stream.of(getKey())),
                    rightStream
            );
        }

        /**
         * The smallest element in the tree greater than this.
         */
        public Node successor() {
            if (hasRight()) return getRight().min();
            return null;
            
        }

        /**
         * The greatest element in the tree less than this.
         *
         * @return
         */
        public Node predecessor() {
            if(hasLeft()) return getLeft().max();
            return null;
        }

        /**
         * Inserts a key into the tree.
         *
         * @param t The key to be inserted.
         */
        public void insert(T t) {
            if (t.compareTo(getKey()) < 0) {
                if (hasLeft()) getLeft().insert(t);
                else setLeft(t);
            } else {
                if (hasRight()) getRight().insert(t);
                else setRight(t);
            }
        }

        /**
         * If there is only one child, that child is returned.
         *
         * @return The only child if there is only one. Otherwise undefined.
         */
        public Node loneChild() {
            return hasLeft() ? getLeft() : getRight();
        }
                
        /**
         * Replace this node with its successor while preserving the rest of the
         * tree.
         */
        private void replaceAndPassChildren() {
            Node suc = successor();
            suc.replace(suc.getRight());
            setKey(suc.getKey());
        }

        /**
         * Deletes a node from the tree.
         *
         * @param t The element in the node.
         */
        public void delete(T t) {
            int comp = t.compareTo(getKey());

            if (comp < 0 && hasLeft()) getLeft().delete(t);
            else if (comp > 0 && hasRight())getRight().delete(t);
            else if(comp == 0){
                if (!hasLeft() || ! hasRight())  replace(loneChild());
                else replaceAndPassChildren();
            }

        }
    }

    /**
     * The root node of the tree.
     */
    private Node root;

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
     *
     * @param t The item that may be in the tree.
     * @return True if t is in the tree and false otherwise.
     */
    public boolean contains(T t) {
        if (isEmpty()) return false;
        Node n = root;
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
        if (isEmpty()) root = new Node(t);
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
