/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.rpc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.testbed.api.rs.RSServiceHelper;
import eu.wisebed.testbed.api.rs.v1.AuthorizationExceptionException;
import eu.wisebed.wiseui.shared.dto.Data;
import eu.wisebed.testbed.api.rs.v1.RS;
import eu.wisebed.testbed.api.rs.v1.RSExceptionException;
import eu.wisebed.testbed.api.rs.v1.ReservervationConflictExceptionException;
import eu.wisebed.testbed.api.rs.v1.GetReservations;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.server.util.APIKeysUtil;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@Singleton
public class ReservationServiceImpl extends RemoteServiceServlet implements ReservationService {

	private static final long serialVersionUID = -7715272862718944674L;

	private static final Logger LOGGER =
		LoggerFactory.getLogger(ReservationServiceImpl.class);

	private final Mapper mapper;

	@Inject
	public ReservationServiceImpl(final Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Make reservation of some nodes in a specific time span
	 *
	 * @param data    ,an <code>ReservationDetails</code> object
	 *                               containing the necessary information to make a reservation.
	 */
	public ConfidentialReservationData makeReservation(SecretAuthenticationKey secretAuthenticationKey,
			String rsEndpointUrl, ReservationDetails rsData)
	throws AuthenticationException, ReservationException{

		// start and stop time
		final DateTime startTime = new DateTime(rsData.getStartTime());
		final DateTime stopTime =  new DateTime(rsData.getStopTime());

		// make a secret authentication key list with the key sent
		final List<eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey> secretAuthKeys = 
			new ArrayList<eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey>();
		eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey snaaKey = 
			new eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey();
		snaaKey.setSecretAuthenticationKey(secretAuthenticationKey.getSecretAuthenticationKey());
		snaaKey.setUrnPrefix(rsData.getUrnPrefix());
		snaaKey.setUsername(secretAuthenticationKey.getUsername());
		secretAuthKeys.add(snaaKey);

		// reservation system proxy
		final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);

		// make confidential reservation data object
		eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData reservationData 
		= new eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData();
		eu.wisebed.testbed.api.rs.v1.Data data = new eu.wisebed.testbed.api.rs.v1.Data();
		data.setUrnPrefix(snaaKey.getUrnPrefix());
		data.setUsername(secretAuthenticationKey.getUsername());
		reservationData.getData().add(data);
		reservationData.setUserData(secretAuthenticationKey.getUsername());
		reservationData.getNodeURNs().addAll(rsData.getNodes());

		// use datatypefactory to 
		DatatypeFactory datatypeFactory;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new ReservationException("An error occured while extracting reservation from the database");
		}
		reservationData.setFrom(datatypeFactory.newXMLGregorianCalendar(startTime.toGregorianCalendar()));
		reservationData.setTo(datatypeFactory.newXMLGregorianCalendar(stopTime.toGregorianCalendar()));

		// retrieve secret reservation keys
		List<eu.wisebed.testbed.api.rs.v1.SecretReservationKey> secretReservationKeys = null;
		try {

			// make reservation and retrieve secret reservation keys from rs
			secretReservationKeys = rs.makeReservation(APIKeysUtil.copySnaaToRs(secretAuthKeys),
					reservationData);

			// reformat reservation confidential data , map it and return it
			data.setSecretReservationKey(secretReservationKeys.get(0).getSecretReservationKey());
			data.setUrnPrefix(secretReservationKeys.get(0).getUrnPrefix());
			reservationData.getData().clear();
			reservationData.getData().add(data);
			ConfidentialReservationData clientReservationData = 
				mapper.map(reservationData, ConfidentialReservationData.class);
			Data clientData = mapper.map(data, Data.class);
			clientReservationData.getData().add(clientData);
			LOGGER.debug("Succesfull Reservation \n" + clientReservationData.toString());

			return clientReservationData;
		} catch (AuthorizationExceptionException e) {
			throw new AuthenticationException("Not authorized for reservation");
		} catch (RSExceptionException e) {
			throw new ReservationException("RS exception");
		} catch (ReservervationConflictExceptionException e) {
			throw new ReservationException("Reservation conflict occured");
		}
	}

	/**
	 * Get public reservations of all the testbed's users
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	public List<PublicReservationData> getPublicReservations(final String rsEndpointUrl,final Date current,final Range range) 
	throws ReservationException {

		// check input arguments
		try{
			ifNullOrEmptyArgument(rsEndpointUrl, "Reservation Service Endpoint URL not set");
			ifNullArgument(current, "Current date is not set");
		}catch(RuntimeException cause){
			throw new ReservationException(cause.getMessage());
		}

		LOGGER.debug("current=" + current);

		/* Convert from range to date to avoid client-site date calculation */
		Date start = null;
		Date end = null;
		final Calendar calendar = Calendar.getInstance();
		if (Range.ONE_DAY == range) {
			calendar.setTime(current);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			start = current;
			end = calendar.getTime();
		} else if (Range.WEEK == range) {
			calendar.setTime(current);
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			start = current;
			end = calendar.getTime();
		} else if (Range.MONTH == range) {
			calendar.setTime(current);
			final int firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
			start = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
			end = calendar.getTime();
		}

		LOGGER.info("getPublicReservations( start: " + start + ", end: " + end + " )");

		final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);
		List<eu.wisebed.testbed.api.rs.v1.PublicReservationData> resultList = null;
		try {
			resultList = rs.getReservations(
					convertDate2XmlGregorianCalendar(start),
					convertDate2XmlGregorianCalendar(end));
		} catch (RSExceptionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ArrayList<PublicReservationData>(Lists.transform(resultList,
				new Function<eu.wisebed.testbed.api.rs.v1.PublicReservationData, PublicReservationData>() {
			@Override
			public PublicReservationData apply(final eu.wisebed.testbed.api.rs.v1.PublicReservationData r) {
				final PublicReservationData publicReservationData;
				publicReservationData = mapper.map(r, PublicReservationData.class);
				return publicReservationData;
			}
		}));
	}

	/**
	 * Get confidential reservation data of user. Current date & range provided.
	 * @param key
	 * @param rsEndpointUrl
	 * @param current
	 * @param range
	 * @return a <code>List</code> of <code>ConfidentialReservationData</code>
	 * @throws ReservationException
	 * @throws AuthenticationException 
	 */
	public List<ConfidentialReservationData> getPrivateReservations(final SecretAuthenticationKey key, 
			final String rsEndpointUrl,final Date current, final Range range) throws AuthenticationException,ReservationException{

		/* Convert from range to date to avoid client-site date calculation */
		Date from = null;
		Date to = null;
		final Calendar calendar = Calendar.getInstance();
		if (Range.ONE_DAY == range) {
			calendar.setTime(current);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			from = current;
			to = calendar.getTime();
		} else if (Range.WEEK == range) {
			calendar.setTime(current);
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			from = current;
			to = calendar.getTime();
		} else if (Range.MONTH == range) {
			calendar.setTime(current);
			final int firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
			from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
			to = calendar.getTime();
		}
		
		// call getPrivateReservations with distinct from,to date objects
		return getPrivateReservations(key, rsEndpointUrl, from, to);
	}

	/**
	 * Get confidential reservation data of user. From and to dates are provided.
	 * @param key
	 * @param rsEndpointUrl
	 * @param from
	 * @param to
	 * @return a <code>List</code> of <code>ConfidentialReservationData</code>
	 */
	public List<ConfidentialReservationData> getPrivateReservations(
			final SecretAuthenticationKey key, final String rsEndpointUrl, final Date from,
			final Date to) throws AuthenticationException, ReservationException {

		// check input arguments
		try{
			ifNullArgument(key,"SecretAuthenticationKey is null");
			ifNullOrEmptyArgument(rsEndpointUrl, "Reservation Service Endpoint URL not set");
			ifNullArgument(from, "From date is not set");
			ifNullArgument(to,"To date is not set");
		}catch(RuntimeException cause){
			throw new ReservationException(cause.getMessage());
		}

		// start and end Dates are provided by client
		final Date start = from;
		final Date end = to;
		LOGGER.info("getPrivateReservations( start: " + start + ", end: " + end + " )");

		// add key to a list
		final List<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey> snaaKeys = 
			new ArrayList<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey>();
		final eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey snaaKey = 
			mapper.map(key, eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey.class);
		snaaKeys.add(snaaKey);

		// reservation system proxy
		final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);

		// set and return results
		List<eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData> resultList = null;
		try {
			GetReservations reservation = new GetReservations();
			reservation.setFrom(convertDate2XmlGregorianCalendar(start));
			reservation.setTo(convertDate2XmlGregorianCalendar(end));
			resultList = rs.getConfidentialReservations(snaaKeys,reservation);
		} catch (RSExceptionException cause) {
			throw new ReservationException(cause.getMessage());
		}

		// filter to the data related to the authenticated user
		final String authenticatedUsername = snaaKeys.get(0).getUsername();
		for(Iterator<eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData> result = resultList.iterator();
			result.hasNext();) {
			String reservationUsername = result.next().getData().get(0).getUsername();
			if(reservationUsername.equals(authenticatedUsername) == false)
			{
				result.remove();
				LOGGER.info("Removing from list CDR with username " + reservationUsername);
				LOGGER.info("is not equal with " + authenticatedUsername);
			}
		}

		// use Lists.transform to map the results to DTOs
		return new ArrayList<ConfidentialReservationData>(Lists.transform(resultList,
				new Function<eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData, ConfidentialReservationData>() {
			@Override
			public ConfidentialReservationData apply(final eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData r) {

				// use mapper to copy the confidential reservation data
				final ConfidentialReservationData confidentialReservationData 
				= mapper.map(r, ConfidentialReservationData.class);

				// iterate the data list and copy it
				for(eu.wisebed.testbed.api.rs.v1.Data d : r.getData()) {
					final Data data = mapper.map(d, Data.class);
					confidentialReservationData.getData().add(data);
				}

				return confidentialReservationData;
			}
		}));
	}

	/**
	 * Given the ID of a reservation delete the corresponding entry from the
	 * persistent model. Also get the appropriate reservation details from
	 * hibernate's cache as far as the reservations have been already loaded
	 * in the current session.
	 *
	 * @param <code>reservationID</code>, ID of a reservationID.
	 */
	public String deleteReservation(SecretReservationKey key, String rsEndpointUrl)
	throws ReservationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Convenience method for converting {@link Date} objects to {@link XMLGregorianCalendar} objects.
	 *
	 * @param date Date to be converted to {@link XMLGregorianCalendar}
	 * @return Returns {@link XMLGregorianCalendar} object converted from the given {@link Date} object.
	 */
	private XMLGregorianCalendar convertDate2XmlGregorianCalendar(final Date date) {
		XMLGregorianCalendar xmlGregorianCalendar = null;

		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
			LOGGER.debug("convertDate2XmlGregorianCalendar( date: " + date.getTime() + " ) = " + xmlGregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return xmlGregorianCalendar;
	}
}
