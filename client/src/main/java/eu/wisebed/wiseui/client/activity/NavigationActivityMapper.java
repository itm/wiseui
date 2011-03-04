package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.navigation.NavigationActivity;

public class NavigationActivityMapper implements ActivityMapper {

    private final NavigationActivity activity;

    @Inject
    public NavigationActivityMapper(final NavigationActivity activity) {
        this.activity = activity;
    }

    public Activity getActivity(final Place place) {
        activity.setPlace(place);
        return activity;
    }
}
