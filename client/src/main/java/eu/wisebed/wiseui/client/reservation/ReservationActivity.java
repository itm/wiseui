package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;

public class ReservationActivity extends AbstractActivity implements Presenter {

    private ReservationView view;

    @Inject
    public ReservationActivity(final ReservationView view, final EventBus eventBus) {
        this.view = view;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view);
    }
}
