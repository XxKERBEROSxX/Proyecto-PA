package managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Book;
import utils.TextUtils;

/**
 * it Controls the "books" objects in the ArrayList
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class BooksManager {
    
    /**
     * @param stock arraylists of books
     * @param AVAILABLE boolean to determine if a book is available to borrow
     */
    private static ArrayList<Book> stock;
    public static BooksManager sInstance;
    private final boolean AVAILABLE = true;

    /**
     * it declares stock as an ArrayList
     */
    public BooksManager() {
        stock = new ArrayList<>();
    }

    /**
     * it Creates an instance to work with this class
     * @return instance for BooksManager
     */
    public static BooksManager getInstance() {
        if (sInstance == null) {
            sInstance = new BooksManager();
        }
        return sInstance;
    }

    /**
     * it adds a new "book" object to the list
     *
     * @param id Book id
     * @param ISBN Book ISBN
     * @param author Book Author
     * @param bookName Book Name
     * @param edition Book Edition
     * @param editorial Book Editorial
     * @param state Book State
     * @return true if book was added successfully
     */
    public boolean addBook(String id, String ISBN, String author, String bookName, String edition, String editorial, boolean state) {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(ISBN)) {
            if (getBookIndexById(id) != -1) {
                JOptionPane.showMessageDialog(null, "Error, ya existe el libro");
            } else {
                state = AVAILABLE;
                stock.add(new Book(id, ISBN, author, bookName, edition, editorial, state));
                return true;
            }

        }
        return false;
    }

    /**
     * gets new book
     * @param bookIndex index of book to get
     * @return object book
     */
    public Book getBook(int bookIndex) {
        if (bookIndex != -1 && bookIndex < stock.size()) {
            return stock.get(bookIndex);
        }
        return null;
    }

    /**
     * @param id id of the book
     * @return book index
     */
    public int getBookIndexById(String id) {
        int equalsSelection = -1;
        int containsSelection = -1;
        if (!TextUtils.isEmpty(id)) {
            for (Book book : stock) {
                if (id.equals(book.getId())) {
                    equalsSelection = stock.indexOf(book);
                    break;
                } else {
                    if (id.contains(book.getName())) {
                        containsSelection = stock.indexOf(book);
                    }
                }
            }
        }
        return equalsSelection == -1 && containsSelection == -1 ? -1 : equalsSelection != -1 ? equalsSelection : containsSelection;
    }

    /**
     * It removes a "book" objetc in the list
     * @param bookIndex index of the book in the arrayList
     * @return true if the book was deleted successfully
     */
    public boolean deleteBook(int bookIndex) {
        if (bookIndex == -1) {
            return false;
        } else {
            stock.remove(bookIndex);
            return true;
        }
    }

    /**
     * it Updates "book" object parameters 
     * @param bookIndex integer book index in the arraylist
     * @param id string book id
     * @param ISBN string ISBN
     * @param author string author
     * @param name string name
     * @param edition string edition
     * @param editorial string editorial
     * @return true if update was successfull 
     */
    public boolean updateBook(int bookIndex, String id, String ISBN, String author, String name, String edition, String editorial) {
        if (bookIndex != -1 && bookIndex < stock.size() && !TextUtils.isEmpty(id)) {
            stock.get(bookIndex).setId(id);
            stock.get(bookIndex).setISBN(ISBN);
            stock.get(bookIndex).setAuthor(author);
            stock.get(bookIndex).setName(name);
            stock.get(bookIndex).setEdition(edition);
            stock.get(bookIndex).setEditorial(editorial);
            return true;
        }
        return false;
    }

    /**
     * it Gets all the "books" objects in the arraylist
     * @return books arraylist
     */
    public ArrayList<Book> getBooks() {
        return stock;
    }

    /**
     * it Deletes all the "books" objects in the list
     */
    public void clearAll() {
        stock.clear();
    }
    
    /**
     * @param bookIndex Index of the book 
     * @return true when it succesfully finds and toggles the state of the book (available or not)
     */
    public boolean bookState(int bookIndex){
        if(bookIndex != -1 && bookIndex < stock.size()){
            boolean currentState = stock.get(bookIndex).getState();
            stock.get(bookIndex).setState(!currentState);
            return true;
        }
        return false; 
    }

    /**
     * it Prints all the "books" objects in the list
     */
    public void printBooks() {
        System.out.println(stock.toString());
    }

}
