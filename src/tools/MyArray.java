package tools;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This class splits an array.
 * @author Dov Neimand
 * @param <T> The type of element in the array.
 */
public class MyArray<T> {
    /**
     * The first half of the proffered array.
     */
    public final T[] left;
    /**
     * The second half of the proffered array.
     */
    public final T[] right;
    
    /**
     * Creates a split array.
     * @param left The first half of the array.
     * @param right The second half of the array.
     */
    private MyArray(T[] left, T[] right) {
        this.left = left;
        this.right = right;
    }
    
    
    
    /**
     * Creates a sub array from src. The length of the sub array is the length
     * of src, but the first value of the subarray is 0 and trailing values are
     * null.
     * @param src The array from which the sub array is taken.
     * @param start The starting index of the sub array, inclusive.
     * @param end The end index of the sub array exclusive.
     * @return A new array with the same length as src, but with all trailing 
     * values of the sub array set to null. 
     */
    private static <T> T[] createSub(T[] src, int start, int length){
        T[] sub = (T[])Array.newInstance(src[0].getClass(), src.length);
        System.arraycopy(src, start, sub, 0, length);
        Arrays.fill(sub, length, sub.length, null);
        return sub;
    }
    
    /**
     * The constructor.
     * @param <T> The type of the content of the array being split.
     * @param src Two arrays will be generated from this array.
     * The first a copy of the first half of needs splitting and the second 
     * a copy of the second half of needsSplitting.  Both arrays will be the
     * same length as needsSpllitng with their 2nd half's null.
     * @param splitAt The index the array is split at
     * @return A SplitArray that is src split in two arrays.  If src is null
     * then the split of src will be two null arrays.
     */
    public static <T> MyArray<T> split(T[] src, int splitAt) {
        
        int leftLength = splitAt,
                rightLength = src.length - leftLength;
        return new MyArray<>(
                createSub(src, 0, leftLength), 
                createSub(src, leftLength, rightLength)
        );
    }
    
    /**
     * Slits the array around the middle value, which is not included in either 
     * half.
     * @param <T> The type in the array being split.
     * @param src The array to be split.
     * @param removeInd the index to be removed, around which the array is split.
     * @return A Split Array without the middle element from src.
     */
    public static <T> MyArray<T> splitAround(T[] src, int removeInd){
        int leftLength = removeInd,
                rightLength = src.length - leftLength - 1;
        return new MyArray<>(
                createSub(src, 0, leftLength), 
                createSub(src, leftLength + 1, rightLength)
        );
    }

    @Override
    public String toString() {
        return Arrays.toString(left) + "\n" + Arrays.toString(right);
    }
    
    
    /**
     * Deletes the ith element from the proffered array
     *
     * @param <K> The contents of the array.
     * @param i The index whos element is to be removed.
     * @param lastIndex the number of elements in the array. All elements from
     * the last index are null.
     * @param array The array from whom the ith element is to be deleted.
     */
    public static <K> void delete(int i, int lastIndex, K[] array) {
        System.arraycopy(array, i + 1, array, i, lastIndex - i - 1);
        array[lastIndex - 1] = null;
    }
    
    /**
     * Inserts a value into the given array.
     * @param <K> The type of the array.
     * @param index The index the element is to be inserted to.
     * @param lastIndex The index of the last element of the array.  After this
     * all elements should be null.
     * @param val The value to be inserted at the given index.
     * @param array The array to receive the insertion.
     */
    public static <K> void insert(int index, int lastIndex, K val, K[] array){
        for(int i = lastIndex; i >= index; i--)
            array[i + 1] = array[i];
        array[index] = val;
    }
    
    /**
     * Tests this class.
     * @param args 
     */
    public static void main(String[] args) {
        Character[] testArray = new Character[]{'a', 'b', 'c'};
        System.out.println(MyArray.split(testArray, 1));
        System.out.println(MyArray.splitAround(testArray, 1));
        
        testArray = new Character[]{'a', 'b', 'c', 'd', 'e', 'f'};
        System.out.println(MyArray.split(testArray, 3));
        System.out.println(MyArray.splitAround(testArray, 3));
    }
}
