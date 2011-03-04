package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class ContentActivityManager extends ActivityManager {

    @Inject
    public ContentActivityManager(final ContentActivityMapper mapper,
                                  final EventBus eventBus) {
        super(mapper, eventBus);
    }
}
