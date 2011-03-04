package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.experimentation.ExperimentationPlace;
import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;

public class ContentActivityMapper implements ActivityMapper {

    private WiseUiGinjector injector;

    /**
     * AppActivityMapper associates each Place with its corresponding
     * {@link Activity}
     *
     * @param injector GIN injector to be passed to activities
     */
    @Inject
    public ContentActivityMapper(final WiseUiGinjector injector) {
        super();
        this.injector = injector;
    }

    /**
     * Map each Place to its corresponding Activity.
     */
    public Activity getActivity(final Place place) {
        Activity mappedActivity = null;
        if (place instanceof TestbedSelectionPlace) {
            final TestbedSelectionActivity activity = injector.getTestbedSelectionActivity();
            activity.setPlace((TestbedSelectionPlace) place);
            mappedActivity = activity;
        }
        if (place instanceof ReservationPlace) {
            mappedActivity = injector.getReservationActivity();
        }
        if (place instanceof ExperimentationPlace) {
            mappedActivity = injector.getExperimentationActivity();
        }
        if (place instanceof AdministrationPlace) {
            mappedActivity = injector.getAdministrationActivity();
        }
        return mappedActivity;
    }
}
