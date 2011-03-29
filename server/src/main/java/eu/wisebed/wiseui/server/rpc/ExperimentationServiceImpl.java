package eu.wisebed.wiseui.server.rpc;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;
import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.Job;

import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.Program;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.server.util.ImageUtil;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.server.util.URLUtil;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.server.manager.ImageServiceManager;
import eu.wisebed.wiseui.server.manager.ReservationServiceManager;
import eu.wisebed.wiseui.server.manager.TestbedConfigurationManager;
import eu.wisebed.wiseui.server.model.Image;
import eu.wisebed.wiseui.shared.ExperimentMessage;
import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@Singleton
public class ExperimentationServiceImpl extends PersistentRemoteService 
	implements ExperimentationService {

	private static final long serialVersionUID = -6301493806193636782L;
	private static final Logger LOGGER = 
		Logger.getLogger(ExperimentationServiceImpl.class.getName());
	private List<ExperimentController> experimentControllers = 
		new ArrayList<ExperimentController>();
	private HibernateUtil gileadHibernateUtil = new HibernateUtil();
	private AsyncJobObserver jobs = new AsyncJobObserver(1, TimeUnit.MINUTES);
	private SessionManagement sessionManagement;


	@Inject
	ExperimentationServiceImpl(){
		gileadHibernateUtil.setSessionFactory(
				WiseUiHibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = 
			new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore sps = new StatelessProxyStore();
		sps.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(sps);
		setBeanManager(persistentBeanManager);
		
		// set controllers
		setExperimentControllers(new ArrayList<ExperimentController>());
	}

	/**
	 *  This methods binds a local controller instance for an experiment 
	 *  and invokes it's session management instance.
	 *  @param <code>reservationID</code>, a reservation ID.
	 */
	@Override
	public void bindAndStartExperimentController(final int reservationID) 
		throws ReservationException,ExperimentationException {
		
		LOGGER.log(Level.INFO,"Binding controller with id = " + reservationID);

		// fetch reservation from ID
		ReservationDetails reservation =
			ReservationServiceManager.fetchReservation(reservationID);
		if(reservation == null){
			throw new ReservationException("Reservation not found");
		}
		SecretReservationKey key = new SecretReservationKey();
		key.setSecretReservationKey(reservation.getSecretReservationKey());
		key.setUrnPrefix(reservation.getUrnPrefix());
		
		// format local endpoint url (standard way)
		String endPointURL=null;
		try {
			endPointURL = "http://" + 
				InetAddress.getLocalHost().getCanonicalHostName() + 
				":" + URLUtil.getPort() + "/controller"
				+ URLUtil.getRandomURLSuffix(key.getSecretReservationKey());
		} catch (UnknownHostException e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException("Could not publish local " +
					"controller on" + endPointURL);
		}
		
		// setup experiment controller
		ExperimentController controller = new ExperimentController();
		controller.setEndPointURL(endPointURL);
		controller.setReservationID(reservationID);
		List<SecretReservationKey> keys = new ArrayList<SecretReservationKey>();
		keys.add(key);
		controller.setKeys(keys);
		controller.setJobs(jobs);
		List<String> urnPrefixList = new ArrayList<String>();
		urnPrefixList.add(reservation.getUrnPrefix());
		List<TestbedConfiguration> testbed = 
			TestbedConfigurationManager.fetchTestbedByUrn(urnPrefixList);
		String sessionManagmentURL = 
			testbed.get(0).getSessionmanagementEndpointUrl();
		sessionManagement = 
			WSNServiceHelper.getSessionManagementService(sessionManagmentURL);
		controller.setSessionManagement(sessionManagement);
		
		// publish controller
		try{
			controller.publish();
		} catch (MalformedURLException e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException(
					"Could not public local controller on " 
					+ controller.getEndPointURL() + " (" + e.getMessage() + ")");
					
		}
		LOGGER.log(Level.INFO,"Local controller published on url: " + 
				controller.getEndPointURL());
		
		// start session management

		// controller found
		controller.startSessionManagement();
		
		// add controller to the controllers list
		experimentControllers.add(controller);		
	}
	
	/**
	 *  This method loads an experiment image on the web services.
	 *  @param <code>reservationID</code>, a reservation ID.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void flashExperimentImage(final int reservationID)
			throws ReservationException, ExperimentationException {
		
		LOGGER.log(Level.INFO,"Flashing image for controller with id = " + reservationID);

		// get reservation
		ReservationDetails reservation =
			ReservationServiceManager.fetchReservation(reservationID);

		//	get image related file name from reservation
		final String filename = reservation.getImageFileName();
		final Image image = 
			ImageServiceManager.fetchImageByFilename(filename);
	
		LOGGER.log(Level.INFO, "Image filename \"" + filename +
				"\" for reservation (" + reservationID +")");

		// Setup for flashing an image
		// form a node list
		List<String> nodeURNs = new ArrayList<String>();		
		for(SensorDetails sensor : reservation.getSensors()){
			nodeURNs.add(sensor.getUrn());
		}
		
		LOGGER.log(Level.INFO, "Fetched " + nodeURNs.size() + " node URNs");
		@SuppressWarnings("rawtypes")
		List programIndices = new ArrayList();		
		for(int i= 0;i < nodeURNs.size();i++){
			LOGGER.log(Level.INFO,"Node URN fetched :" + nodeURNs.get(i));
			programIndices.add(0);
		}

		// setup image to flash
		List<Program> programs = new ArrayList<Program>();
               try {
			programs.add(ImageUtil.readImage(image,
			        "iSerial",
			        "",
			        "iSense",
			        "1.0"
			));
		} catch (Exception e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException();
		}
		// TODO CHECK JOBS FOR NULL !! 
		ExperimentController controller = 
			findExperimentControllerByID(reservationID);
		
        jobs.submit(new Job(
        		"flash nodes",
                controller.getWsn().flashPrograms(
                		nodeURNs, programIndices, programs),
                nodeURNs,
                Job.JobType.flashPrograms
            ));
        jobs.join();		
	}

	@Override
	public void terminateExperiment(final int reservationID)
			throws ExperimentationException {
		
		LOGGER.log(Level.INFO,"Terminating controller with id = " + reservationID);

//		// find output controller
		ExperimentController controller 
			= findExperimentControllerByID(reservationID);

		if(controller == null){
			throw new ExperimentationException("Unexpected. Controller not " +
					"properly set on the server");
		}

		// try to free session management
		controller.freeSessionManagement();

		// remove the selected controller 
		experimentControllers.remove(controller);		
	}
	
	/**
	 * Returns an undeliverd message back to the client
	 * @param <code>reservationID</code>, a reservation ID for an experiment
	 */
	@Override
	public ExperimentMessage getNextUndeliveredMessage(final int reservationID) 
		throws ExperimentationException {
		
		ExperimentController controller = findExperimentControllerByID(reservationID);
		
		if(controller == null){
			throw new ExperimentationException("Unexpected. Controller not " +
					"properly set on the server");
		}
		
		if(controller.getUndelivered() == null){
			throw new ExperimentationException("Unexpected. Message queue not " +
						"properly set on the controller with id #" + reservationID);
		}	
		
		ExperimentMessage message = controller.getUndelivered().poll();
		message.setReservationID(reservationID);
		
		return message;
	}
	
	/**
	 * Finds an outputcontroller by looking for it's ID in the controllers list. 
	 * @param <code>ID</code>, id of a controller.
	 * @return an <code>OutputController</code> object 
	 * or <code>null</code> if controller not found. 
	 */
	private ExperimentController findExperimentControllerByID(final int reservationID){
		for(ExperimentController controller : experimentControllers){
			if(reservationID == controller.getReservationID()){
				return controller;
			}
		}
		return null;
	}

	public void setExperimentControllers(
			List<ExperimentController> experimentControllers) {
		this.experimentControllers = experimentControllers;
	}

	public List<ExperimentController> getExperimentControllers() {
		return experimentControllers;
	}
}
