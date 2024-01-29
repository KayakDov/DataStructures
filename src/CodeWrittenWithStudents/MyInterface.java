package CodeWrittenWithStudents;

/**
 * A class that does something.
 * @author Dov Neimand
 */
public interface MyInterface {
    public int doSomething(double x);
    
    public default void somethingElse(){
        System.out.println("What did you learn in OOP?!");
    }
    
    public default int doSoemthingTwice(double x){
        doSomething(x);
        return doSomething(x);
    }
    
}

