package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;

public interface TestbedSelectionView extends IsWidget {

    void setPresenter(Presenter presenter);

    AcceptsOneWidget getConfigurationContainer();

    AcceptsOneWidget getDetailContainer();

    AcceptsOneWidget getNetworkContainer();

    HasEnabled getLoginEnabled();

    HasEnabled getReloadEnabled();

    public interface Presenter {

        void reload();

        void showLoginDialog();

        void setPlace(TestbedSelectionPlace place);
    }
}
