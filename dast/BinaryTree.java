package dast;

import java.util.stream.Stream;

/**
 * A binary search tree. See Corman chapter 12.
 *
 * Note that, as in many other cases, the student should be able to implement
 * all methods in both recursive and iterative format.  In the tree below,
 * we use both.
 * 
 * @author Dov Neimand
 */
public class BinaryTree<T extends Comparable<T>> {

    /**
     * Each node has the property that all nodes to the left of it have smaller
     * keys and all nodes to the right have bigger keys.
     */
    private class Node {

        public T key;
        public Node /**
                 * Every element in the left subtree is smaller than this.
                 */
                left,
                /**
                 * Every element in the right subtree is smaller than this.
                 */
                right,
                /**
                 * The parent of this node.
                 */
                parent;

        /**
         * The constructor for a node. left and right subtrees are by default
         * null.
         *
         * @param key The element to be held at this node.
         */
        public Node(T key, Node parent) {
            this.key = key;
            this.parent = parent;
        }

        /**
         * Is this a leafe.
         *
         * @return true if this is a leafe, false otherwise.
         */
        public boolean isLeaf() {
            return !hasLeft() && !hasRight();
        }

        /**
         * Does this node have a left child.
         *
         * @return True if the node has a left child, false otherwise.
         */
        public boolean hasLeft() {
            return left != null;
        }

        /**
         * Does this node have a right
         *
         * @return ture if the node has a right child, false otherwise.
         */
        public boolean hasRight() {
            return right != null;
        }

        /**
         * Does this node have only one child?
         *
         * @return true if the node has only one child, false otherwise.
         */
        public boolean hasOneChild() {
            return hasLeft() && !hasRight() || hasRight() && !hasLeft();
        }

        /**
         * Is this a root node?
         *
         * @return true if this is a root node, false if it is not.
         */
        public boolean isRoot() {
            return parent == null;
        }

        /**
         * Is this node a left child?
         *
         * @return True if this node is a left child, false otherwise.
         */
        private boolean isLeftChild() {
            if (isRoot()) return false;
            return parent.left == this;
        }

        /**
         * Is this node a right child?
         *
         * @return True if this node is a right child, false otherwise.
         */
        private boolean isRightChild() {
            if (isRoot()) return false;
            return parent.right == this;
        }

        /**
         * The minimum element in the subtree rooted at this node.
         *
         * @return The minimum element in the subtree rooted at this node.
         */
        public Node min() {
            if (left == null) return this;
            return left.min();
        }

        /**
         * The Maximum element in the subtree rooted at this node.
         *
         * @return The maximum element in the subtree rooted at this node.
         */
        public Node max() {
            Node n = this;
            while (n.right != null) n = n.right;
            return n;
        }

        /**
         * A stream of all the elements in this tree in order.
         *
         * @return A stream of all the elements in this tree in order.
         */
        public Stream<T> inOrderTreeWalk() {
            Stream leftStream = hasLeft() ? left.inOrderTreeWalk() : Stream.of(),
                    rightStream = hasRight() ? right.inOrderTreeWalk() : Stream.of();

            return Stream.concat(
                    Stream.concat(leftStream, Stream.of(key)),
                    rightStream
            );
        }

        /**
         * The smallest element in the tree greater than this.
         */
        public Node successor() {
            if (right == null) return null;
            return right.min();
        }

        /**
         * The greatest element in the tree less than this.
         *
         * @return
         */
        public Node predecessor() {
            if (left == null) return null;
            return left.max();
        }

        /**
         * Inserts a key into the tree.
         *
         * @param t The key to be inserted.
         */
        public void insert(T t) {
            if (t.compareTo(key) < 0) {
                if (!hasLeft()) left = new Node(t, this);
                else left.insert(t);
            } else {
                if (!hasRight()) right = new Node(t, this);
                else right.insert(t);
            }
        }

        /**
         * Sets the proffered node as one of the immediate children of this node
         * instead of the current child.
         *
         * @param node
         */
        private void replaceChild(Node node) {
            
            if (node.key.compareTo(key) < 0) left = node;
            else right = node;

            node.parent = this;
        }

        /**
         * If there is only one child, that child is returned.
         *
         * @return The only child if there is only one. Otherwise undefined.
         */
        public Node loneChild() {
            return hasLeft() ? left : right;
        }

        /**
         * Removes this node from supertree without regaurd for the order of the
         * tree. This should only be called on a leaf.
         */
        private void cutFromParent() {
            if (isRoot()) root = null;
            else {
                if (key.compareTo(parent.key) < 0) parent.left = null;
                else parent.right = null;
            }
        }

        /**
         * Replace this node with its only child. Do not call if there's more
         * than one child.
         */
        private void replaceLoneChild() {
            if (isRoot()) root = loneChild();
            else parent.replaceChild(loneChild());
        }

        /**
         * Replace this node with its successor while preserving the rest
         * of the tree.
         */
        private void replaceAndPassTwoChildren() {

            Node suc = successor();
            if (suc.hasRight()) suc.parent.replaceChild(suc.right);
            if (isRoot()) root = suc;
            else parent.replaceChild(suc);
            suc.left = left;
            suc.right = right;
        }

        /**
         * deletes a node from the tree.
         *
         * @param t The element in the node.
         * @return true if the element is in the tree. False otherwise.
         */
        public boolean delete(T t) {
            int comp = t.compareTo(key);

            if(comp < 0) return hasLeft() ? left.delete(t) : false;
            else if(comp > 0) return hasRight() ? right.delete(t) : false;
            else {
                if (isLeaf()) cutFromParent();
                else if (hasOneChild()) replaceLoneChild();
                else replaceAndPassTwoChildren();
                return true;
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
     * @param t The item that may be in the tree.
     * @return True if t is in the tree and false otherwise.
     */
    public boolean contains(T t){
        if(isEmpty()) return false;
        Node n = root;
        while(!n.isLeaf() && n.key != t)
            n = t.compareTo(n.key) < 0? n.left : n.right;
        return n.key.equals(t);
    }

    /**
     * Adds an element to the tree.
     *
     * @param t The element to be added.
     */
    public void insert(T t) {
        if (isEmpty()) root = new Node(t, null);
        else root.insert(t);
    }

    public boolean delete(T t) {
        if (isEmpty()) return false;
        return root.delete(t);
    }

    /**
     * Creates an empty binary tree.
     */
    public BinaryTree() {
    }

    /**
     * The main method is currently set to test some of the other methods.
     *
     * @param args
     */
    public static void main(String[] args) {
        BinaryTree<String> bt = new BinaryTree<>();

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
