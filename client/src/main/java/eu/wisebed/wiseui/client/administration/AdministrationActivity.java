package eu.wisebed.wiseui.client.administration;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.AdministrationView.Presenter;

public class AdministrationActivity extends AbstractActivity implements Presenter {

    private AdministrationView view;

    @Inject
    public AdministrationActivity(final AdministrationView view) {
        this.view = view;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view);
    }
}
