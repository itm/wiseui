package eu.wisebed.wiseui.client;

import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;
import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class WiseUiPlace extends KeyValuePlace {

	public WiseUiPlace() {
		set(TestbedSelectionConstants.TESTBED_SELECTION_STRING, null);
	}
	
	public WiseUiPlace(final String token) {
		parse(token);
	}
	
	public WiseUiPlace(final Integer selection) {
		set(TestbedSelectionConstants.TESTBED_SELECTION_STRING, Objects2.nullOrToString(selection));
	}
	
	public Integer getTestbedId() {
		return Ints2.nullOrValueOf(get(TestbedSelectionConstants.TESTBED_SELECTION_STRING));
	}
	
	public WiseUiPlace copy(final Integer testbedId) {
		return new WiseUiPlace(testbedId);
	}
}
