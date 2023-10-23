package DAST;

import java.util.Arrays;

/**
 * Implements heapSort as described in Cormen chapter 6.
 *
 * @author Dov Neimand
 */
public class Heap<T extends Comparable<T>> {

    /**
     * Used to store the elements in the heap.
     */
    protected T[] at;

    /**
     * Used to store the number of elements in the heap, which may not include
     * elements at the end of the array.
     */
    protected int heapSize;

    
    
    /**
     * The constructor.
     *
     * @param arrayOfComparables An array from which a heap can be created.
     */
    public Heap(T... arrayOfComparables) {
        this.at = arrayOfComparables;
        this.heapSize = arrayOfComparables.length;
        isHeap = false;
    }
    

    /**
     * Finds the index of the left child of the given parent index.
     *
     * @param parent the index of the parent. This should be non negative.
     * @return The index of the left child.
     */
    protected final static int left(int parent) { //Note, by making the method final static primitve, we ensure the compiler will implement it inline.
        return (parent << 1) + 1; //2*i + 1;
    }

    /**
     * Finds the index of the right child of the given parent index.
     *
     * @param parent The parent index. This should be nonnegative.
     * @return The index of the right child of the given parent.
     */
    protected final static int right(int parent) {
        return (parent + 1) << 1; //2*i+2;
    }

    /**
     * Swaps two elements in the array.
     *
     * @param i An index of an element in the array. Must be non negative.
     * @param j An index of an element in the array. Must be non negative.
     */
    protected void swap(int i, int j) {
        T temp = at[i];
        at[i] = at[j];
        at[j] = temp;
    }

    /**
     * Gives the index of the parent of the proffered child index.
     *
     * @param child The index of the child. This should be positive.
     * @return The index of the parent.
     */
    protected final static int parent(int child) {
        return child / 2;
    }

    /**
     * Checks if the element is in the array and not null;
     *
     * @param i the index to be inspected.
     * @return true if the element is in the array and not null, false
     * otherwise.
     */
    protected boolean available(int i) {
        return i >= 0 && i < heapSize && at[i] != null;
    }

    /**
     * The maximum of the elements at the given indices.
     *
     * @param i The index of the first element.
     * @param j The index of the second element.
     * @return The maximum of the two elements.If neither element is available,
     * then an arbitrary element is returned. If one element is unavailable, the
     * other element is returned.
     */
    private int max(int i, int j) {
        if (!available(i)) return j;
        if (!available(j)) return i;
        return at[i].compareTo(at[j]) > 0 ? i : j;
    }

    /**
     * Finds the maximum element of this element and its children.
     *
     * @param i An index to be compared to its children to find the maximum
     * element of the three.
     * @return The maximum element of this element and its children.
     */
    private int maxFamily(int i) {
        return max(i, max(left(i), right(i)));
    }

    /**
     * Ensures that the sub tree rooted at i is a heap, while assuming that the
     * children of i are heaps.
     *
     * @param i the index of the root of the subtree to be converted to a heap.
     */
    protected void maxHeapify(int i) {
        if (i >= heapSize / 2) return;

        int max = maxFamily(i);

        if (i != max) {
            swap(i, max);
            maxHeapify(max);
        }
    }

    /**
     * Turns the unsorted array into a heap.
     */
    protected void buildMaxHeap() {
        if (!isHeap) for (int i = at.length / 2; i >= 0; i--)
            maxHeapify(i);
        
        isHeap = true;
    }

    /**
     * Sorts the array using the heapSort algorithm.
     */
    public Heap<T> heapSort() {
        buildMaxHeap();
        while (heapSize > 0) {
            swap(0, --heapSize);
            maxHeapify(0);
        }
        isHeap = false;
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(at);
    }

    /**
     * Is this a heap? True if this instance has undergone heapify, and not lost
     * its heap states since then.
     */
    private boolean isHeap;

    /**
     * Is this a heap?
     *
     * @return True if this instance has undergone heapify, and not lost its
     * heap states since then.
     */
    public boolean isHeap() {
        return isHeap;
    }

    /**
     * Is the heap empty?
     * @return True of the heap is empty, false otherwise.
     */
    public boolean heapIsEmpty(){
        return heapSize == 0;
    }
    
    /**
     * Sets the size of the heap to 0.
     */
    public void emptyHeap(){
        heapSize = 0;
    }
    
    /**
     * Tests some of the methods.
     * @param args 
     */
    public static void main(String[] args) {

        System.out.println(new Heap(5, 1, 8, -7, 23).heapSort());
    }

}
