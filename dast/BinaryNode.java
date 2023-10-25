package dast;

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
    public void setLeft(T t) {
        left = new BinaryNode(t);
        left.parent = this;
    }

    /**
     * Sets the right child of the node to a new node with the proffered
     * element. If this node already has right child, then that child will be
     * replaced and its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public void setRight(T t) {
        right = new BinaryNode(t);
        right.parent = this;
    }

    /**
     * Sets the left child of the node to a new node with the proffered element.
     * If this node already has left child, then that child will be replaced and
     * its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public void setLeft(BinaryNode<T> n) {
        left = n;
        if(n != null) n.parent = this;
    }

    /**
     * Sets the right child of the node to a new node with the proffered
     * element. If this node already has right child, then that child will be
     * replaced and its subtree lost to this node.
     *
     * @param t The element of the new left child.
     */
    public void setRight(BinaryNode<T> n) {
        right = n;
        if(n != null) right.parent = this;
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

    public BinaryNode getParent() {
        return parent;
    }

    /**
     * Is this node a left child?
     *
     * @return True if this node is a left child, false otherwise.
     */
    public boolean isLeftChild() {
        if (isRoot()) return false;
        return parent.left == this;
    }

    /**
     * Is this node a right child?
     *
     * @return True if this node is a right child, false otherwise.
     */
    public boolean isRightChild() {
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
     * Swaps this node out with the replacement node.  Note that the replacement
     * node brings its subtree with it. 
     * @param replacement The node / subtree to replace this one.
     */
    public void replace(BinaryNode<T> replacement){
        if(isRoot()) return;
        if(isLeftChild()) getParent().setLeft(replacement);
        else getParent().setRight(replacement);
    }
    
    public void setKey(T key) {
        this.key = key;
    }
    
    
}
