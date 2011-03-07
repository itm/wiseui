package eu.wisebed.wiseui.client.testbedselection;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TestbedSelectionPlace extends Place {

	private static final String SEPARATOR = "&";
	
	private final Map<String, String> variables = new HashMap<String, String>();

    public TestbedSelectionPlace() {
    	this(null, 0);
    }

    public TestbedSelectionPlace(final Integer selection, final Integer view) {
        set("selection", selection);
        set("view", view);
    }
    
    public String get(String key) {
    	return variables.get(key);
    }
    
    public void set(String key, Object value) {
    	variables.put(key, value == null ? null : value.toString());
    }

    public Integer getSelection() {
    	final String selection = get("selection");
        return selection == null ? null : Integer.parseInt(selection);
    }
    
    public Integer getView() {
		return Integer.parseInt(get("view"));
	}
    
    public static TestbedSelectionPlace parse(String token) {
    	final TestbedSelectionPlace place = new TestbedSelectionPlace();
    	final String[] variables = token.split(SEPARATOR);
    	for (final String variable : variables) {
    		final String[] tokens = variable.split("=");
    		place.set(tokens[0], tokens[1]);
    	}
    	return place;
    }
    
    @Override
    public String toString() {
    	final StringBuilder builder = new StringBuilder();
    	for (final String key : variables.keySet()) {
    		String value = variables.get(key);
    		if (value != null) {
    			builder.append(key).append("=").append(value).append(SEPARATOR);
    		}
    	}
    	if (builder.length() > 0) {
    		builder.deleteCharAt(builder.length() - 1);
    	}
    	return builder.toString();
    }

    public static class Tokenizer implements PlaceTokenizer<TestbedSelectionPlace> {

        public String getToken(final TestbedSelectionPlace place) {
            return place.toString();
        }

        public TestbedSelectionPlace getPlace(final String token) {
        	return TestbedSelectionPlace.parse(token);
        }
    }
}
