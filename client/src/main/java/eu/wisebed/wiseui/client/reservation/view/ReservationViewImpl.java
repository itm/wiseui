package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class ReservationViewImpl extends Composite implements ReservationView {

	@UiTemplate("ReservationViewImpl.ui.xml")
    interface ReservationViewImplUiBinder extends
            UiBinder<Widget, ReservationViewImpl> {
    }

	private static ReservationViewImplUiBinder uiBinder = GWT
    	.create(ReservationViewImplUiBinder.class);

	public ReservationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private Presenter presenter;

    @UiField
    SimplePanel reservationPanel;
    @UiField
    SimplePanel nodeSelectionPanel;

    public SimplePanel getReservationPanel() {
        return reservationPanel;
    }

    public SimplePanel getNodeSelectionPanel() {
        return nodeSelectionPanel;
    }

    public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

}