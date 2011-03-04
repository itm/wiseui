package eu.wisebed.wiseui.client.navigation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.experimentation.ExperimentationPlace;
import eu.wisebed.wiseui.client.navigation.view.NavigationView;
import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AbstractActivity implements
        NavigationView.Presenter {

    private class Entry {
        private Place place;

        private String name;

        public Entry(final String name, final Place place) {
            this.name = name;
            this.place = place;
        }

        public String getName() {
            return name;
        }

        public Place getPlace() {
            return place;
        }
    }

    private WiseUiGinjector injector;

    private NavigationView navigationView;

    private List<Entry> navigation = new ArrayList<Entry>();

    private Place place;

    @Inject
    public NavigationActivity(final WiseUiGinjector injector) {
        this.injector = injector;

        navigation.add(new Entry("Testbed Selection", new TestbedSelectionPlace(null)));
        navigation.add(new Entry("Reservation", new ReservationPlace()));
        navigation
                .add(new Entry("Experimentation", new ExperimentationPlace()));
        navigation.add(new Entry("Administration", new AdministrationPlace()));
    }

    public void start(final AcceptsOneWidget container, final EventBus eventBus) {
        GWT.log("Start navigation activity");
        navigationView = injector.getNavigationView();
        navigationView.setPresenter(this);

        initTabs();
        updateSelection();

        container.setWidget(navigationView);
    }

    private void initTabs() {
        for (Entry entry : navigation) {
            navigationView.add(entry.getName());
        }
    }

    public void updateSelection() {
        if (place == null) {
            navigationView.select(0);
        }
        int i = 0;
        for (Entry entry : navigation) {
            if (place != null
                    && place.getClass().equals(entry.getPlace().getClass())) {
                navigationView.select(i);
            }
            i++;
        }
    }

    public void selected(final Integer index) {
        final Entry entry = navigation.get(index);
        GWT.log("Go to place: " + entry.getName());
        injector.getPlaceController().goTo(entry.getPlace());
    }

    public void setPlace(final Place place) {
        this.place = place;
        if (navigationView != null) {
            updateSelection();
        }
    }
}
