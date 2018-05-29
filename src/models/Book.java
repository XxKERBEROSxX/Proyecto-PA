package models;

import java.io.Serializable;

/**
 * it contains the book's parameters that are saved later in the list
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class Book implements Comparable<Book>, Serializable {

    private String id;
    private String ISBN;
    private String author;
    private String name;
    private String edition;
    private String editorial;
    private Boolean state;

    /**
     *
     * @param id string id of the book 
     * @param ISBN string ISBN of the book
     * @param author string author of the book
     * @param bookName string name of the book
     * @param edition string edition of the book
     * @param editorial string editorial of the book
     * @param state boolean state of the book TRUE = AVAILABLE. 
     */
    public Book(String id, String ISBN, String author, String bookName, String edition, String editorial, boolean state) {
        this.id = id;
        this.ISBN = ISBN;
        this.author = author;
        this.name = bookName;
        this.edition = edition;
        this.editorial = editorial;
        this.state = state;
    }
    /**
     * it gets the parameter id
     * @return
     * 
     */
    public String getId() {
        return id;
    }
    /**
     * it assigns the parameter id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * it gets the parameter ISBN
     * @return
     */
    public String getISBN() {
        return ISBN;
    }
    
    /**
     * it assigns the parameter ISBN
     * @param ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    /**
     * it gets the parameter author
     * @return
     */
    public String getAuthor() {
        return author;
    }
    /**
     * it assigns the parameter author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * it gets the parameter name
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * it assigns the parameter name 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * it gets the parameter edition
     * @return
     */
    public String getEdition() {
        return edition;
    }
    /**
     * it assigns the parameter edition
     * @param edition
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }
    /**
     * it gets the parameter editorial
     * @return
     */
    public String getEditorial() {
        return editorial;
    }
    /**
     * it assigns the parameter editorial
     * @param editorial
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    /**
     * it gets the parameter state
     * @return
     */
    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

     /**
     *  it orders books alphabetically
     * @param book
     * @return 
     */
    @Override
    public int compareTo(Book book) {
        String firstName = book.getName();
        String nextName = this.getName();
        return nextName.compareTo(firstName);
    }

    /**
   * it prints book object in the console
   * @return 
   */
    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", name=" + name + '}';
    }
}