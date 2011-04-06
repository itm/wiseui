package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView.Presenter;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;


public class PublicReservationsPresenter implements Presenter{

	private final PublicReservationsView view;
	private final ReservationServiceAsync RSService;

	@Inject
	public PublicReservationsPresenter(final PublicReservationsView view, final ReservationServiceAsync RSService){
		this.view = view;
		this.RSService = RSService;
		getPublicReservations();
	}
	
	private final void getPublicReservations(){
		// FIXME: Get testbed selected from testbeds' side pane.
		final String rsEndpointUrl = "http://wisebed.itm.uni-luebeck.de:8889/rs";
		final Date from = view.getFrom(); 
		final Date to = view.getTo();
		RSService.getPublicReservations(rsEndpointUrl, from, to, new AsyncCallback<List<PublicReservationData>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc while getting public reservations");
			}
			public void onSuccess(final List<PublicReservationData> publicReservations){
				if(publicReservations == null || publicReservations.isEmpty()){
					GWT.log("Failed when looking for public reservations");

				}else{
					view.renderPublicReservations(publicReservations);
				}
			}
		});
	}
}
