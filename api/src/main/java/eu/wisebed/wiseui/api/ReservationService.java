package eu.wisebed.wiseui.api;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;

import java.util.Date;
import java.util.List;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {

    List<Node> getNodeList(String sessionManagementEndpointUrl) throws IllegalArgumentException;

    String makeReservation(SecretAuthenticationKey secretAuthenticationKey,
                           String rsEndpointUrl, ReservationDetails data)
            throws AuthenticationException, ReservationException,
            ReservationConflictException;

    List<PublicReservationData> getPublicReservations(String rsEndpointUrl, Date from, Date to);
    	
    List<ReservationDetails> getUserReservations(SecretAuthenticationKey key) throws ReservationException;

    String cancelReservation(String username, int reservationID) throws ReservationException;

}
