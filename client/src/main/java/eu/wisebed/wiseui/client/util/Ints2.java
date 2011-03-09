package eu.wisebed.wiseui.client.util;


/**
 * Additional utility class like the Ints class from google guava.
 * 
 * @author Malte Legenhausen
 */
public class Ints2 {

    public static Integer nullOrValueOf(final String value) {
    	return value == null ? null : Integer.valueOf(value);
    }
}
