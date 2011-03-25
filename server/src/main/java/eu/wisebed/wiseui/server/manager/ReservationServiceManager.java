package eu.wisebed.wiseui.server.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.AuthenticationDetails;
import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;

public class ReservationServiceManager {
	
	/**
	 * Temporarily populating sensors' table. Just keep a record of the 
	 * existing sensor infrastructure for development needs.
	 */
	public final static void saveSensorInfrastructure(
			final ArrayList<SensorDetails> sensors){

		final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();
		session.beginTransaction();
		Criteria maxCriteria = session.createCriteria(SensorDetails.class)
			.setProjection(Projections.max("sensorid"));
		Integer id = (Integer) maxCriteria.uniqueResult();
		if(id==null) id = 0;
		for (int i=id, j=0; i<id + sensors.size(); i++, j++){
			SensorDetails sensor = new SensorDetails();
			Criteria existCriteria = session.createCriteria(SensorDetails.class)
				.add(Restrictions.eq("urn", sensors.get(j).getUrn()));
			if(existCriteria.uniqueResult()!=null) continue;
			sensor.setUrn(sensors.get(j).getUrn());
			sensor.setType(sensors.get(j).getType());
			sensor.setDescription(sensors.get(j).getDescription());
			sensor.setSensorid(i+1);
			session.saveOrUpdate(sensor);
		}
		session.getTransaction().commit();
	}

	/**
	 * Persist reservation details given. Also take care of constructing the
	 * corresponding associations between sensors and reservations. Each 
	 * reservation could bind multiple sensors. First fetch all sensor details
	 * and then create the association and store reservation details.
	 * @param <code>userID</code>, a user ID.
	 * @param <code>reservation</code>, the reservation to save into the 
	 * persistent store.
	 */
	@SuppressWarnings("unchecked")
	public final static void saveReservation(final int userID,
			final ReservationDetails reservation){

		final AuthenticationDetails user = new AuthenticationDetails();
		
		// Create association with reservation and user
		user.setUserid(userID);
	    reservation.setUser(user);

		// Begin transaction to save reservation and its association
	    final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();
		session.beginTransaction();
		final Criteria criteria = session.createCriteria(SensorDetails.class);
		Disjunction disjunction = Restrictions.disjunction();
		for (int i=0; i<reservation.getNodes().size(); i++){
			Criterion urn = Restrictions.eq("urn",reservation.getNodes().get(i));
			disjunction.add(urn);
		}
		criteria.add(disjunction);
		final List<SensorDetails> sensorDetails = 
			(List<SensorDetails>) criteria.list();
		
		// For all sensors fetched create the association with reservation
		for(SensorDetails s: sensorDetails){
		    reservation.addSensor(s);
		    session.save(reservation);
		}
		session.getTransaction().commit();
	}
	
	/**
	 * Given a user ID fetch all reservations have been made for client with
	 * this ID
	 * @param <code>userID</code>, a user ID.
	 * @return a <code>List</code> containing <code>ReservationDetails</code>
	 */
	@SuppressWarnings("unchecked")
	public final static ArrayList<ReservationDetails> 
		fetchAllReservationsByUser(final int userID){
	    final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();
	    session.beginTransaction();
	    final Criteria criteria = session.createCriteria(
	    		ReservationDetails.class).add(Restrictions.eq(
	    				"user.userid", userID));
	    final ArrayList<ReservationDetails> reservations = 
	    	(ArrayList<ReservationDetails>) criteria.list();
	    return reservations;
	}
}
