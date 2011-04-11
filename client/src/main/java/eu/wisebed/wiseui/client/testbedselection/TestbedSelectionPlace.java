package eu.wisebed.wiseui.client.testbedselection;

import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
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
public class TestbedSelectionPlace extends KeyValuePlace {

    public TestbedSelectionPlace() {
    	this(TestbedSelectionConstants.MAP_VIEW);
    }

    public TestbedSelectionPlace(final String view) {
        set(TestbedSelectionConstants.TESTBED_VIEW_STRING, view);
    }
    
    public String getView() {
		return Objects2.nullOrToString(get(TestbedSelectionConstants.TESTBED_VIEW_STRING));
	}
}
