package HW1;

import java.util.List;

/**
 * A class for managing a library. The underlying data structure for this class
 * minimizes memory use.
 * @author Tal
 */
public class Library {

    /**
     * Creates a brand new library with no books.
     */
    public Library() {
    }

    /**
     * Adds a new book to the library with a copy quantity of one.
     *
     * @param name     the name of the book
     * @param uniqueID the unique ID of the book
     * @param author   the author of the book
     */
    public void addBook(String name, int uniqueID, String author) {
        // TODO: Implement this method
    }

    /**
     * Removes a book from the library based on its unique ID.
     *
     * @param uniqueID the unique ID of the book to be removed
     */
    public void removeBooks(int... uniqueID) {
        // TODO: Implement this method
    }

    /**
     * Borrows one copy of the book with the specified unique ID.
     *
     * @param uniqueID the unique ID of the book to be borrowed
     * @return An unmodifiable list of books.
     */
    public List<Book> borrowBooks(int... uniqueID) {
        // TODO: Implement this method

    }

    /**
     * Removes all books with the specified name from the library.
     *
     * @param name the name of the books to be removed
     * @return An unmodifiable list of books
     */
    public List<Book> borrowAllBooks(String name) {
        // TODO: Implement this method
    }

    /**
     * Sorting the books in the library according to the number of copies in 
     * ascending order and printing the parts
     * This method has linear complexity.
     * @return A sorted unmodifiable list of all the books in the library.
     */
    public List<Book> sortedByQuantity() {
        // TODO: Implement this method
    }
    
    /**
     * This method should be called when a shelf in the library is in disarray
     * and the books on the shelf need to be sorted.
     * Each shelf has a starting ID and an ending ID.
     * The books on the shelf should be sorted by their id number.
     * This method has linear complexity.
     * @param startID The id of the first element on the shelf.
     * @param endID The id of the last element on the shelf.
     * @return A sorted unmodifiable list of all the books on the shelf.
     */
    public List[] sortForShelf(int startID, int endID){
        
    }

    /**
     * Returns the total number of books in the library.
     *
     * @return the total number of books in the library
     */
    public int totalBooksInLibrary() {
        // TODO: Implement this method
        return 0;
    }

    /**
     * Prints the name of the author who wrote the most books in the library.
     */
    public String authorWithMostBooks() {

    }


    /**
     * Book class representing a book with name, unique ID, author, and quantity.
     */
    public static class Book {


        /**
         * Constructor for a book
         * @param name The name of the book.
         * @param uniqueID The ID of the book.
         * @param author The author of the book.
         */
        public Book(String name, int uniqueID, String author) {

        }

        @Override
        public String toString() {
            //implement this method.
        }

        
    }
}
s