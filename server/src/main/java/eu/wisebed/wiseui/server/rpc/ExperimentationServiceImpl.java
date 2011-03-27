package eu.wisebed.wiseui.server.rpc;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.server.util.URLUtil;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.server.manager.ReservationServiceManager;
import eu.wisebed.wiseui.shared.ExperimentMessage;
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

		// fetch reservation from key
//		SecretReservationKey key = ReservationServiceManager.
//			fetchReservationByReservationID(reservationID);
//		if(key == null){
//			throw new ReservationException("Reservation not found");
//		}
		
		// format local endpoint url (for testing behind NAT must go !)
//		String endPointURL = "http://" + 
//		"94.64.211.89" + 
//		":" + 
//		URLUtil.getNextAvailablePort() + "/controller";

		// format local endpoint url (standard way)
//		String endPointURL=null;
//		try {
//			endPointURL = "http://" + 
//				InetAddress.getLocalHost().getCanonicalHostName() + 
//				":" + URLUtil.getNextAvailablePort() + "/controller";
//		} catch (UnknownHostException e) {
//			LOGGER.log(Level.FATAL, e);
//			throw new ExperimentationException("Could not publish local " +
//					"controller on" + endPointURL);
//		}
		
		// setup experiment controller
//		ExperimentController controller = new ExperimentController();
//		controller.setEndPointURL(endPointURL);
//		controller.setReservationID(reservationID);
//		List<SecretReservationKey> keys = new ArrayList<SecretReservationKey>();
//		keys.add(key);
//		controller.setKeys(keys);
		
		// publish controller
//		try{
//			controller.publish();
//		} catch (MalformedURLException e) {
//			LOGGER.log(Level.FATAL, e);
//			throw new ExperimentationException(
//					"Could not public local controller on " 
//					+ controller.getEndPointURL() + " (" + e.getMessage() + ")");
//					
//		}
//		LOGGER.log(Level.INFO,"Local controller published on url: " + 
//				controller.getEndPointURL());
		
		// start session management

		// controller found
//		controller.startSessionManagement();
		
		// add controller to the controllers list
//		experimentControllers.add(controller);		
	}
	
	/**
	 *  This method loads an experiment image on the web services.
	 *  @param <code>reservationID</code>, a reservation ID.
	 */
	@Override
	public void flashExperimentImage(final int reservationID)
			throws ReservationException, ExperimentationException {
		
		//	get image related file name from reservation
//		final String imageName = ReservationServiceManager
//			.fetchImageFileName(reservationID);
//		final String imageNameField = ReservationServiceManager
//			.fetchImageFileNameField(reservationID);
//
//		LOGGER.log(Level.INFO, "Image filename \"" + imageName +
//				"\" for reservation (" + reservationID +")");
//		LOGGER.log(Level.INFO, "Image nameField \"" + imageNameField +
//				"\" for reservation (" + reservationID +")");

		// Setup for flashing an image
		// form a node list
//		List<String> nodeURNs = ReservationServiceManager
//			.fetchNodeURNs(reservationID);
//		
//		LOGGER.log(Level.INFO, "Fetched " + nodeURNs.size() + " node URNs");
//		@SuppressWarnings("rawtypes")
//		List programIndices = new ArrayList();		
//		for(int i= 0;i < nodeURNs.size();i++){
//			LOGGER.log(Level.INFO,"Node URN fetched :" + nodeURNs.get(i));
//			programIndices.add(0);
//		}

		// TODO read from image BLOBs
//		File f = new File(imageName);
//		//String c = uploadedFilesContentType.get(imageNameField);
//
//		// setup image to flash
//		@SuppressWarnings("rawtypes")
//		List programs = new ArrayList();
//               try {
//			programs.add(Utils.readProgram(
//					f.getCanonicalPath(),
//			        "iSerial",
//			        "",
//			        "iSense",
//			        "1.0"
//			));
//		} catch (Exception e) {
//			LOGGER.log(Level.FATAL, e);
//			return null;
//		}
//		
//		ExperimentController controller = 
//			findOutputcontrollerByID(reservationID);
//		
//        jobs.submit(new Job(
//        		"flash nodes",
//                controller.getWSN().flashPrograms(
//                		nodeURNs, programIndices, programs),
//                nodeURNs,
//                Job.JobType.flashPrograms
//            ));
//        jobs.join();		
	}

	@Override
	public void terminateExperiment(final int reservationID)
			throws ExperimentationException {
		
//		// find output controller
//		ExperimentController controller 
//			= findExperimentControllerByID(reservationID);
//
//		if(controller == null){
//			throw new ExperimentationException("Unexpected. Controller not " +
//					"properly set on the server");
//		}
//
//		// try to free session management
//		controller.freeSessionManagement();
//
//		// remove the selected controller 
//		experimentControllers.remove(controller);		
	}
	
	/**
	 * Returns an undeliverd message back to the client
	 * @param <code>reservationID</code>, a reservation ID for an experiment
	 */
	@Override
	public ExperimentMessage getNextUndeliveredMessage(final int reservationID) 
		throws ExperimentationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Finds an outputcontroller by looking for it's ID in the controllers list. 
	 * @param <code>ID</code>, id of a controller.
	 * @return an <code>OutputController</code> object 
	 * or <code>null</code> if controller not found. 
	 */
	private ExperimentController findExperimentControllerByID(final int reservationID){
		LOGGER.log(Level.INFO, "Looking for controller with ID:" + reservationID );
		for(ExperimentController controller : experimentControllers){
			if(reservationID == controller.getReservationID()){
				LOGGER.log(Level.INFO, "Found controller with ID:" + reservationID + 
						" (" +controller.getEndPointURL() +")");
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
