package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
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

    /**
     * Make a reservation for the given time span and with the given credentials.
     *
     * @param rsEndpointUrl reservation endpoint URL
     * @param secretAuthenticationKeys List of {@link SecretAuthenticationKey}s
     * @param confidentialReservationData {@link ConfidentialReservationData} to perform a reservation
     * @return List of {@link SecretReservationKey}s
     * @throws AuthenticationException
     * @throws ReservationException
     * @throws ReservationConflictException
     */
    List<SecretReservationKey> makeReservation(
            String rsEndpointUrl,
            List<SecretAuthenticationKey> secretAuthenticationKeys,
            ConfidentialReservationData confidentialReservationData)
            throws
            AuthenticationException, ReservationException,
            ReservationConflictException;

    /**
     * Get public reservations of all the testbed's users.
     *
     * @param rsEndpointUrl reservation endpoint URL
     * @param current Pivot date for applying the range
     * @param range Date range (e.g. day, week, month)
     * @return List of public reservation data
     * @throws ReservationException
     */
    List<PublicReservationData> getPublicReservations(
            String rsEndpointUrl,
            Date current,
            Range range) throws ReservationException;

    List<ConfidentialReservationData> getPrivateReservations(
            SecretAuthenticationKey key,
            String rsEndpountUrl,
            Date current,
            Range range)
            throws
            AuthenticationException, ReservationException;

    List<ConfidentialReservationData> getPrivateReservations(
            SecretAuthenticationKey key,
            String rsEndpountUrl,
            Date from,
            Date to)
            throws
            AuthenticationException, ReservationException;

    String deleteReservation(
            SecretReservationKey key,
            String rsEndpointUrl)
            throws
            ReservationException;

    enum Range {

        ONE_DAY,
        WEEK,
        MONTH
    }
}
