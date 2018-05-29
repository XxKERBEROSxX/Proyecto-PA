
package utils;

import java.util.ArrayList;

public class CountElements {

    /**
     * 
     * @param <T> generic type of data
     * @param array the array list we are going to work with
     * @return number of elements in the arraylist 
     */
    
    public static <T> int count(ArrayList<T> array) {
        int count = 0;
            for (T element : array) {
                if(element != null)
                    count ++;
            }
        return count;

    }
}
