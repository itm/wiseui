package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class AllReservationsViewImpl extends Composite implements AllReservationsView{
	
	@UiTemplate("AllReservationsViewImpl.ui.xml")
    interface AllReservationsViewImplUiBinder extends
            UiBinder<Widget, AllReservationsViewImpl> {
    }

	private static AllReservationsViewImplUiBinder uiBinder = GWT
    	.create(AllReservationsViewImplUiBinder.class);

	public AllReservationsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter presenter;
	
	public void setPresenter(final Presenter presenter){
		this.presenter = presenter;
	}
 
}
