package managers;

import java.util.ArrayList;
import java.util.Date;
import models.Loan;
import utils.DateUtils;
import utils.TextUtils;

/**
 * it Controls the "Loan" objects in the ArrayList
 *
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class LoansManager {

    private static final int DAYS_TO_LOAN = 3;
    private static final int EXTRA_DAYS = 1;
    private static ArrayList<Loan> loans;
    public static LoansManager sInstance;
    BooksManager bookManager = BooksManager.getInstance();
    StudentsManager studentManager = StudentsManager.getInstance();

    /**
     * it declares loans like a ArrayList
     */
    public LoansManager() {
        loans = new ArrayList<>();
    }

    /**
     * it Creates an instance to work with this class
     * @return loansmanager instance
     */
    public static LoansManager getInstance() {
        if (sInstance == null) {
            sInstance = new LoansManager();
        }
        return sInstance;
    }

    /**
     * it adds a new "Loan" object to the list
     *
     * @param studentId string id of student
     * @param bookId string id of book
     * @return true if loan was successfull 
     */
    public boolean addLoan(String studentId, String bookId) {
        if (!TextUtils.isEmpty(studentId) && !TextUtils.isEmpty(bookId)) {
            Date myDate = new Date();
            int book = bookManager.getBookIndexById(bookId);
            loans.add(new Loan(bookId, studentId, myDate, DateUtils.addDays(myDate, DAYS_TO_LOAN), 0.0));
            bookManager.bookState(book);
            return true;

        }
        return false;
    }

    /**
     * gets loan
     * @param bookIndex index of the book in the arraylist
     * @return loan 
     */
    public Loan getLoan(int bookIndex) {
        if (bookIndex != -1 && bookIndex < loans.size()) {
            return loans.get(bookIndex);
        }
        return null;
    }

    /**
     * it Searches a "Loan" object using the book id parameter
     * @param id string id of book
     * @return index of the loan in the arraylist 
     */
    public int getLoanIndexByBookId(String id) {
        int loanIndex = -1;
        if (!TextUtils.isEmpty(id)) {
            for (Loan loan : loans) {
                if (id.equals(loan.getBookId())) {
                    loanIndex = loans.indexOf(loan);
                }
            }
        }
        return loanIndex;
    }

    /**
     * deletes the loan when the book is returned
     * @param loanIndex index of the loan in the arraylist
     * @return true when the return of book was successfull 
     */
    public boolean returnBook(int loanIndex) {
        if (loanIndex == -1) {
            return false;
        } else {
            String bookId = loans.get(loanIndex).getBookId();
            int book = bookManager.getBookIndexById(bookId);
            bookManager.bookState(book);
            loans.remove(loanIndex);
            return true;
        }
    }

    /**
     * it Updates "Loan object to add one more day to the loan 
     * @param bookId string id of the book
     * @return true when it could assign a day more to the loan 
     */
    public boolean updateLoan(String bookId) {
        int loanIndex = getLoanIndexByBookId(bookId);
        if (loanIndex != -1 && loanIndex < loans.size() && !TextUtils.isEmpty(bookId)) {
            Date actualDate = loans.get(loanIndex).getDueDate();
            loans.get(loanIndex).setDueDate(DateUtils.addDays(actualDate, EXTRA_DAYS));
            return true;
        }
        return false;
    }

    /**
     * it Gets all the "Loans" objects in the arraylist
     * @return loans
     */
    public ArrayList<Loan> getLoans() {
        return loans;
    }

    /**
     * it Deletes all the "Loans" objects in the arraylist 
     */
    public void clearAll() {
        loans.clear();
    }

}
