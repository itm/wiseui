package eu.wisebed.wiseui.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.gwt.place.shared.Place;
import com.google.gwt.thirdparty.guava.common.base.Splitter;


/**
 * Use this class to store string key values in a Place.
 * 
 * @author Malte Legenhausen
 */
public class KeyValuePlace extends Place {
	
	private static final String DEFAULT_JOINER = "&";
	
	private static final String DEFAULT_SEPARATOR = "=";
	
	private final String joiner;
	
	private final String separator;

	private final Map<String, String> variables = new HashMap<String, String>();

	public KeyValuePlace(final String joiner, final String separator) {
		this.joiner = joiner;
		this.separator = separator;
	}
	
	public KeyValuePlace(final String joiner, final String separator, final String token) {
		this(joiner, separator);
		parse(token);
	}
	
	public KeyValuePlace() {
		this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
	}
	
	public KeyValuePlace(final String token) {
		this(DEFAULT_JOINER, DEFAULT_SEPARATOR, token);
	}

	public String get(final String key) {
		return variables.get(key);
	}

	public void set(final String key, final String value) {
		variables.put(key, value);
	}
	
    public void parse(final String token) {
    	final Iterable<String> vars = Splitter.on(joiner).split(token);
    	for (final String variable : vars) {
    		final String[] tokens = variable.split(separator);
    		if (tokens.length == 2) {
    			set(tokens[0], tokens[1]);
    		}
    	}
    }
    
    @Override
    public String toString() {
    	final Map<String, String> vars = Maps.filterValues(this.variables, Predicates.notNull());
    	return Joiner.on(joiner).withKeyValueSeparator(separator).join(vars);
    }
}
