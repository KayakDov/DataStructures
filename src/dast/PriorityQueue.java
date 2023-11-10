
package DAST;

import java.util.Arrays;

/**
 * Implements a heap.  See Cormen chapter 6.
 * @author Dov Neimand
 */
public class PriorityQueue<T extends Comparable<T>> extends Heap<T>{

    
    /**
     * Creates an empty priority queue with the given maximum size.
     * @param maxSize The maximum size of the queue.
     */
    public PriorityQueue(T[] array){
        super(array);
        buildMaxHeap();
    }
    
    /**
     * 
     * @return The maximum element of the queue
     */
    public T maxElement(){
        return at[0];
    }
    
    /**
     * Extracts the maximum element of the queue
     * @return The maximum element of the queue.  If the queue is empty, this 
     * method will return null.
     */
    public T extractMax(){
        if(heapIsEmpty()) return null;
        T max = maxElement();
        at[0] = null;
        maxHeapify(0);
        heapSize--;
        return max;
    }
    
    /**
     * Notifies the tree that an element has an increased key.
     * @param elementIndex The index of the element whose relative value 
     * (ordering) has been changed.
     */
    public void notificationOfIncreasedKey(int elementIndex){
        while(available(parent(elementIndex)) && 
                at[parent(elementIndex)].compareTo(at[elementIndex]) < 0){
            swap(elementIndex, parent(elementIndex));
            elementIndex =  parent(elementIndex);
        }
    }
    
    
    /**
     * Inserts a new element into the priority queue.
     * @param element The element to be inserted.
     */
    public void insert(T element){
        at[heapSize++] = element;
        notificationOfIncreasedKey(heapSize - 1);
    }
    
    /**
     * Tests some of the methods.
     * @param args 
     */
    public static void main(String[] args) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Integer[5]);
        queue.emptyHeap();
        
        queue.insert(3);
        
        queue.insert(5);
        
        queue.insert(1);
        
        queue.insert(7);

        queue.insert(-5);
        System.out.println(Arrays.toString(queue.at));
        
        while(!queue.heapIsEmpty())
            System.out.println(queue.extractMax());
        
    }
}