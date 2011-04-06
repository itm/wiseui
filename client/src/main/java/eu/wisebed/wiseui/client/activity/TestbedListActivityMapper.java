package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.TestbedListActivity;

public class TestbedListActivityMapper implements ActivityMapper {

	private final TestbedListActivity activity;
	
	@Inject
	public TestbedListActivityMapper(final TestbedListActivity activity) {
		this.activity = activity;
	}
	
	@Override
	public Activity getActivity(Place place) {
		activity.setPlace((WiseUiPlace) place);
		return activity;
	}

}
