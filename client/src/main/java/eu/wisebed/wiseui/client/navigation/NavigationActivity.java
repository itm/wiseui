package eu.wisebed.wiseui.client.navigation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.navigation.view.NavigationView;

public class NavigationActivity extends AbstractActivity implements NavigationView.Presenter {

    private class Entry {
    	
        private final NavigationPlace place;

        private final String name;

        public Entry(final String name, final NavigationPlace place) {
            this.name = name;
            this.place = place;
        }

        public String getName() {
            return name;
        }

        public NavigationPlace getPlace() {
            return place;
        }
    }

    private WiseUiGinjector injector;

    private NavigationView navigationView;

    private List<Entry> navigation = new ArrayList<Entry>();

    private WiseUiPlace place;

    @Inject
    public NavigationActivity(final WiseUiGinjector injector) {
        this.injector = injector;

        navigation.add(new Entry("Testbed Selection", new NavigationPlace(0)));
        navigation.add(new Entry("Reservation", new NavigationPlace(1)));
        navigation.add(new Entry("Experimentation", new NavigationPlace(2)));
        navigation.add(new Entry("Administration", new NavigationPlace(3)));
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
        final NavigationPlace navigationPlace = (NavigationPlace) place.get(NavigationPlace.class);
        navigationView.select(navigationPlace.getIndex());
    }

    public void selected(final Integer index) {
        final Entry entry = navigation.get(index);
        GWT.log("Go to place: " + entry.getName());
        injector.getPlaceController().goTo(place.update(entry.getPlace()));
    }

    public void setPlace(final WiseUiPlace place) {
        this.place = place;
        if (navigationView != null) {
            updateSelection();
        }
    }
}
