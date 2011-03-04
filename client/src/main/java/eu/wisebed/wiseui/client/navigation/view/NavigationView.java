package eu.wisebed.wiseui.client.navigation.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface NavigationView extends IsWidget {

    void select(Integer index);

    void add(String link);

    void setPresenter(Presenter presenter);

    public interface Presenter {

        void selected(Integer index);
    }
}
