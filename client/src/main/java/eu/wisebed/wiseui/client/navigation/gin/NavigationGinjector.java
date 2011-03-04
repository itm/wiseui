package eu.wisebed.wiseui.client.navigation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.navigation.NavigationActivity;
import eu.wisebed.wiseui.client.navigation.view.NavigationView;

public interface NavigationGinjector extends Ginjector {

    NavigationView getNavigationView();

    NavigationActivity getNavigationActivity();
}
