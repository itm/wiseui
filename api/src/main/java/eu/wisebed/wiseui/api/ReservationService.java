package eu.wisebed.wiseui.api;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {
	ArrayList<SensorDetails> getNodeList()
		throws IllegalArgumentException;
	String makeReservation(String username, ReservationDetails data)
		throws AuthenticationException, ReservationException,
			ReservationConflictException;
	ArrayList<ReservationDetails> getUserReservations(String username) 
		throws ReservationException;
	String cancelReservation(String username,int reservationID) 
		throws ReservationException;
}
