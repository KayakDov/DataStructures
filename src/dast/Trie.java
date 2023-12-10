package dast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Dov Neimand
 * @param <T> What is the trie keeping track of, numbers with lots of digits?
 * Words with lots of characters?
 */
public class Trie<T> {

    private final toBoundedIntArray<T> map;

    private TrieNode root;

    /**
     * A constructor.
     *
     * @param numDigits The number of digits or letters in the alphabet that
     * each node should have access to. The smaller this number, the more
     * efficient your trie. For example, if you were storing lower case strings
     * then this would be (int)'z'.
     * @param map A one to one function that maps whatever objects you want to
     * store in the trie to a sequence of integers from 0 to numDigits. The more
     * overlap between sequence prefixes, the more efficient the trie. For
     * example, if you want to store lowercase strings then each string might be
     * mapped to a sequence of it's characters askii values, translated so that
     * 'a' maps to 0.
     * @param bound The maximum integer value of each digit.
     * @param inverseMap The inverse of the map.
     */
    public Trie(Function<T, int[]> map,
            Function<int[], T> inverseMap, int bound) {
        this(toBoundedIntArray.get(map, inverseMap, bound));
    }

    /**
     * Does the trie contain this sequence of digits.
     *
     * @param digits
     * @return
     */
    private boolean contains(int[] digits) {
        TrieNode n = root;
        for (int digit : digits)
            if (root.hasNext(digit)) root = root.next(digit);
            else return false;
        return root.isTerminal();
    }

    /**
     * Does the trie contain this element.
     *
     * @param key The element to be searched for.
     * @return True if the trie contains key, false otherwise.
     */
    public boolean contains(T key) {
        return contains(map.apply(key));
    }

    /**
     * Inserts a word into the trie.
     *
     * @param digits The digits, or letters of the word.
     */
    private void insert(int[] digits) {
        TrieNode n = root;
        for (int digit : digits) n = n.next(digit);
        n.setTerminal(true);
    }

    /**
     * Insert an element into the trie.
     *
     * @param element
     */
    public void insert(T element) {
        insert(map.apply(element));
    }
    
    /**
     * inserts a bunch of elements into this trie.
     * @param elements The elements to be inserted.
     */
    public void insert(T... elements){
        for(T e: elements) insert(e);
    }

    /**
     * Deletes a word from the trie and removes unnescesary nodes.
     *
     * @param digits The digits of the word being removed.
     */
    private void delete(int[] digits) {
        LinkedList<TrieNode> path = new LinkedList<>();
        path.add(root);
        for (int digit : digits) path.add(path.getLast().next(digit));
        path.getLast().setTerminal(false);
        for (int i = digits.length - 1; i >= 0 && !path.getLast().hasNext(); i--)
            if (!path.getLast().hasNext() && !path.getLast().isTerminal()) {
                path.removeLast();
                path.getLast().deleteNext(digits[i]);
            }

    }

    /**
     * Deletes an element from the trie and cleans up unused space.
     *
     * @param element The element to be deleted.
     */
    public void delete(T element) {
        delete(map.apply(element));
    }

    /**
     * All the elements that start with prefix.
     *
     * @param prefix The prefix of the desired elements.
     * @return
     */
    private Stream<int[]> all(int[] prefix) {
        TrieNode n = root;
        for (int digit : prefix)
            if (n.hasNext()) n = n.next(digit);
            else return Stream.of();

        return n.allChildren(IntStream.of(prefix).boxed().toList());
    }

    /**
     * All the elements in the trie with the proffered prefix.
     * @param prefix The prefix of the desired elements.
     * @return All the elements in the trie with the desired prefix.
     */
    public Stream<T> all(T prefix) {
        return all(map.apply(prefix)).map(array -> map.inverse(array));
    }
    
    /**
     * All the elements in the trie.
     * @return All the elements in the trie with the desired prefix.
     */
    public Stream<T> all() {
        return all(new int[0]).map(array -> map.inverse(array));
    }

    /**
     * The constructor.
     *
     * @param numDigits The number of digits or letters in the alphabet that
     * each node should have access to. The smaller this number, the more
     * efficient your trie. For example, if you were storing lower case strings
     * then this would be (int)'z'.
     * @param map A one to one function that maps whatever objects you want to
     * store in the trie to a sequence of integers from 0 to numDigits. The more
     * overlap between sequence prefixes, the more efficient the trie. For
     * example, if you want to store lowercase strings then each string might be
     * mapped to a sequence of it's characters askii values, translated so that
     * 'a' maps to 0.
     */
    public Trie(toBoundedIntArray<T> map) {
        this.map = map;
        this.root = new TrieNode(map.getUpperBound());
    }

    /**
     * A trie designed to contain single words, no spaces, of upper and lower
     * case letter.
     *
     * @return A trie designed to hold single words of upper and lower case
     * letters.
     */
    public static Trie<String> wordTrie() {
        return new Trie(new toBoundedIntArray<String>() {
            @Override
            public String inverse(int[] array) {
                return Arrays.stream(array)
                        .map(
                                i -> i <= 'Z' - 'A' ? i + 'A' : i - ('Z' - 'A') - 1 + 'a'
                        ).mapToObj(i -> Character.toString((char) i)).
                        collect(Collectors.joining());
            }

            @Override
            public int getUpperBound() {
                return apply("z")[0] + 1;
            }

            @Override
            public int[] apply(String string) {
                return string.chars().map(
                        c -> c >= 'A' && c <= 'Z' ? c - 'A'
                        : c - 'a' + 'Z' - 'A' + 1
                ).toArray();
            }
        });
    }

    /**
     * A trie designed to hold integers.
     * @return A Trie designed to hold integers.
     */
    public static Trie<Integer> intTrie() {
        return new Trie<>(new toBoundedIntArray<Integer>() {
            @Override
            public Integer inverse(int[] array) {
                int inverse = 0, pow = 1;
                for(int i = 0; i < array.length; i++, pow *= 10)
                    inverse += array[i] * pow;
                if(array[array.length - 1] == 0) inverse *= -1;
                return inverse;
            }

            @Override
            public int getUpperBound() {
                return 10;
            }

            /**
             * The number of digits in a number.  Note that a negative
             * counts as a digit.
             * @param i The number for whom the number of digits is desired.
             * @return The number of digits, base 10, of i.
             */
            private int numDigits(int i) {
                int numDigits = i < 0 ? 1 : 0;

                while (Math.abs(i) > 0) {
                    i /= 10;
                    numDigits++;
                }
                return numDigits;
            }

            @Override
            public int[] apply(Integer t) {
                int[] apply = new int[numDigits(t)];
                
                for (int i = 0; i < apply.length; i++, t /= 10)
                    apply[i] = Math.abs(t % 10);

                return apply;
            }
        });
    }
    
    /**
     * Tests the wordTrie.
     */
    public static void testWordTrie(){
        
        Trie<String> strings = Trie.wordTrie();
        
        strings.insert("ham", "bob", "hat", "hats");
        
        strings.delete("hat");
        
        strings.all("").forEach(System.out::println);
    }
    
    /**
     * Tests the numTrie.
     */
    public static void testNumTrie(){
        Trie numbers = Trie.intTrie();

        numbers.insert(55, 25, 12);
        
        numbers.delete(25);
        
        numbers.all(5).forEach(System.out::println);
    }
    
    /**
     * Testing some of the methods here.
     * @param args Not used.
     */
    public static void main(String[] args) {
        
        testWordTrie();
        testNumTrie();
        
    }
}

/**
 * A node.
 */
class TrieNode {

    private final TrieNode[] next;
    private boolean terminal;

    /**
     * The constructor
     *
     * @param numChildren The number of available digits or children this nodes
     * in this tree will have. This should be consistent for all nodes in this
     * tree.
     * @param terminal Is this a terminal node.
     */
    public TrieNode(int numChildren) {
        this.next = new TrieNode[numChildren];
        this.terminal = false;
    }

    /**
     * Is this node the final digit of a value being stored.
     *
     * @return True if it is, false otherwise.
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * The next node with the following index.
     *
     * @param i The index of the next node.
     * @return The next node with value and index i.
     */
    public TrieNode next(int i) {
        if (hasNext(i)) return next[i];
        else return setNext(i);
    }

    /**
     * Is the proffered index a child of this node?
     *
     * @param i The desired child.
     * @return True if the proffered index is a child of this node, false
     * otherwise.
     */
    public boolean hasNext(int i) {
        return next[i] != null;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    /**
     * expands the tree at the i'th index.
     *
     * @param i The index the tree should be expanded at.
     */
    public TrieNode setNext(int i) {
        return next[i] = new TrieNode(next.length);
    }

    /**
     * Does this node have any children.
     *
     * @return True if this node has any children, false otherwise.
     */
    public boolean hasNext() {
        return IntStream.range(0, next.length)
                .parallel().anyMatch(i -> hasNext(i));
    }

    /**
     * Deletes the next node.
     *
     * @param nodeIndex The index of the node to be deleted.
     */
    public void deleteNext(int nodeIndex) {
        next[nodeIndex] = null;
    }

    /**
     * Copies a list and adds an element to the coppied list.
     *
     * @param prefix The list to be copied.
     * @param add The element to be added.
     * @return A new list that is a copy of the old list with an added element.
     */
    private static LinkedList<Integer> copy(
            List<Integer> prefix,
            int add) {
        LinkedList<Integer> prefixWithI = new LinkedList<>(prefix);
        prefixWithI.add(add);
        return prefixWithI;
    }

    /**
     * Returns the proffered linked list appended to the stream.
     *
     * @param addTo The stream the linked likst should be added to as an int[].
     * @param add The element to be added to the stream.
     * @return The list appended as an int[] to the stream.
     */
    private static Stream<int[]> addNext(Stream<int[]> addTo,
            LinkedList<Integer> add) {
        return Stream.concat(
                addTo,
                Stream.of(add.stream().mapToInt(j -> j).toArray())
        );
    }

    /**
     * All the children of this node that are terminal.
     *
     * @param prefix The prefix to this node.
     * @return A stream of all the children that are terminal to this node.
     */
    public Stream<int[]> allChildren(List<Integer> prefix) {
        Stream<int[]> allChildren = Stream.of();

        for (int i = 0; i < next.length; i++)
            if (hasNext(i)) {
                LinkedList<Integer> iPlusPrefix = copy(prefix, i);
                if (next(i).isTerminal())
                    allChildren = addNext(allChildren, iPlusPrefix);
                allChildren = Stream.concat(
                        allChildren,
                        next(i).allChildren(iPlusPrefix)
                );
            }
        return allChildren;
    }
}
