package eu.wisebed.wiseui.server.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.testbed.api.rs.RSServiceHelper;
import eu.wisebed.testbed.api.rs.v1.AuthorizationExceptionException;
import eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData;
import eu.wisebed.testbed.api.rs.v1.Data;
import eu.wisebed.testbed.api.rs.v1.RS;
import eu.wisebed.testbed.api.rs.v1.RSExceptionException;
import eu.wisebed.testbed.api.rs.v1.ReservervationConflictExceptionException;
import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v211.SessionManagement;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.server.manager.ReservationServiceManager;
import eu.wisebed.wiseui.server.manager.SNAAManager;
import eu.wisebed.wiseui.server.util.APIKeysUtil;
import eu.wisebed.wiseui.server.util.WiseMLInfoExtractor;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.AuthenticationDetails;
import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;


@Singleton
public class ReservationServiceImpl extends PersistentRemoteService 
	implements ReservationService {	
	
	
	private static final long serialVersionUID = -7715272862718944674L;
	private static final Logger LOGGER = Logger.getLogger(
			ReservationServiceImpl.class.getName());

	private HibernateUtil gileadHibernateUtil = new HibernateUtil();
	/**
	 * Constructor
	 */
	@Inject
	public ReservationServiceImpl() {
		gileadHibernateUtil.setSessionFactory(
				WiseUiHibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = 
			new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore sps = new StatelessProxyStore();
		sps.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(sps);
		setBeanManager(persistentBeanManager);
	}

	/**
	 * Retrieves network from the WISEBED web services of a testbed.
	 * @return <code>ArrayList</code> with sensor information.
	 */
	public ArrayList<SensorDetails> getNodeList(
			final String sessionManagementEndpointUrl) {

		SessionManagement sessionManagement = WSNServiceHelper.
			getSessionManagementService(sessionManagementEndpointUrl); 
	    String serializedWiseML = sessionManagement.getNetwork();
		ArrayList<SensorDetails> nodeList= WiseMLInfoExtractor.
			getNodeList(serializedWiseML);
	    ReservationServiceManager.saveSensorInfrastructure(nodeList);
	    
	    return nodeList;
	}
	
	/**
	 * Make reservation of some nodes in a specific time span
	 * @param <code>sessionID</code> , current session ID.  
	 * @param <code>rsData</code> ,an <code>ReservationDetails</code> object 
	 * containing the necessary information to make a reservation.
	 */
	// TODO remove return make this method void
	public String makeReservation(
			final eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey secretAuthenticationKey,
			final String rsEndpointUrl,
			final ReservationDetails rsData)
		throws AuthenticationException,ReservationException,
			ReservationConflictException {
		
		// TODO after the joda implementation do we really need the duration 
		//to be sent from client or calculated right here?
		final long durationInSeconds =
			TimeUnit.SECONDS.toSeconds(rsData.getDuration());
		final DateTime startTime = new DateTime(rsData.getStartTime());
		final DateTime stopTime =  new DateTime(rsData.getStopTime());
		
		final List<SecretAuthenticationKey> secretAuthKeys = 
			new ArrayList<SecretAuthenticationKey>();
		SecretAuthenticationKey snaaKey = new SecretAuthenticationKey();
		snaaKey.setSecretAuthenticationKey(secretAuthenticationKey.
				getSecretAuthenticationKey());
		AuthenticationDetails authenticationDetails = 
			SNAAManager.fetchUserBySecretAuthenticationKey(
				secretAuthenticationKey.getSecretAuthenticationKey());
		snaaKey.setUrnPrefix(secretAuthenticationKey.getUrnPrefix());
		snaaKey.setUsername(secretAuthenticationKey.getUsername());
		secretAuthKeys.add(snaaKey);

		// reservation system proxy
		RS rs = RSServiceHelper.getRSService(rsEndpointUrl);

		// generate confidential reservation data
		ConfidentialReservationData reservationData = 
			new ConfidentialReservationData();
		Data data = new Data();
		data.setUrnPrefix(snaaKey.getUrnPrefix());
		data.setUsername(authenticationDetails.getUsername());
		reservationData.getData().add(data);
		reservationData.setUserData("WiseUi");
		reservationData.getNodeURNs().addAll(rsData.getNodes());
		
		DatatypeFactory datatypeFactory;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new ReservationException("An error occured while extracting" +
					" reservation from the database");
		}
		reservationData.setFrom(datatypeFactory.newXMLGregorianCalendar(
			startTime.toGregorianCalendar())
		);
		reservationData.setTo(datatypeFactory.newXMLGregorianCalendar(
			stopTime.toGregorianCalendar())
		);
		
		// retrieve secret reservation keys
		List<SecretReservationKey> secretReservationKeys = null;
		try {
			secretReservationKeys = rs.makeReservation(
					APIKeysUtil.copySnaaToRs(secretAuthKeys),
					reservationData
			);
		} catch (AuthorizationExceptionException e) {
			throw new AuthenticationException("Not authorized for reservation");
		} catch (RSExceptionException e) {
			throw new ReservationException("RS exception");
		} catch (ReservervationConflictExceptionException e) {
			throw new ReservationConflictException("Reservation conflict");
		}
		rsData.setSecretReservationKey(
				secretReservationKeys.get(0).getSecretReservationKey());
		rsData.setUrnPrefix(
				secretReservationKeys.get(0).getUrnPrefix());

		// save reservation
		ReservationServiceManager.saveReservation(
				authenticationDetails.getUserid(), rsData);
		
	    LOGGER.log(Level.INFO, "Successfully reserved the following nodes: {" + 
	    		rsData.getNodes() + "}" + "for " + durationInSeconds +
	    		" seconds");
	    
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
	public final ArrayList<ReservationDetails> getUserReservations( 
			final String sessionID) throws ReservationException {
		// TODO: Add functionality while integrating
		return null;
	}
	
	// TODO methods below that point save/fetch data to/from DB we need to place
	// some should be public others private . Those public should be placed to 
	// another class file exposed to other servlets
		
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
}