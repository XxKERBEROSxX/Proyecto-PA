package models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alex, Carlos, Sergio, Ericka
 */
public class Loan implements Serializable{
    

    private String bookId; 
    private String userId; 
    private Date startDate; 
    private Date dueDate; 
    private double bill;
    
     /**
     * @param bookID string id of book
     * @param userID string username of user
     * @param startDate Date date that the loan took place
     * @param dueDate Date date that the book must be returned
     * @param bill double how much is to be ticked for this loan
     */
    
    public Loan(String bookID, String userID, Date startDate, Date dueDate, double bill){
        this.bookId = bookID; 
        this.userId = userID; 
        this.startDate = startDate; 
        this.dueDate = dueDate; 
        this.bill = bill; 
    }


    /**
     * 
     * @return string id of the book that is in the loan 
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * 
     * @param bookId string id of the book that is in the loan
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /**
     * 
     * @return  string username of the user that is in the loan
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId  string username of the user that is in the loan
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return date date that the loan started
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate  date date that the loan started
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return date due date of the loan
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * 
     * @param dueDate date due date of the loan
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * 
     * @return double money to ticket for the loan
     */
    public double getBill() {
        return bill;
    }

    /**
     * 
     * @param bill double money to ticket for the loan
     */
    public void setBill(double bill) {
        this.bill = bill;
    }
    
    
}

