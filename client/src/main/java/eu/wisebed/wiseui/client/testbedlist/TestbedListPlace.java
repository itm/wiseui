package eu.wisebed.wiseui.client.testbedlist;

import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class TestbedListPlace extends KeyValuePlace {

	private static final String TESTBED_ID_STRING = "testbedId";
	
	public TestbedListPlace() {
		set(TESTBED_ID_STRING, null);
	}
	
	public TestbedListPlace(final Integer index) {
		set(TESTBED_ID_STRING, Objects2.nullOrToString(index));
	}
	
	public Integer getTestbedId() {
		return Ints2.nullOrValueOf(get(TESTBED_ID_STRING));
	}
}
