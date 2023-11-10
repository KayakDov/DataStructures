package dast;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A simple tree node with some basic operations.
 *
 * @author Dov Neimand
 */
public class BinaryNode<T> {

    private BinaryNode parent, left, right;
    private T key;

    /**
     * Creates a root node.
     *
     * @param key The value held at the root node.
     */
    public BinaryNode(T key) {
        this.key = key;
    }

    /**
     * Does this node have a left child.
     *
     * @return True if this node has a left child, false otherwise.
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Does this node have a right child.
     *
     * @return True if this node has a right child. False otherwise.
     */
    public boolean hasRight() {
        return right != null;
    }

    /**
     * Is this a leaf.
     *
     * @return true if this is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return !hasLeft() && !hasRight();
    }

    /**
     * Sets the left child of the node to a new node with the proffered element.
     * If this node already has left child, then that child will be replaced and
     * its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public BinaryNode<T> setLeft(T t) {
        left = new BinaryNode(t);
        left.parent = this;
        return getLeft();
    }

    /**
     * Sets the right child of the node to a new node with the proffered
     * element. If this node already has right child, then that child will be
     * replaced and its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public BinaryNode<T> setRight(T t) {
        right = new BinaryNode(t);
        right.parent = this;
        return getRight();
    }

    /**
     * Sets the left child of the node to a new node with the proffered element.
     * If this node already has left child, then that child will be replaced and
     * its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public BinaryNode<T> setLeft(BinaryNode<T> n) {
        left = n;
        if (n != null) n.parent = this;
        return getLeft();
    }

    /**
     * Sets the right child of the node to a new node with the proffered
     * element. If this node already has right child, then that child will be
     * replaced and its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public BinaryNode<T> setRight(BinaryNode<T> n) {
        right = n;
        if (n != null) right.parent = this;
        return getRight();
    }

    /**
     * Does this node have only one child?
     *
     * @return true if the node has one or fewer children, false otherwise.
     */
    public boolean hasOneChildOrLess() {
        return (hasLeft()?1:0) + (hasRight()?1:0) < 2;
    }

    /**
     * Is this a root node?
     *
     * @return true if this is a root node, false if it is not.
     */
    public boolean isRoot() {
        return parent == null;
    }

    public BinaryNode getParent() {
        return parent;
    }

    /**
     * Is this node a left child?
     *
     * @return True if this node is a left child, false otherwise.
     */
    public boolean isLeft() {
        if (isRoot()) return false;
        return parent.left == this;
    }

    /**
     * Is this node a right child?
     *
     * @return True if this node is a right child, false otherwise.
     */
    public boolean isRight() {
        if (isRoot()) return false;
        return parent.right == this;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public T getKey() {
        return key;
    }

    /**
     * Swaps this node out with the replacement node. Note that the replacement
     * node brings its subtree with it.
     *
     * @param replacement The node / subtree to transplant this one.
     * @param return The replacement node.
     */
    public void transplant(BinaryNode<T> replacement) {
        if (!isRoot()) {
            if (isLeft()) getParent().setLeft(replacement);
            else getParent().setRight(replacement);
        }
        else{
            setLeft(replacement.getLeft());
            setRight(replacement.getRight());
            setKey(replacement.getKey());
        }
    }

    public void setKey(T key) {
        this.key = key;
    }

    /**
     * The sibling of this node.
     *
     * @return The sibling of this node. Or null if none exists.
     */
    public BinaryNode<T> sibling() {
        if (isRoot()) return null;
        return isLeft() ? getParent().getRight() : getParent().getLeft();
    }

    /**
     * The unique uncle of this node.
     *
     * @return The unique uncle of this node, or null if none exists.
     */
    public BinaryNode<T> uncle() {
        if (isRoot()) return null;
        return getParent().sibling();
    }

    /**
     * Checks if this node has an uncle.
     *
     * @return True if the node has an uncle, and false otherwise.
     */
    public boolean hasUnlce() {
        return uncle() != null;
    }

    /**
     * The grandparent of this node.
     *
     * @return The grandparent of this node, or null if there isn't one.
     */
    public BinaryNode<T> grandParent() {
        if (isRoot()) return null;
        return getParent().getParent();
    }
    
    
    /**
     * The nearest nephew on the tree.
     * @return The nearest nephue on the tree, or null if there isn't one.
     */
    public BinaryNode<T> nearNephew(){
        if(sibling() == null) return null;
        return isLeft()? sibling().getLeft() : sibling().getRight();
    }
    
    /**
     * The farthest nephew on the tree.
     * @return The farthest nephew on the tree.
     */
    public BinaryNode<T> farNephew(){
        if(sibling() == null) return null;
        return isLeft()? sibling().getRight() : sibling().getLeft();
    }

    /**
     * Does this node have a grandparent?
     *
     * @return True if this node has a grandparent, false otherwise.
     */
    public boolean hasGrandParent() {
        return grandParent() != null;
    }

    /**
     * A stream of all the elements in this tree in order.
     *
     * @return A stream of all the elements in this tree in order.
     */
    public Stream<BinaryNode<T>> inOrderTreeWalk() {

        Stream<BinaryNode<T>> leftStream
                = hasLeft() ? getLeft().inOrderTreeWalk() : Stream.of(),
                rightStream
                = hasRight() ? getRight().inOrderTreeWalk() : Stream.of();

        return Stream.concat(
                Stream.concat(leftStream, Stream.of(this)),
                rightStream
        );
    }

    /**
     * Finds the node containing t.
     *
     * @param t The item who's node is being searched for.
     * @return The node containing t, or null if there is none.
     */
    public BinaryNode<T> find(T t) {
        return inOrderTreeWalk().filter(node -> node.getKey().equals(t))
                .findAny().orElse(null);
    }
    
    /**
     * The number of elements in the tree.
     * @return The number of elements in the tree.
     */
    public long size(){
        return inOrderTreeWalk().count();
    }
    
    /**
     * Fills the array with the keys of the tree. The format is the same used
     * in heap, with the left child of an element at 2*i + 1 and the right
     * child of an element at 2*i + 2. Note, the array length must be greater
     * than the number of elements in the tree.
     * @param array The array being filled.
     */
    public T[] fillArray(T[] array){
        fillArray(0, array);
        return array;
    }
    
    /**
     * Fills the array with the keys of the tree. The format is the same used
     * in heap, with the left child of an element at 2*i + 1 and the right
     * child of an element at 2*i + 2.
     * @param i The index of the current element being assigned.
     * @param array The array being filled.
     */
    private void fillArray(int i, T[] array){
        array[i] = getKey();
        
        if(hasLeft()) getLeft().fillArray(2*i + 1, array);
        if(hasRight()) getRight().fillArray(2*i + 2, array);
    }
    
    /**
     * Removes this node and all descenents from the tree, by severing its 
     * parents pointer to this.
     */
    protected void deleteSubTree(){
        if(isRight()) getParent().setRight((BinaryNode)null);
        if(isLeft()) getParent().setLeft((BinaryNode)null);
    }
}
