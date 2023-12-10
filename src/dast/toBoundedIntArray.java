package dast;

import java.util.function.Function;

/**
 * An invertible function from some domain T to a range of int arrays.  It is
 * required that there exists a single and defined upper bound on all the 
 * integer values in  the range's arrays.
 * @author Dov Neimand
 * @param <T> The elements in the domain of the function.
 */
public interface toBoundedIntArray<T> extends Function<T, int[]> {

    /**
     * The inverse of the function.
     * @param array a value in the range of this ToTrie,
     * @return A value in the domain of this toTrie that is mapped by apply to 
     * the array.
     */
    public T inverse(int[] array);

    /**
     * The upper bound on the elements of the range.
     * @return The upper bound on the elements of the range.
     */
    public int getUpperBound();
    
    /**
     * Creates an invertible function.
     * @param <T> The domain of the function.
     * @param <K> The range of the function.
     * @param f The function.
     * @param fInverse The inverse of f.  It is on the caller to verify that
     * this is in fact an inverse.
     * @param upperBound The upper bound on the integers in the arrays in f's range.
     * @return An invertible function constructed from f and its inverse.
     */
    public static <T> toBoundedIntArray<T> get(Function<T, int[]> f,
            Function<int[], T> fInverse, int upperBound) {
        return new toBoundedIntArray<T>() {
            @Override
            public T inverse(int[] k) {
                return fInverse.apply(k);
            }

            @Override
            public int[] apply(T t) {
                return f.apply(t);
            }

            @Override
            public int getUpperBound() {
                return upperBound;
            }
        };
    }
}

