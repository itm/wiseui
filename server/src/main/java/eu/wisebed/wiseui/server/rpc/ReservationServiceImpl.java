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
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import eu.wisebed.testbed.api.rs.v1.Data;
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
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
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
	public SecretReservationKey makeReservation(SecretAuthenticationKey secretAuthenticationKey,
			String rsEndpointUrl, ReservationDetails rsData)
	throws AuthenticationException, ReservationException,
	ReservationConflictException {
		
		// TODO after the joda implementation do we really need the duration 
		//to be sent from client or calculated right here?
		final long durationInSeconds = TimeUnit.SECONDS.toSeconds(rsData.getDuration());
		final DateTime startTime = new DateTime(rsData.getStartTime());
		final DateTime stopTime =  new DateTime(rsData.getStopTime());

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

		// generate confidential reservation data
		eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData reservationData 
		= new eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData();
		Data data = new Data();
		data.setUrnPrefix(snaaKey.getUrnPrefix());
		data.setUsername(secretAuthenticationKey.getUsername());
		reservationData.getData().add(data);
		reservationData.setUserData(secretAuthenticationKey.getUsername());
		reservationData.getNodeURNs().addAll(rsData.getNodes());

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
			secretReservationKeys = rs.makeReservation(APIKeysUtil.copySnaaToRs(secretAuthKeys),
					reservationData
			);
		} catch (AuthorizationExceptionException e) {
			throw new AuthenticationException("Not authorized for reservation");
		} catch (RSExceptionException e) {
			e.printStackTrace();
			throw new ReservationException("RS exception");
		} catch (ReservervationConflictExceptionException e) {
			throw new ReservationConflictException("Reservation conflict");
		}
		data.setSecretReservationKey(secretReservationKeys.get(0).getSecretReservationKey());
		data.setUrnPrefix(secretReservationKeys.get(0).getUrnPrefix());

		LOGGER.debug("Successfully reserved the following nodes: {" + 
				rsData.getNodes() + "}" + "for " + durationInSeconds +
		" seconds");

		return null;
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
	 */
	public List<ConfidentialReservationData> getPrivateReservations(final SecretAuthenticationKey key, 
			final String rsEndpointUrl,final Date current, final Range range) throws ReservationException{

		// check input arguments
		try{
			ifNullArgument(key,"SecretAuthenticationKey is null");
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
			throw new ReservationException("RS Exception");
		}

		return new ArrayList<ConfidentialReservationData>(Lists.transform(resultList,
				new Function<eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData, ConfidentialReservationData>() {
			@Override
			public ConfidentialReservationData apply(final eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData r) {
				final ConfidentialReservationData confidentialReservationData;
				confidentialReservationData = mapper.map(r, ConfidentialReservationData.class);
				return confidentialReservationData;
			}
		}));
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
			throw new ReservationException("RS Exception");
		}

		return new ArrayList<ConfidentialReservationData>(Lists.transform(resultList,
				new Function<eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData, ConfidentialReservationData>() {
			@Override
			public ConfidentialReservationData apply(final eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData r) {
				final ConfidentialReservationData confidentialReservationData;
				confidentialReservationData = mapper.map(r, ConfidentialReservationData.class);
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
