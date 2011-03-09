package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;

/**
 * This place represents the current state of the testbed-selection-view as reflected by the URL.
 * The state consists of the two parameters: testbed selection, view.
 * Testbed selection is the number of the currently selected testbed.
 * View is the currently displayed view of the selected testbed.
 * The view can be "map" (default), "detail" or "raw".
 *
 * @author Malte Legenhausen, Sönke Nommensen
 */
public class TestbedSelectionPlace extends KeyValuePlace {

    public TestbedSelectionPlace() {
    	this(null, TestbedSelectionConstants.MAP_VIEW);
    }

    public TestbedSelectionPlace(final Integer selection, final String view) {
    	set(TestbedSelectionConstants.TESTBED_SELECTION_STRING, Objects2.nullOrToString(selection));
        set(TestbedSelectionConstants.TESTBED_VIEW_STRING, Objects2.nullOrToString(view));
    }
    
    public TestbedSelectionPlace(final String token) {
    	this(null, null);
    	parse(token);
    }

    public Integer getSelection() {
        return Ints2.nullOrValueOf(get(TestbedSelectionConstants.TESTBED_SELECTION_STRING));
    }
    
    public String getView() {
		return Objects2.nullOrToString(get(TestbedSelectionConstants.TESTBED_VIEW_STRING));
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
