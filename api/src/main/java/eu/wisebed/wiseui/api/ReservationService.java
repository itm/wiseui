package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

import java.util.Date;
import java.util.List;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {

    SecretReservationKey makeReservation(
    		SecretAuthenticationKey key,
            String rsEndpointUrl,
            ReservationDetails data) 
    		throws 
            AuthenticationException, ReservationException,
            ReservationConflictException;

    List<PublicReservationData> getPublicReservations(
    		String rsEndpointUrl, 
    		Date current, 
    		Range range);

    List<ReservationDetails> getUserReservations(
    		List<SecretReservationKey> keys,
    		String rsEndpointUrl) 
    		throws
    		ReservationException;

    String deletReservation(
    		String sessionId, 
    		String rsEndpointUrl)
    		throws 
    		ReservationException;

    enum Range {
        ONE_DAY,
        WEEK,
        MONTH
    }
}
