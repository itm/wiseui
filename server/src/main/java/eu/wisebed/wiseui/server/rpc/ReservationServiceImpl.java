package eu.wisebed.wiseui.server.rpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.SecretReservationKey;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.api.ReservationService;
//import eu.wisebed.wiseui.server.util.WiseMLInfoExtractor;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SensorDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;


@Singleton
public class ReservationServiceImpl extends RemoteServiceServlet implements ReservationService {

	private static final long serialVersionUID = -7715272862718944674L;

	@Inject
	public ReservationServiceImpl() {
	}

	/**
	 * Retrieves network from the WISEBED web services of a testbed.
	 * @return <code>ArrayList</code> with sensor information.
	 */
	public List<Node> getNodeList(
			final String sessionManagementEndpointUrl) {

		SessionManagement sessionManagement = WSNServiceHelper.
			getSessionManagementService(sessionManagementEndpointUrl); 
	    String serializedWiseML = sessionManagement.getNetwork();

        // TODO FIXME!!!
        ArrayList<Node> nodeList= null;
        //WiseMLInfoExtractor.
		//	getNodeList(serializedWiseML);
	    
	    return nodeList;
	}
	
	/**
	 * Make reservation of some nodes in a specific time span
	 * @param <code>sessionID</code> , current session ID.  
	 * @param <code>rsData</code> ,an <code>ReservationDetails</code> object 
	 * containing the necessary information to make a reservation.
	 */
	// TODO remove return make this method void
	public String makeReservation(SecretAuthenticationKey secretAuthenticationKey,
                           String rsEndpointUrl, ReservationDetails data)
            throws AuthenticationException, ReservationException,
            ReservationConflictException {
		// TODO: Add functionality while integrating
		return "OK";
	}
	
	/**
	 * Cancels a reservation on services and deletes the appropriate reservation
	 * entry from the persistent model
	 * @param <code>sessionID</code>, current session ID.
	 * @param <code>reservationID</code>, ID of the cancelled reservation.
	 */
	public String cancelReservation(final String sessionID,
			final int reservationID) throws ReservationException {
		// TODO: Add functionality while integrating
		return null;
	}
	
	/**
	 * Get all reservations made by a user.
	 * @param <code>sessionID</code>, current session ID in order to identify 
	 * the user.
	 * @return an <code>ArrayList</code> of <code>ReservationDetails</code>. 
	 * objects that are the reservations madey by a user.
	 */
	public final List<ReservationDetails> getUserReservations(
			final SecretAuthenticationKey key) throws ReservationException {
		// TODO: Add functionality while integrating
		return null;
	}
	
	// TODO methods below that point save/fetch data to/from DB we need to place
	// some should be public others private . Those public should be placed to 
	// another class file exposed to other servlets
	
	/**
	 * Persist reservation details given. Also take care of constructing the
	 * corresponding associations between sensors and reservations. Each 
	 * reservation could bind multiple sensors. First fetch all sensor details
	 * and then create the association and store reservation details.
	 * @param <code>userID</code>, a user ID.
	 * @param <code>reservation</code>, the reservation to save into the 
	 * persistent store.
	 */
	public final static void saveReservation(final int userID,
			final ReservationDetails reservation){

		// TODO: Add functionality while integrating
	}
		
	/**
	 * Given a list of the sensor IDs fetch all sensor details with the
	 * corresponding IDs. 
	 * @param <code>sensorIDs</code>, a list of integers as sensor IDs.
	 * @return a <code>Set</code> of <code>SensorDetails</code> objects.
	 */
	public static final Set<SensorDetails> fetchSensors(
			final ArrayList<Integer> sensorIDs){
		// TODO: Add functionality while integrating
		return null;
	}
		
	/**
	 * Given a reservation ID fetch the corresponding secret reservation keys.
	 * @param <code>reservationID</code>, ID of a reservationID.
	 * @return a <code>SecretReservationKey</code> object or <code>null</code> 
	 * if reservation does not exist
	 */
	public static final SecretReservationKey fetchSecretReservationKey(
			final int reservationID){
		// TODO: Add functionality while integrating
		return null;
	}
	
	/**
	 * Given the ID of a reservation delete the corresponding entry from the
	 * persistent model. Also get the appropriate reservation details from 
	 * hibernate's cache as far as the reservations have been already loaded
	 * in the current session.
	 * @param <code>reservationID</code>, ID of a reservationID.
	 */
	private static final void deleteReservation(final int reservationID){
		// TODO: Add functionality while integrating
	}
	
	/**
	 * Given a user ID fetch all reservations have been made for client with
	 * this ID
	 * @param <code>userID</code>, a user ID.
	 * @return a <code>List</code> containing <code>ReservationDetails</code>
	 */
	public final static ArrayList<ReservationDetails> 
		fetchAllReservationsForUser(final int userID){
		// TODO: Add functionality while integrating
		return null;
	}

	/**
	 * Temporarily populating sensors' table. Just keep a record of the 
	 * existing sensor infrastructure for development needs.
	 */
	private final static void saveSensorInfrastructure(
			final ArrayList<SensorDetails> sensors){
		// TODO: Add functionality while integrating
	}
	
	/**
	 * Given a a reservaition ID fetch the imageFileNameField of the reservation
	 * made by this user.
	 * @param <code>reservationID</code>, a reservation ID.
	 * @return experiment image filename
	 */
	public final static String fetchImageFileNameField(
			final int reservationID) {
		// TODO: Add functionality while integrating
		return null;
	}

	/**
	 * Given a user ID fetch last reservations' ID
	 * @param <code>userID</code>, a user ID.
	 * @return a reservation ID if exists, else -1.
	 */
	public final static Integer fetchReservationID(final int userID){
		// TODO: Add functionality while integrating
		return null;
	}
	
	/**
	 * Given a a reservation ID fetch the related node URNs.
	 * by this user.
	 * @param <code>reservationID</code>, a reservation ID.
	 * @return a <code>List</code> of node URNs.
	 */
	public final static List<String> fetchNodeURNs(final int reservationID){
		// TODO: Add functionality while integrating
		return null;
	}

	/**
	 * Given a a reservation ID fetch the imageFilename of the reservation made
	 * by this user.
	 * @param <code>reservationID</code>, a reservation ID.
	 * @return a <code>List</code> of node URNs.
	 */
	public final static String fetchImageFileName(final int reservationID){
		// TODO: Add functionality while integrating
		return null;
	}

    public List<PublicReservationData> getPublicReservations(Date from, Date to) {
        return new ArrayList<PublicReservationData>();
    }
}