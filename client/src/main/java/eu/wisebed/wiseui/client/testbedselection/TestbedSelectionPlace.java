package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class TestbedSelectionPlace extends KeyValuePlace {

    public TestbedSelectionPlace() {
    	this(null, 0);
    }

    public TestbedSelectionPlace(final Integer selection, final Integer view) {
    	set("selection", Objects2.nullOrToString(selection));
        set("view", Objects2.nullOrToString(view));
    }
    
    public TestbedSelectionPlace(final String token) {
    	this(null, 0);
    	parse(token);
    }

    public Integer getSelection() {
        return Ints2.nullOrValueOf(get("selection"));
    }
    
    public Integer getView() {
		return Ints2.nullOrValueOf(get("view"));
	}

    public static class Tokenizer implements PlaceTokenizer<TestbedSelectionPlace> {

        public String getToken(final TestbedSelectionPlace place) {
            return place.toString();
        }

        public TestbedSelectionPlace getPlace(final String token) {
        	return new TestbedSelectionPlace(token);
        }
    }
}
