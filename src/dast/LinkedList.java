package dast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * A linked list skeleton.
 *
 * @author Dov Neimand
 * @param <T> The content of the list.
 */
public class LinkedList<T> implements Iterable<T>, List<T> {

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> next = first;

            @Override
            public boolean hasNext() {
                if(next == null) return false;
                return next.next != null;
            }

            @Override
            public T next() {
                Node<T> n = next;
                next = next.next;
                return n.content;
            }
        };
    }

    @Override
    public int size() {
        if(isEmpty()) return 0;
        return first.tailSize();
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public boolean contains(Object o) {
        if(isEmpty()) return false;
        return first.contains(o);
    }

    @Override
    public Object[] toArray() {
        Iterator<T> iter = iterator();
        Object[] array = new Object[size()];
        Arrays.setAll(array, i -> iter.next());
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<T> iter = (Iterator<T>) iterator();
        for (int i = 0; i < size(); i++)
            a[i] = iter.next();
        return a;
    }

    @Override
    public boolean remove(Object o) {
        if(isEmpty()) return false;
        if(first.content.equals(o)){
            if(first.hasNext()) first = first.next;
            first = null;
        }
                
        return first.removeFromNext(n -> n.equals(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(isEmpty()) return false;
        return c.stream().allMatch(e -> first.contains(e));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(!isEmpty()) first.addAll(c.iterator());
        else{
            Iterator<? extends T> iter = c.iterator();
            if(!iter.hasNext()) return true;
            first = new Node<>(iter.next());
            first.addAll(iter);
        }
        return true;
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        
        
        while(!isEmpty() && c.contains(first.content)) first = first.next;
                
        boolean notFirst = c.stream().filter(a -> !a.equals(first.content)).allMatch(a -> first.removeFromNext(content -> content.equals(a)));
        
        if(c.contains(first.content)) first = first.next;
        
        return notFirst;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        while(!isEmpty() && !c.contains(first.content)) first = first.next;
        first.removeFromNext(e -> !c.contains(e));
        return true;
    }

    @Override
    public void clear() {
        first = null;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * The type of content of the node.
     *
     * @param <T>
     */
    private static class Node<T> {

        public Node<T> next;
        public T content;

        /**
         * Is there a node after this one.
         *
         * @return True if there is a node after this one, false otherwise.
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * A constructor
         *
         * @param content The element in the node.
         */
        public Node(T content) {
            this.content = content;
        }

        /**
         * Gets the i'th node after this one.
         *
         * @param i the index of the desired element.
         * @return The ith node after this one.
         */
        public Node<T> get(int i) {
            if (i == 0) return this;
            if (!hasNext()) throw new IndexOutOfBoundsException("There is no "
                        + i + "th element in this list.");
            return next.get((i - 1));
        }

        /**
         * Does this tail contain the desired object?
         *
         * @return True if it does, false otherwise.
         */
        public boolean contains(Object o) {
            if (content.equals(o)) return true;
            return hasNext() && next.contains(o);
        }

        /**
         * Adds an element to the last node in this chain.
         *
         * @param t the content of the element to be added.
         */
        public void add(T t) {
            if (!hasNext()) next.add(t);
            else next = new Node<>(t);
        }
        
        /**
         * Adds all the elements to the end of the list.
         * @param col 
         */
        public void addAll(Iterator<? extends T> col){
            if(!col.hasNext()) return;
            if(!hasNext()) next = new Node<>(col.next());
            next.addAll(col);
        }

        /**
         * The size of this nodes tail inclusive.
         *
         * @return The size of this node's tail, inclusive.
         */
        public int tailSize() {
            if (!hasNext()) return 1;
            return 1 + next.tailSize();
        }
        
        /**
         * If the next node contains the desired content, it is removed.
         * @param t The content to be removed.
         * @return true if the item is here, and false otherwise.
         */
        public boolean removeFromNext(Predicate<T> remove){
            if(!hasNext()) return false;
            if(remove.test(next.content)){
                if(next.hasNext()) next = next.next;
                else next = null;
                return true;
            }
            return next.removeFromNext(remove);
        }

    }

    /**
     * The ith element of the list.
     *
     * @param i The index of the desired element.
     * @return The ith element of the list.
     */
    @Override
    public T get(int i) {
        return first.get(i).content;
    }

    /**
     * Adds an element to the list.
     *
     * @param t The element to be added.
     */
    @Override
    public boolean add(T t) {
        if (first == null) first = new Node<>(t);
        else first.add(t);
        return true;
    }

    /**
     * The first element of the list.
     */
    private Node<T> first;

    /**
     * For testing the list.
     *
     * @param args
     */
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(4);
        list.add(-5);
        list.add(100);

        Iterator<Integer> iter = list.iterator();

        while (iter.hasNext()) System.out.println(iter.next());
    }
}
