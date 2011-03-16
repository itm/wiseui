package eu.wisebed.wiseui.client.util;

import java.util.List;

import javax.annotation.Nullable;


/**
 * Additional functionality for the <code>Lists</code> class from the google guava library.
 * 
 * @author Malte Legenhausen
 */
public class Lists2 {
	
	public static <T> boolean isNullorEmpty(@Nullable final List<T> list) {
		return list == null || list.isEmpty();
	}
}
