package eu.wisebed.wiseui.client.navigation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class NavigationViewImpl extends Composite implements NavigationView {

    interface NavigationViewImplUiBinder extends
            UiBinder<Widget, NavigationViewImpl> {
    }

    private static NavigationViewImplUiBinder uiBinder = GWT
            .create(NavigationViewImplUiBinder.class);

    @UiField
    TabBar navigationBar;

    private Presenter presenter;

    public NavigationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        navigationBar.setWidth("100%");
    }

    @UiHandler("navigationBar")
    public void handleSelection(final SelectionEvent<Integer> event) {
        presenter.selected(event.getSelectedItem());
    }

    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    public void add(final String name) {
        navigationBar.addTab(name);
    }

    public void select(final Integer index) {
        navigationBar.selectTab(index, false);
    }
}
