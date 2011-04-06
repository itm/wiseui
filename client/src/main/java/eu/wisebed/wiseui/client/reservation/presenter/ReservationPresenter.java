package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.client.reservation.common.Messages;
import eu.wisebed.wiseui.client.reservation.event.MissingReservationParametersEvent;
import eu.wisebed.wiseui.client.reservation.event.MissingReservationParametersEventHandler;

import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEventHandler;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEventHandler;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ReservationPresenter implements Presenter, MissingReservationParametersEventHandler, 
	ReservationSuccessEventHandler, ReservationFailedEventHandler{

	private final ReservationView view;
	private ReservationPlace place;
	private ReservationServiceAsync reservationService;
	private PlaceController placeController;
	private EventBus eventBus;
	private WiseUiGinjector injector;

	@Inject
	public ReservationPresenter(final WiseUiGinjector injector, final ReservationServiceAsync reservationService,
			final ReservationView view,
			final PlaceController placeController,
			final EventBus eventBus){
		this.injector = injector;
		this.reservationService = reservationService;
		this.view = view;
		this.placeController = placeController;
		this.eventBus = eventBus;
	}
	
    @Override
    public void setPlace(final ReservationPlace place) {
    	this.place = place;
    }
    
    public ReservationPlace getPlace(){
    	return place;
    }

	public void bindEnabledViewEvents() {
		eventBus.addHandler(MissingReservationParametersEvent.TYPE, this);
		eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
		eventBus.addHandler(ReservationFailedEvent.TYPE, this);
	}
	
	/**
	 * Makes an Asynchronous callback to server asking for the nodes located in 
	 * the network. Result consists of an array with all sensors' useful 
	 * information.
	 */
	public void getNetwork(final String sessionManagementEndpointUrl) {
		reservationService.getNodeList(sessionManagementEndpointUrl, new AsyncCallback<List<Node>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc");
			}
			public void onSuccess(List<Node> nodeList){
				if(nodeList==null){
					MessageBox.error(Messages.NO_NODES_RETURNED_TITLE,
							Messages.NO_NODES_RETURNED, null, null);
				}else{
                    // TODO FIXME!
					//injector.getNewReservationView().renderNodes(nodeList);
				}
			}
		});
	}
	
	/**
	 * Sends reservation details to server and books a new reservation.
	 */
	public void makeReservation(){
		// FIXME: Get testbed selected from testbed panel
		TestbedConfiguration bed = new TestbedConfiguration();
		final String rsEndpointUrl = bed.getRsEndpointUrl();
		final String urnPrefix = bed.getUrnPrefixList().get(0); 	
		final AuthenticationManager auth = injector.getAuthenticationManager();
		SecretAuthenticationKey secretAuthKey = auth.getKeyHash().get(urnPrefix);
		// FIXME: Retrieve reservation details from google cal;
		final ReservationDetails data = null;
		reservationService.makeReservation(secretAuthKey, rsEndpointUrl, data, new AsyncCallback<String>(){ 
				public void onFailure(Throwable caught){
					if(caught instanceof AuthenticationException){
						GWT.log("User not authorized to make reservations");
					}else if(caught instanceof ReservationException){
						GWT.log("RS system failed");
					}else if(caught instanceof ReservationConflictException){
						GWT.log("Cannot make reservations some nodes are " +
								"already reserved");
					}else{
						GWT.log("Error occured while contacting the server");
					}
					eventBus.fireEvent(new ReservationFailedEvent());
				}
				public void onSuccess(String result) {
					eventBus.fireEvent(new ReservationSuccessEvent());
				}
			});
	}

	public void onMissingReservationParameters( final MissingReservationParametersEvent event){
		MessageBox.error(Messages.MISSING_RESERVATION_PARAMETERS_TITLE, Messages.MISSING_RESERVATION_PARAMETERS, null, 
				null);		
	}

	public void onReservationSuccess(final ReservationSuccessEvent event){
		MessageBox.success(Messages.RESERVATION_SUCCESS_TITLE, Messages.RESERVATION_SUCCESS, null);			
	}
	
	public void onReservationFailed(final ReservationFailedEvent event){
		MessageBox.error(Messages.RESERVATION_FAILED_TITLE, Messages.RESERVATION_FAILED, null, null);
	}
}