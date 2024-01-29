package CodeWrittenWithStudents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

/**
 *
 * @author dovne
 */
public class AnonymousClasssExample {

    
    public static void main(String[] args) {
        
        List<Integer> list = new ArrayList<>(Arrays.asList(4, 7, -3));
        
        BiFunction<Double, Double, Double> f = (a, b) -> {
            if(a < 0) return a + b;
            return a;
        };
        
        System.out.println(f.apply(3.0, 7.0));
        
        
        
    }
    
}
