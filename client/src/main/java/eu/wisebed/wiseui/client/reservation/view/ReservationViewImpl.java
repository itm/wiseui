package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ReservationViewImpl extends Composite implements ReservationView {

    interface ReservationViewImplUiBinder extends
            UiBinder<Widget, ReservationViewImpl> {
    }

    private static ReservationViewImplUiBinder uiBinder = GWT
            .create(ReservationViewImplUiBinder.class);

    public ReservationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {

    }

}
