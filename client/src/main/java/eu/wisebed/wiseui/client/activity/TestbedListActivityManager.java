package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestbedListActivityManager extends ActivityManager {

	@Inject
	public TestbedListActivityManager(final TestbedListActivityMapper mapper, final EventBus eventBus) {
		super(mapper, eventBus);
	}
}
