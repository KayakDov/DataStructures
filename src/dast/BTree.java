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
        return children == null || children[0] == null;
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
     * @param childIndex The index of the child to recieve the key.
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
        if (getNumKeys() >= keys.length) return splitRoot().insert(key);
        if (getNumKeys() == 0) {
            keys[0] = key;
            numKeys++;
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
     * merges children at indicis i and i + 1, and moves key[i] into that set of
     * children.
     *
     * @param i The index of the first child to be merged.
     */
    private BTree<T> mergeChild(int i) {
        BTree<T> mergeChild = new BTree<>(keys[0].getClass(), keys.length);
        BTree<T> left = children[i], right = children[i + 1];
        System.arraycopy(left.keys, 0, mergeChild.keys, 0, left.getNumKeys());
        mergeChild.keys[left.getNumKeys()] = keys[i];
        System.arraycopy(right.keys, 0, mergeChild.keys, left.getNumKeys() + 1,
                right.getNumKeys());
        mergeChild.numKeys = left.numKeys + right.numKeys + 1;
        MyArray.delete(i, getNumKeys(), keys);
        MyArray.delete(i + 1, getNumKeys() + 1, children);
        children[i] = mergeChild;
        if (!left.isLeaf()) {
            mergeChild.openChildren();
            System.arraycopy(left.children, 0, mergeChild.children, 0,
                    left.getNumKeys() + 1);
            System.arraycopy(right.children, 0, mergeChild.children,
                    left.getNumKeys() + 1, right.getNumKeys() + 1);
        }
        numKeys--;
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
        return childIndex >= 0 && childIndex < numKeys + 1
                && !isLeaf() && children[childIndex].hasKeyToGive();
    }
    
    /**
     * Can this node delete a key and still be large enough?
     * @return True if yes, false otherwise.
     */
    private boolean hasKeyToGive(){
        return getNumKeys() > (keys.length - 1) / 2;
    }

    /**
     * Deletes the first key and its left child.
     */
    private void deleteFirst() {
        MyArray.delete(0, numKeys, keys);
        if(!isLeaf()) MyArray.delete(0, numKeys + 1, children);
        numKeys--;
    }

    /**
     * Deletes the last child.
     */
    private void deleteLast() {
        keys[getNumKeys() - 1] = null;
        if(!isLeaf())children[getNumKeys()] = null;
        numKeys--;
    }

    /**
     * Inserts a key and its child to the begining of this array.
     *
     * @param key The key to be inserted.
     * @param child A tree of elements less than the key.
     */
    private void insertFirst(T key, BTree<T> child) {
        MyArray.insert(0, numKeys, key, keys);
        MyArray.insert(0, numKeys + 1, child, children);
        numKeys++;
    }

    /**
     * Appends a key and its children to the end of this array.
     *
     * @param key The key to be appended.
     * @param child A subtree of elements greater than the appended key.
     */
    private void insertLast(T key, BTree<T> child) {
        keys[numKeys] = key;
        if(!isLeaf())children[numKeys + 1] = child;
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
        BTree<T> grandChGift = donator.isLeaf()? null: 
                donator.children[left ? donator.numKeys + 1 : 0];

        if (left) reciever.insertFirst(keys[keyIndex], grandChGift);
        else reciever.insertLast(keys[keyIndex], grandChGift);
        
        keys[keyIndex] = donator.keys[left ? donator.getNumKeys() : 0];
        
        if(left) donator.deleteLast();
        else donator.deleteFirst();
    }

    /**
     * To delete a key that's not present in this node's list of keys.
     *
     * @param key The key to be deleted.
     * @param i The index of the child that should contain the key.
     * @return The root of this tree.
     */
    private BTree<T> deleteKeyNotHere(int i, T key) {
        if (isLeaf()) return this;
        if (!hasKeyToGive(i)) {
            if (hasKeyToGive(i + 1)) rotateKey(i, false);
            else if (hasKeyToGive(i - 1)) rotateKey(i, true);
            else mergeChild(i == getNumKeys() ? --i : i);
        }
        children[i].delete(key);
        return numKeys > 0 ? this : children[0];
    }

    
    
    /**
     * Deletes a key from an internal node.
     *
     * @param i The index of the key to be delted.
     */
    private void deleteInternalNodeKey(int i) {
        if (hasKeyToGive(i)) {
            T predecssor = children[i].keys[children[i].getNumKeys() - 1];
            children[i].delete(predecssor);
            keys[i] = predecssor;
        } else if (hasKeyToGive(i + 1)) {
            T successor = children[i + 1].keys[0];
            children[i + 1].delete(successor);
            keys[i] = successor;
        } else {
            T key = keys[i];
            mergeChild(i).delete(key);
        }
    }

    /**
     * Deletes a bunch of keys.
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
     * Splits the grandchildren around the median grandchild of the given child
     * index.
     *
     * @param childIndex The index of the child whose grandchildren need to be
     * split.
     */
    private void splitGrandChildren(int childIndex) {
        BTree<T> child = children[childIndex];
        MyArray<BTree<T>> grandChildKeys = MyArray.split(
                child.children, (child.getNumKeys() + 1) / 2);
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
        MyArray<T> childKeys = MyArray.splitAround(
                child.keys, child.getNumKeys() / 2);

        MyArray.insert(
                childIndex, 
                numKeys++, 
                child.keys[child.getNumKeys()/ 2], 
                keys);
        MyArray.insert(
                childIndex, 
                getNumKeys(), 
                new BTree<>(childKeys.left),
                children);
        
        children[childIndex + 1] = new BTree<>(childKeys.right);

        if (child.children != null) splitGrandChildren(childIndex);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(keys).filter(key -> key != null).forEach(key -> sb.append(key).append(" "));
        sb.append("\n");
        if (children != null)
            for (BTree child : children)
                if (child != null){
                    sb.append("|");
                    Arrays.stream(child.keys).filter(key -> key != null).forEach(key -> sb.append(key).append(" "));
                }
        return sb.toString();
    }

    /**
     * A sample tree for testing
     *
     * @return A sample tree for testing
     */
    private static BTree<Integer> sampleTreeForTesting() {
        BTree<Integer> testTree = new BTree<>(4, 8, 16, null);
        testTree.children[0] = new BTree<>(1, 2, 3, null);
        testTree.children[1] = new BTree<>(5, 6, 7, null);
        testTree.children[2] = new BTree<>(10, 12, 14, null);
        testTree.children[3] = new BTree<>(17, 18, 19, null);

        testTree.children[2].children[0]
                = new BTree<>(9, null, null, null);
        testTree.children[2].children[1]
                = new BTree<>(11, null, null, null);
        testTree.children[2].children[2]
                = new BTree<>(13, null, null, null);
        testTree.children[2].children[3]
                = new BTree<>(15, null, null, null);

        return testTree;
    }

    /**
     * Tests the splitChild method.
     */
    public static void testSplit() {

        BTree<Integer> testTree = sampleTreeForTesting();

        testTree.splitChild(2);

        System.out.println(testTree);//4, 8, 12, 16 \n 123 567 10 14 171819
        System.out.println(testTree.children[2]);//10 \n 9 11
        System.out.println(testTree.children[3]);//14 \n 13 15
    }

    /**
     * Tests the insert function.
     */
    private static void test() {
        BTree<Integer> testTree = new BTree<>(null, null, null);

        testTree = testTree.insert(4, 30, 15, 20, 25);
        
        testTree = testTree.delete(20, 25, 30);
        
        System.out.println(testTree.toString());
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
