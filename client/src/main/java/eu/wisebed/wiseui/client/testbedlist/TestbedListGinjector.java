package eu.wisebed.wiseui.client.testbedlist;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;

public interface TestbedListGinjector extends Ginjector {

	TestbedListActivity getTestbedListActivity();
	
	TestbedListView getTestbedListView();
}
