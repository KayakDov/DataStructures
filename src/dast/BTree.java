package dast;

import java.lang.reflect.Array;
import java.util.Arrays;
import tools.MyArray;

/**
 * B Tree Node
 *
 * @author Dov Neimand
 * @param <T> The type of key stored in the tree.
 */
public class BTree<T extends Comparable<T>> {

    private final T[] keys;
    private BTree<T>[] children;
    private int numKeys;

    public int getNumKeys() {
        return numKeys;
    }

    /**
     * The constructor
     *
     * @param keys The keys in this node. Node that all nodes in the tree should
     * have the same size keys array. If there are not enough keys to fill the
     * array, then the remaining elements should be null. For nodes other than
     * the root node, the array should be at least half full to ensure proper
     * BTree behavior.
     */
    public BTree(T... keys) {
        if (keys == null)
            throw new IllegalArgumentException("keys must not be null");
        this.keys = keys;
        numKeys = countKeys();
    }
    
    /**
     * An empty BTree.
     * @param numKeys The number of keys at each level.
     * @param cl The type of element that will be stored in the tree.
     */
    public BTree(int numKeys, Class cl){
        keys = (T[])Array.newInstance(cl, numKeys);
        this.numKeys = 0;
    }

    /**
     * Counts the number of keys in the keys array.
     *
     * @return The number of keys in the keys array.
     */
    private int countKeys() {
        int i = 0;
        while (i < keys.length && keys[i] != null)
            i++;
        return i;
    }

    /**
     * Is this node a leaf?
     *
     * @return True if this node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return children == null;
    }

    /**
     * A lightweight class containing information about the location of a key in
     * the tree.
     */
    public class KeyLocation {

        /**
         * The node containing the desired key.
         */
        public final BTree<T> node;
        /**
         * The index of the desired key in the node.
         */
        public final int index;

        /**
         * The constructor.
         *
         * @param node The node the key is in.
         * @param index The index of the desired key.
         */
        public KeyLocation(BTree<T> node, int index) {
            this.node = node;
            this.index = index;
        }
    }

    /**
     * Finds the node containing the proffered key, and the index of that key
     * within the node.
     *
     * @param key The key whose node is sought.
     * @return The node, and index within that node, containing the proffered
     * key.
     */
    private KeyLocation search(T key) {
        int index = indexOf(key);
        if (index >= 0)
            return new KeyLocation(this, index);
        if (!isLeaf())
            return children[-index - 1].search(key);
        else return new KeyLocation(null, -1);
    }

    /**
     * Is this node full? Can a new key be inserted here?
     *
     * @return True if this node is full, false otherwise.
     */
    private boolean isFull() {
        return getNumKeys() == keys.length;
    }

    /**
     * Inserts all the keys provided.
     *
     * @param keys The keys to be inserted to the tree.
     * @return The new root of the tree.
     */
    public BTree<T> insert(T... keys) {
        BTree<T> root = this;
        for (T key : keys)
            root = root.insert(key);
        return root;
    }

    /**
     * Creates a parent node for this node, then splits this node.
     *
     * @return The parent node.
     */
    private BTree<T> splitRoot() {
        BTree<T> root = new BTree<>(keys[0].getClass(), keys.length);
        root.openChildren();
        root.children[0] = this;
        root.splitChild(0);
        return root;
    }

    /**
     * Finds where this key would be inserted. If this is a leaf, inserts a key
     * into this node's keys.
     *
     * @param key The key to be inserted.
     * @return The index of where the insertion would be.
     */
    private int shiftToIndex(T key) {
        int i;
        for (i = getNumKeys(); i > 0 && keys[i - 1].compareTo(key) > 0; i--)
            if (isLeaf()) keys[i] = keys[i - 1];
        return i;
    }

    /**
     * Inserts the key to the child with the proffered index.
     *
     * @param key The key to be inserted.
     * @param childIndex The index of the child to receive the key.
     * @return The root node of the entire tree.
     */
    private void insertInChild(T key, int childIndex) {

        if (children[childIndex].isFull()) {
            splitChild(childIndex);
            if (keys[childIndex].compareTo(key) < 0) {
                children[childIndex + 1].insert(key);
                return;
            }
        }
        children[childIndex].insert(key);
    }

    /**
     * Inserts the proffered key into this tree.
     *
     * @param key The key to be inserted.
     * @return The root of the tree after the insert.
     */
    public BTree<T> insert(T key) {
        if (isFull()) {
            BTree<T> root = splitRoot();
            root.insert(key);
            return root;
        }
        if (getNumKeys() == 0) {
            keys[numKeys++] = key;
            return this;
        }

        if (isLeaf()) {
            keys[shiftToIndex(key)] = key;
            numKeys++;
        } else insertInChild(key, -indexOf(key) - 1);

        return this;
    }

    /**
     * Does a binary search for the desired element.
     *
     * @param key The key whose index is desired.
     * @return Same as Arrays.binarySearch, except null elements are ignored.
     */
    private int indexOf(T key) {
        return Arrays.binarySearch(keys, 0, getNumKeys(), key);
    }

    /**
     * A constructor
     *
     * @param componentType the type of elements in the tree.
     * @param length The number of keys in each node.
     */
    private BTree(Class<?> componentType, int length) {
        this((T[]) Array.newInstance(componentType, length));
    }

    /**
     * Opens up the children. Should not be called if children is not null.
     */
    private void openChildren() {
        children = (BTree<T>[]) Array.newInstance(getClass(),
                keys.length + 1);
    }

    /**
     * Merges keys and children in from the proffered tree to this tree.
     *
     * @param from The tree from which keys and children should be taken.
     */
    private void mergeIn(BTree<T> from) {
        System.arraycopy(from.keys, 0, keys, getNumKeys(), from.getNumKeys());
        if (!from.isLeaf()) {
            if(isLeaf())openChildren();
            System.arraycopy(from.children, 0, children,
                    getNumKeys(), from.getNumKeys() + 1);
        }
        numKeys += from.getNumKeys();
    }

    /**
     * merges children at indicis i and i + 1, and moves key[i] into that set of
     * children.
     *
     * @param i The index of the first child to be merged.
     */
    private BTree<T> mergeChild(int i) {
        BTree<T> mergeChild = new BTree<>(keys[0].getClass(), keys.length);

        mergeChild.mergeIn(children[i]);

        mergeChild.keys[mergeChild.numKeys++] = keys[i];

        mergeChild.mergeIn(children[i + 1]);

        delete(i, false);

        children[i] = mergeChild;

        return mergeChild;
    }

    /**
     * Does the child at childIndex have enough keys to give one away?
     *
     * @param childIndex The index of the child in question.
     * @return True if the child has enough keys to give one away, false
     * otherwise.
     */
    private boolean hasKeyToGive(int childIndex) {
        return childIndex >= 0 && childIndex < getNumKeys() + 1
                && !isLeaf() && children[childIndex] != null 
                &&children[childIndex].hasKeyToGive();
    }

    /**
     * Can this node delete a key and still be large enough?
     *
     * @return True if yes, false otherwise.
     */
    private boolean hasKeyToGive() {
        return getNumKeys() > (keys.length - 1) / 2;
    }

    /**
     * Deletes the first key and its left (or right) child.
     *
     * @param index the index of the node to be deleted.
     * @param leftGrandChild which grandchild to delete.
     */
    private void delete(int index, boolean leftGrandChild) {
        MyArray.delete(index, numKeys, keys);
        if (!isLeaf()) MyArray.delete(index + (leftGrandChild ? 0 : 1), numKeys
                    + 1, children);
        numKeys--;
    }

    /**
     * Deletes the last child.
     */
    private void deleteLast() {
        keys[getNumKeys() - 1] = null;
        if (!isLeaf()) children[getNumKeys()] = null;
        numKeys--;
    }

    /**
     * Inserts a key and its child to the begining of this array.
     *
     * @param key The key to be inserted.
     * @param child A tree of elements less than the key.
     */
    private void insert(T key, BTree<T> child, int index, boolean leftGrandChild) {
        MyArray.insert(index, numKeys, key, keys);
        MyArray.insert(index + (leftGrandChild ? 0 : 1), numKeys + 1, child,
                children);
        numKeys++;
    }

    /**
     * Appends a key and its children to the end of this array.
     *
     * @param key The key to be appended.
     * @param child A subtree of elements greater than the appended key.
     */
    private void append(T key, BTree<T> child) {
        keys[numKeys] = key;
        if (!isLeaf()) children[numKeys + 1] = child;
        numKeys++;
    }

    /**
     * Moves the min/max key from the right/left child up to index i / i - 1,
     * and moves the key at index i down to the ith child.
     *
     * @param index The index of the child in need of a key
     * @param left Is the donator on the left (true), or the right (false).
     */
    private void rotateKey(int index, boolean left) {
        BTree<T> reciever = children[index],
                donator = children[index + (left ? -1 : 1)];
        int keyIndex = left ? index - 1 : index;
        BTree<T> grandChGift = donator.isLeaf() ? null : donator.children[left
                ? donator.numKeys + 1 : 0];

        if (left) reciever.insert(keys[keyIndex], grandChGift, 0, true);
        else reciever.append(keys[keyIndex], grandChGift);

        keys[keyIndex] = donator.keys[left ? donator.getNumKeys() : 0];

        if (left) donator.deleteLast();
        else donator.delete(0, true);
    }

    /**
     * Should this node no longer be the root?
     *
     * @return True if this node should no longer be the root, false otherwise.
     */
    private boolean tossThis() {
        return numKeys == 0 && children != null && children[0] != null;
    }

    /**
     * To delete a key that's not present in keys.
     *
     * @param key The key to be deleted.
     * @param i The index of the child that should contain the key.
     * @return The root of this tree.
     */
    private BTree<T> deleteKeyNotHere(int i, T key) {
        if (isLeaf()) return this;
        if (!hasKeyToGive(i)) {
            if(children[i] == null) return this;
            if (hasKeyToGive(i + 1)) rotateKey(i, false);
            else if (hasKeyToGive(i - 1)) rotateKey(i, true);
            else mergeChild(i == getNumKeys() ? --i : i);
        }
        children[i].delete(key);
        return tossThis() ? children[0] : this;
    }

    /**
     * Takes a node from the proffered BTree and deletes the node in that tree.
     *
     * @param takeFromChild The child index from which the key is to be taken.
     * @param key The key to be taken.
     */
    private void takeFrom(int takeFromChild, int giveTo, T key) {
        children[takeFromChild].delete(key);
        keys[giveTo] = key;

    }

    /**
     * The last key in this node.
     *
     * @return The last key in this node.
     */
    private T lastKey() {
        return keys[getNumKeys() - 1];
    }

    /**
     * The first key in this node.
     *
     * @return The first key in this node.
     */
    private T firstKey() {
        return keys[0];
    }

    /**
     * Deletes a key present in this internal node.
     *
     * @param i The index of the key to be delted.
     */
    private void deleteInternalNodeKey(int i) {
        if (hasKeyToGive(i))
            takeFrom(i, i, children[i].lastKey());
        else if (hasKeyToGive(i + 1))
            takeFrom(i + 1, i, children[i + 1].firstKey());
        else {
            T key = keys[i];
            mergeChild(i).delete(key);
        }
    }

    /**
     * Deletes a bunch of keys from the subtree.
     *
     * @param keys the keys to be deleted
     * @return The root of the tree.
     */
    private BTree<T> delete(T... keys) {
        BTree<T> root = this;
        for (T key : keys)
            root = root.delete(key);
        return root;
    }

    /**
     * Deletes a key from this subtree.
     *
     * @param key The key to be deleted.
     * @return The root of the tree.
     */
    private BTree<T> delete(T key) {
        int i = indexOf(key);
        if (i < 0) return deleteKeyNotHere(-i - 1, key);
        if (isLeaf()) {
            MyArray.delete(i, getNumKeys(), keys);
            numKeys--;
        } else deleteInternalNodeKey(i);

        return this;
    }

    /**
     * Splits the grandchildren around the median child of the given child
     * index.
     *
     * @param childIndex The index of the child whose grandchildren need to be
     * split.
     */
    private void splitGrandChildren(int childIndex, BTree<T>[] grandChildren) {
        
        MyArray<BTree<T>> grandChildKeys = MyArray.split(grandChildren, children[childIndex].getNumKeys() + 1);
        children[childIndex].children = grandChildKeys.left;
        children[childIndex + 1].children = grandChildKeys.right;
    }

    /**
     * Splits this child at the given index, pulling the median element of the
     * child up into this node.
     *
     * @param childIndex The index of child to be split.
     */
    private void splitChild(int childIndex) {

        BTree<T> child = children[childIndex];
        BTree<T>[] grandChildren = child.children;
        MyArray<T> childKeys = MyArray.splitAround(
                child.keys, child.getNumKeys() / 2);

        MyArray.insert(
                childIndex,
                numKeys,
                child.keys[child.getNumKeys() / 2],
                keys);
        
        MyArray.insert(
                childIndex,
                ++numKeys,
                new BTree<>(childKeys.left),
                children);

        children[childIndex + 1] = new BTree<>(childKeys.right);

        if (!child.isLeaf()) splitGrandChildren(childIndex, grandChildren);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(keys).filter(key -> key != null).forEach(key -> sb.append(
                key).append(" "));
        sb.append("\n");
        if (!isLeaf()){
            for (BTree child : children)
                if (child != null) {
                    sb.append("|");
                    Arrays.stream(child.keys).filter(key -> key != null).
                            forEach(key -> sb.append(key).append(" "));
                }else sb.append("|null ");
            
            sb.append("\n\n");

            for(BTree child: children) 
                if(child != null)sb.append(child.toString()).append("\n");
                
        }
        
        return sb.toString();
    }

    /**
     * Tests the insert function.
     */
    private static void test() {
        BTree<Integer> testTree = new BTree<>(3, Integer.class);

        testTree = testTree.insert(4, 30, 15, 20, 25, -9, 100, -12, 99, 42, 8);

        testTree = testTree.delete(4, 20, 30);
        System.out.println(testTree.toString() + "\n");
//        for(BTree child: testTree.children) 
//            if(child != null)System.out.println(child.toString() + "\n");
        
    }

    /**
     * For testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        test();
    }
}
