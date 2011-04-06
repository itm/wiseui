package eu.wisebed.wiseui.client.reservation.view;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.dto.PublicReservationData;

@ImplementedBy(PublicReservationsViewImpl.class)
public interface PublicReservationsView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	void renderPublicReservations(List<PublicReservationData> publicReservations);
	Date getFrom();
	Date getTo();
	
	public interface Presenter{
	}
}