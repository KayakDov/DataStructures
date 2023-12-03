package dast;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A node for a red black search tree. See Cormen for details.
 *
 * @author Dov Neimand
 * @param <T> The type of element held in the node.
 */
public class RBNode<T extends Comparable<T>> extends BSTNode<T> {

    private boolean isRed;

    /**
     * The constructor.
     *
     * @param key The element to be stored in this node.
     */
    public RBNode(T key) {
        super(key);
        isRed = false;
    }

    @Override
    public RBNode<T> getLeft() {
        return (RBNode<T>) super.getLeft();
    }

    @Override
    public RBNode<T> getRight() {
        return (RBNode<T>) super.getRight();
    }

    @Override
    public RBNode<T> getParent() {
        return (RBNode<T>) super.getParent();
    }

    @Override
    public RBNode<T> setLeft(T t) {
        super.setLeft(new RBNode<>(t));
        getLeft().setRed();
        return getLeft();

    }

    @Override
    public RBNode<T> setRight(T t) {
        super.setRight(new RBNode<>(t));
        getRight().setRed();
        return getRight();
    }

    @Override
    public RBNode<T> grandParent() {
        return (RBNode<T>) super.grandParent();
    }

    @Override
    public RBNode<T> sibling() {
        return (RBNode<T>) super.sibling();
    }

    private boolean isRed() {
        return isRed;
    }

    private boolean isBlack() {
        return !isRed();
    }

    @Override
    public RBNode<T> farNephew() {
        return (RBNode<T>) super.farNephew();
    }

    @Override
    public RBNode<T> nearNephew() {
        return (RBNode<T>) super.nearNephew();
    }

    /**
     * Sets the color to red.
     */
    private void setRed() {
        this.isRed = true;
    }

    /**
     * sets the color to black.
     */
    private void setBlack() {
        isRed = false;
    }

    /**
     * Performs a right rotation. See page 313 of Cormen for details. This node
     * moves down one generation and the left child moves up one generation.
     */
    private void rightRotate() {
        if (!hasLeft()) return;

        RBNode<T> tempPrevTop = new RBNode<>(getKey()),
                tempGrandChild = getLeft().getRight();
        tempPrevTop.isRed = isRed();

        setKey(getLeft().getKey());
        isRed = getLeft().isRed();

        setLeft(getLeft().getLeft());
        tempPrevTop.setRight(getRight());
        tempPrevTop.setLeft(tempGrandChild);
        setRight(tempPrevTop);
    }

    /**
     * Performs a left rotation. See page 313 of Cormen for details. This node
     * moves down one generation and the right node moves up one generation.
     */
    private void leftRotate() {
        if (!hasRight()) return;
        RBNode<T> tempPrevTop = new RBNode<>(getKey()),
                tempGrandChild = getRight().getLeft();
        tempPrevTop.isRed = isRed();

        setKey(getRight().getKey());
        isRed = getRight().isRed();

        setRight(getRight().getRight());
        tempPrevTop.setLeft(getLeft());
        tempPrevTop.setRight(tempGrandChild);
        setLeft(tempPrevTop);
    }

    /**
     * Rotates this node up.
     *
     * @return the node that ocupies the space this node used to ocupy.
     */
    private RBNode<T> rotateUp() {
        if (isRoot()) return this;
        if (isLeft()) {
            getParent().rightRotate();
            return getRight();
        }
        getParent().leftRotate();
        return getLeft();
    }

    /**
     * Inserts a new red/black node to the tree. Be sure to use this method
     * instead of insert, since that will not maintain the tree.
     *
     * @param t The element to be added to the tree.
     * @return The node containing the added element.
     */
    public RBNode RBInsert(T t) {
        ((RBNode) insert(t)).insertFixUp();
        return this;
    }

    /**
     * Is this node the bottom of a triangle?
     *
     * @return True if this node is the bottom of a triangle, also called an
     * inner node, false otherwise.
     */
    private boolean isTriangle() {
        if (hasGrandParent())
            return isLeft() && getParent().isRight() || isRight()
                    && getParent().isLeft();
        return false;
    }

    /**
     * Changes the color of this node.
     */
    private void flipColor() {
        isRed = !isRed;
    }

    /**
     * Flips the colors of all the proffered nodes.
     *
     * @param nodes A set of node to have their colors changed. Note: The
     * proffered node must be an RB Node.
     */
    private static void flipColors(BinaryNode... nodes) {
        Arrays.stream(nodes).filter(n -> n != null).forEach(n -> ((RBNode) n).flipColor());
    }

    /**
     * To be called after an insertion was made. This method fixes the trees
     * colors using rotations where necessary to maintain properties.
     */
    private void insertFixUp() {
        if (isRoot()) setBlack();
        else if (getParent().isRed())
            if (hasUnlce() && ((RBNode) uncle()).isRed()) {
                flipColors(getParent(), uncle(), grandParent());
                grandParent().insertFixUp();
            } else {
                if (isTriangle()) rotateUp().insertFixUp();
                else {
                    flipColors(getParent(), grandParent());
                    getParent().rotateUp();
                }

            }

    }

    
    
    /**
     * Deletes the node containing t.
     */
    @Override
    public void delete() {
        boolean origIsRed = isRed();
        RBNode<T> replacementNode, successor = null;
        if (hasOneChildOrLess())
            transplant(replacementNode = (RBNode<T>) aChild());
        else {
            origIsRed = (successor = (RBNode<T>) successor()).isRed();

            if (!successor.hasRight()) successor.setRight((T) null);

            replacementNode = successor.getRight();

            replaceWithSuccessor(successor);
        }

        if (!origIsRed && replacementNode != null) {
            replacementNode.deleteFixUp();
            if (replacementNode.getKey() == null){
                replacementNode.deleteSubTree();
            }
        }
    }

    /**
     *
     * @param nodes The node whose color is to be checked.
     * @return True if the node is black, false otherwise.
     */
    private static boolean isBlack(RBNode... nodes) {
        return Arrays.stream(nodes).allMatch(n -> n == null || n.isBlack());
    }

    /**
     * Sets the proffered nodes to be black.
     *
     * @param nodes The nodes whose color should be set to black.
     */
    private void setBlack(RBNode<T>... nodes) {
        Arrays.stream(nodes).filter(n -> n != null)
                .forEach(node -> node.setBlack());
    }

    /**
     * What to do when a deletion was made, and the node of interest has a red
     * sibling.
     */
    private void delFixRedSibling() {
        sibling().setBlack();
        getParent().setRed();
        sibling().rotateUp();
    }

    /**
     * To fix a deletion where both nephews are black.
     */
    private void delFixBlackNephews() {
        sibling().setRed();
        getParent().deleteFixUp();
    }

    /**
     * To be called as part of delete fix up, when just the far nephew is black.
     */
    private void delFixJustFarNephewBlack() {
        sibling().nearNephew().setBlack();
        sibling().setRed();
        sibling().nearNephew().rotateUp();
    }

    /**
     * To be called to fix deletions when the node of interest has a far nephew
     * that's red.
     */
    private void delFixFarNephewRed() {
        sibling().isRed = getParent().isRed();
        setBlack(getParent(), farNephew());
        sibling().rotateUp();
    }

    /**
     * Corrects mistakes that may have been introduced during deletion.
     */
    private void deleteFixUp() {
        if (isRoot() || isRed()) {
            setBlack();
            return;
        }

        if (sibling().isRed()) delFixRedSibling();

        if (isBlack(nearNephew(), farNephew())) delFixBlackNephews();

        else {
            if (isBlack(farNephew())) delFixJustFarNephewBlack();
            delFixFarNephewRed();
        }

    }

    /**
     * some simple tests for the node.
     * @param args Not used.
     */
    public static void main(String[] args) {
        RBNode<Integer> root = new RBNode<>(5);
        for (int i = 0; i > -10; i--)
            root.RBInsert(i);

        root.delete(-6);

        Integer[] array = new Integer[20];

        System.out.println(Arrays.toString(root.fillArray(array)));
        root.inOrderTreeWalk().forEachOrdered(n -> {
            if(((RBNode)n).isRed()) System.err.print(n.getKey() + " ");
            else System.out.print(n.getKey() + " ");
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(RBNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
