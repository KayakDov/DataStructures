package tools;

import java.util.Arrays;

/**
 * This class splits an array.
 * @author Dov Neimand
 * @param <T> The type of element in the array.
 */
public class SplitArray<T> {
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
    private SplitArray(T[] left, T[] right) {
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
     * @return A new array with the same length as src, but with all traling 
     * values of the sub array set to null.
     */
    private static <T> T[] createSub(T[] src, int start, int length){
        T[] sub = Arrays.copyOf(src, src.length);
        if(start != 0) 
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
     * @return A SplitArray that is src split in two arrays.
     */
    public static <T> SplitArray<T> halfs(T[] src, int splitAt) {
        int leftLength = splitAt,
                rightLength = src.length - leftLength;
        return new SplitArray<>(
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
    public static <T> SplitArray<T> around(T[] src, int removeInd){
        int leftLength = removeInd,
                rightLength = src.length - leftLength - 1;
        return new SplitArray<>(
                createSub(src, 0, leftLength), 
                createSub(src, leftLength + 1, rightLength)
        );
    }

    @Override
    public String toString() {
        return Arrays.toString(left) + "\n" + Arrays.toString(right);
    }
    
    /**
     * Tests this class.
     * @param args 
     */
    public static void main(String[] args) {
        Character[] testArray = new Character[]{'a', 'b', 'c'};
        System.out.println(SplitArray.halfs(testArray, 1));
        System.out.println(SplitArray.around(testArray, 1));
        
        testArray = new Character[]{'a', 'b', 'c', 'd', 'e', 'f'};
        System.out.println(SplitArray.halfs(testArray, 3));
        System.out.println(SplitArray.around(testArray, 3));
    }
}
