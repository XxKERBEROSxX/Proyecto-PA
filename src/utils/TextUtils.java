package utils;
/**
 *
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class TextUtils {
/**
 * It checks if the parameter it's empty or not
 * @param text string to check if its empty 
 * @return true if its empty 
 */
    public static boolean isEmpty(String text) {
        if (text == null) {
            return true;
        }
        return text.length() <= 0;
    }
}

