package eu.wisebed.wiseui.api;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {
	ArrayList<SensorDetails> getNodeList(String sessionManagementEndpointUrl)
		throws IllegalArgumentException;
	String makeReservation(SecretAuthenticationKey secretAuthenticationKey,
			String rsEndpointUrl, ReservationDetails data)
		throws AuthenticationException, ReservationException,
			ReservationConflictException;
	List<ReservationDetails> getUserReservations(SecretAuthenticationKey secretAuthenticationKey) 
		throws ReservationException;
	String cancelReservation(String username,int reservationID) 
		throws ReservationException;
}
