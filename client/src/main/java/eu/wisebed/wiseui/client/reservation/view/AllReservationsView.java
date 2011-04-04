package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(AllReservationsViewImpl.class)
public interface AllReservationsView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	
	public interface Presenter{
	}
}