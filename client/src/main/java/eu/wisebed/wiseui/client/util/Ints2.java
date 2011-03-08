package eu.wisebed.wiseui.client.util;

public class Ints2 {

    public static Integer nullOrValueOf(final String value) {
    	return value == null ? null : Integer.valueOf(value);
    }
}
