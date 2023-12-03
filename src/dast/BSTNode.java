
package dast;

import java.util.stream.Stream;

/**
 *
 * @author Dov Neimand
 */

/**
 * Each node has the property that all nodes to the left of it have smaller keys
 * and all nodes to the right have bigger keys.
 */
public class BSTNode<T extends Comparable<T>> extends BinaryNode<T> {

    /**
     * The constructor for a node. left and right subtrees are by default null.
     *
     * @param key The element to be held at this node.
     */
    public BSTNode(T key) {
        super(key);
    }

    @Override
    public BSTNode<T> setRight(T t) {
        super.setRight(new BSTNode(t));
        return getRight();
    }

    @Override
    public BSTNode<T> setLeft(T t) {
        setLeft(new BSTNode(t));
        return getLeft();
    }

    @Override
    public BSTNode<T> getRight() {
        return (BSTNode<T>) super.getRight();
    }

    @Override
    public BSTNode<T> getLeft() {
        return (BSTNode<T>) super.getLeft();
    }

    @Override
    public BSTNode<T> getParent() {
        return (BSTNode<T>) super.getParent();
    }

    /**
     * The minimum element in the subtree rooted at this node.
     *
     * @return The minimum element in the subtree rooted at this node.
     */
    public BSTNode min() {
        if (!hasLeft()) return this;
        return getLeft().min();
    }

    /**
     * The Maximum element in the subtree rooted at this node.
     *
     * @return The maximum element in the subtree rooted at this node.
     */
    public BSTNode max() {
        BSTNode n = this;
        while (n.hasRight()) n = n.getRight();
        return n;
    }

    /**
     * The smallest element in the tree greater than this.
     */
    public BSTNode successor() {
        if (hasRight()) return getRight().min();
        return null;

    }

    /**
     * The greatest element in the tree less than this.
     *
     * @return
     */
    public BSTNode predecessor() {
        if (hasLeft()) return getLeft().max();
        return null;
    }

    /**
     * Inserts a key into the tree.
     *
     * @param t The key to be inserted.
     * @return The new node created that has key t.
     */
    public final BSTNode<T> insert(T t) {
        int compare = t.compareTo(getKey());
        
        if (compare < 0) {
            if (hasLeft()) return getLeft().insert(t);
            else return setLeft(t);
        
        } else if(compare > 0){
            if (hasRight()) return getRight().insert(t);
            else return setRight(t);
        
        }else return this;
    }

    /**
     * If there is only one child, that child is returned.
     *
     * @return A child if there is one, otherwise null.
     */
    public BSTNode aChild() {
        return hasLeft() ? getLeft() : getRight();
    }

    /**
     * Replace this node with its successor.  This children of this node will 
     * become the children of the successor and the parent of this node, the 
     * parent of the successor. Meanwhile the successor is cleanly removed
     * from the tree with its right child taking its place.
     * @param suc The unique successor to this node.
     * @param return this.
     */
    protected BSTNode<T> replaceWithSuccessor(BSTNode<T> suc) {
        suc.transplant(suc.getRight());
        setKey(suc.getKey());
        return this;
    }

    /**
     * Deletes a node from the tree.
     */
    public void delete() {
        if (hasOneChildOrLess()) transplant(aChild());
        else replaceWithSuccessor(successor());
    }
    
    /**
     * Deletes the node containing t.
     * @param t The element to whose node is to be deleted.
     * @return The node that replaced the deleted node.
     */
    public void delete(T t){
        find(t).delete();
    }

    @Override
    public BSTNode<T> find(T t) {
        int compare = t.compareTo(getKey());
        if(compare < 0 && hasLeft()) return getLeft().find(t);
        if(compare > 0 && hasRight()) return getRight().find(t);
        if(compare == 0) return this;
        return null;
    }
    
    
}
