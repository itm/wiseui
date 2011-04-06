package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;
import eu.wisebed.wiseui.client.util.Objects2;

/**
 * This place represents the current state of the testbed-selection-view as reflected by the URL.
 * The state consists of the two parameters: testbed selection, view.
 * Testbed selection is the number of the currently selected testbed.
 * View is the currently displayed view of the selected testbed.
 * The view can be "map" (default), "detail" or "raw".
 *
 * @author Malte Legenhausen, Sönke Nommensen
 */
public class TestbedSelectionPlace extends WiseUiPlace {

    public TestbedSelectionPlace() {
    	this(null, TestbedSelectionConstants.MAP_VIEW);
    }

    public TestbedSelectionPlace(final Integer selection, final String view) {
    	super(selection);
        set(TestbedSelectionConstants.TESTBED_VIEW_STRING, view);
    }
    
    public TestbedSelectionPlace(final String token) {
    	this(null, TestbedSelectionConstants.MAP_VIEW);
    	parse(token);
    }
    
    public String getView() {
		return Objects2.nullOrToString(get(TestbedSelectionConstants.TESTBED_VIEW_STRING));
	}
    
    @Override
    public WiseUiPlace copy(final Integer testbedId) {
    	return new TestbedSelectionPlace(testbedId, getView());
    }

    /**
     * Tokenizer instance for serialization and deserialization of the TestbedSelectionPlace.
     * 
     * @author Malte Legenhausen
     */
    public static class Tokenizer implements PlaceTokenizer<TestbedSelectionPlace> {

        public String getToken(final TestbedSelectionPlace place) {
            return place.toString();
        }

        public TestbedSelectionPlace getPlace(final String token) {
        	return new TestbedSelectionPlace(token);
        }
    }
}
