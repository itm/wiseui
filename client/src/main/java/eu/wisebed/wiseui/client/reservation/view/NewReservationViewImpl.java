package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class NewReservationViewImpl extends Composite implements NewReservationView {

	@UiTemplate("NewReservationViewImpl.ui.xml")
    interface NewReservationViewImplUiBinder extends
            UiBinder<Widget, NewReservationViewImpl> {
    }

	private static NewReservationViewImplUiBinder uiBinder = GWT
    	.create(NewReservationViewImplUiBinder.class);

	public NewReservationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter presenter;
	
	public void setPresenter(final Presenter presenter){
		this.presenter = presenter;
	}
	
}
