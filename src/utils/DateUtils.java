package utils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Alex, Carlos, Sergio, Ericka
 */
public class DateUtils {

    /**
     * it adds or decrements days on a date
     *
     * @param date date that we want to add days to 
     * @param days integer number of days we want to add (minus number decrements days)
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); 
        return cal.getTime();
    }
    
    /**
     * tells us how many days are between two dates
     * @param startDate first date
     * @param dueDate second date
     * @return long difference between the two dates
     */

    public static long differenceBetweenDays(Date startDate, Date dueDate) {
        long startTime = startDate.getTime();
        long endTime = dueDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        return diffDays;
    }
}
