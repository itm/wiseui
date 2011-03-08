package eu.wisebed.wiseui.client.util;

/**
 * Object utility class with additional methods to Objects from Guava API.
 * 
 * @author Malte Legenhausen
 */
public class Objects2 {

    public static String nullOrToString(Object value) {
    	return value == null ? null : value.toString();
    }
}
