package dast;

import java.util.function.Consumer;

/**
 *
 * @author Dov Neimand
 */
public class ComplexityExamples {

    public static void example1(long n){
        for(long i = 0; i < n; i++);
    }
    public static void example2(long n){
        for(long i = -10; i < n; i += 3);
    }    
    public static void example3(long n, int m){
        for(int i = 0; i < n; i++)
            for(long j = 0; j < m; j++);
    }
    public static void example4(long n, int m){
        for(long i = 0; i < n; i++)
            for(long j = 0; j < m; j++);
    }
    
    public static double testTime(Consumer<Long> example, long n){
        long startTime = System.nanoTime();
        example.accept(n);
        long endTime = System.nanoTime();
        return (double)(endTime - startTime)/1e9;
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(int i = 1; i <= 10; i++)
            System.out.println(testTime(ComplexityExamples::example1, i*(long)1e9));
    }
    
}
