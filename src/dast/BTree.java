package dast;

import java.util.Arrays;
import tools.SplitArray;

/**
 * B Tree Node
 *
 * @author Dov Neimand
 * @param <T> The type of key stored in the tree.
 */
public class BTree<T extends Comparable<T>> {

    private final T[] keys;
    private final BTree<T>[] children;
    private int numKeys;

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
        this.keys = keys;
        children = new BTree[keys.length + 1];
        numKeys = countKeys();
    }

    /**
     * Counts the number of keys in the keys array.
     *
     * @return The number of keys in the keys array.
     */
    private int countKeys() {
        int i = 0;
        while (i < keys.length && keys[i] != null) i++;
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
        if (index >= 0)  return new KeyLocation(this, index);
        if (!isLeaf()) return children[-index - 1].search(key);
        else return null;
    }

    /**
     * Inserts the proffered key into this tree.
     *
     * @param key The key to be inserted.
     */
    private void insert(T key) {
        if (numKeys < keys.length) {
            int i = numKeys - 1;
            while (i > 0 && keys[i].compareTo(key) > 0) {
                keys[i] = keys[i + 1];
                i--;
            }
            keys[i] = key;

        }

    }

    /**
     * Does a binary search for the desired element.
     * @param key The key whos index is desired.
     * @return Same as Arrays.binarySearch, except null elements are ignored.
     */
    private int indexOf(T key){
        return Arrays.binarySearch(keys, 0, numKeys, key);
    }
    
    /**
     * Deletes a key from this node.
     *
     * @param key The key to be deleted.
     */
    private void delete(T key) {
        int i = indexOf(key);
        if (i >= 0) {
            System.arraycopy(keys, i + 1, keys, i, numKeys - i - 1);
            numKeys--;
        }

    }

    /**
     * A constructor
     * @param keys The keys of this node.
     * @param children The children of the keys of this node.
     */
    private BTree(T[] keys, BTree<T>[] children) {
        this.keys = keys;
        this.children = children;
        numKeys = countKeys();
    }
    
    /**
     * Splits this child at the given index, pulling the median element of the
     * child up into this node.
     *
     * @param childIndex The index of child to be split.
     */
    private void splitChild(int childIndex) {
        
        BTree<T> child = children[childIndex];
        SplitArray<T> childKeys = 
                SplitArray.around(child.keys, child.numKeys/2);
        SplitArray<BTree<T>> grandChildKeys = 
                SplitArray.halfs(child.children, (child.numKeys + 1)/2);
        
        BTree<T> leftOfMid = 
                new BTree<>(childKeys.left, grandChildKeys.left), 
                rightOfMid = 
                new BTree<>(childKeys.right, grandChildKeys.right);
        
        for(int i = numKeys++; i > childIndex; i--){
            keys[i] = keys[i - 1];
            children[i + 1] = children[i];
        }
        
        keys[childIndex] = child.keys[child.numKeys/2];
        children[childIndex] = leftOfMid;
        children[childIndex + 1] = rightOfMid;
    }

    @Override
    public String toString() {
        StringBuilder sb = 
                new StringBuilder(Arrays.toString(keys)).append("\n");
        for(BTree<T> child: children) if(child != null) 
            sb.append(Arrays.toString(child.keys)).append(" ");
        return sb.toString();
    }
    
    /**
     * A sample tree for testing
     * 
     * @return A sample tree for testing
     */
    private static BTree<Integer> sampleTreeForTesting(){
        BTree<Integer> testTree = new BTree<>(4, 8, 16, null);
        testTree.children[0] = new BTree<>(1, 2, 3, null);
        testTree.children[1] = new BTree<>(5, 6, 7, null);
        testTree.children[2] = new BTree<>(10, 12, 14, null);
        testTree.children[3] = new BTree<>(17, 18, 19, null);
        
        testTree.children[2].children[0] =  
                new BTree<>(9, null, null, null);
        testTree.children[2].children[1] =  
                new BTree<>(11, null, null, null);
        testTree.children[2].children[2] =  
                new BTree<>(13, null, null, null);
        testTree.children[2].children[3] =  
                new BTree<>(15, null, null, null);
        
        return testTree;
    }
    
    /**
     * For testing.
     * @param args 
     */
    public static void main(String[] args) {
        
        BTree<Integer> testTree = sampleTreeForTesting();
        
        testTree.splitChild(2);
        
        System.out.println(testTree);//4, 8, 12, 16 \n 123 567 10 14 171819
        System.out.println(testTree.children[2]);//10 \n 9 11
        System.out.println(testTree.children[3]);//14 \n 13 15

    }
}
