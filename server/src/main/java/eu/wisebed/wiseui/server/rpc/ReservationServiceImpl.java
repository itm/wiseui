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

import org.dozer.Mapper;
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
import eu.wisebed.testbed.api.rs.v1.GetReservations;
import eu.wisebed.testbed.api.rs.v1.RS;
import eu.wisebed.testbed.api.rs.v1.RSExceptionException;
import eu.wisebed.testbed.api.rs.v1.ReservervationConflictExceptionException;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;
import static java.lang.String.*;

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
     * {@inheritDoc}
     */
	@Override
    public List<SecretReservationKey> makeReservation(final String rsEndpointUrl,
                                                      final List<SecretAuthenticationKey> secretAuthenticationKeys,
                                                      final ConfidentialReservationData confidentialReservationData)
            throws AuthenticationException, ReservationException {

        ifNullOrEmptyArgument(rsEndpointUrl, "rsEndpointUrl is null or empty");
        ifNullOrEmptyArgument(secretAuthenticationKeys, "secretAuthenticationKeys is null or empty");
        ifNullArgument(confidentialReservationData, "confidentialReservationData is null");

        LOGGER.info(format("makeReservation( %s, %s, %s )",
                rsEndpointUrl, secretAuthenticationKeys.toString(), confidentialReservationData.toString()));

        // Init remote reservation service with the given endpoint URL
        final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);
        ifNull(rs, "rs is null");

        // Init result of remote call with local object representation
        List<SecretReservationKey> result;

        // Map local transport objects to remote objects
        List<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey> rsSecretAuthenticationKeys
                = new ArrayList<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey>(
                Lists.transform(secretAuthenticationKeys,
                        new Function<SecretAuthenticationKey, eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey>() {
                            @Override
                            public eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey apply(
                                    final SecretAuthenticationKey s) {
                                return mapper.map(s, eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey.class);
                            }
                        }));
        eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData rsConfidentialReservationData =
                mapper.map(confidentialReservationData, eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData.class);

        // Call the remote makeReservation operation and receive result
        List<eu.wisebed.testbed.api.rs.v1.SecretReservationKey> rsResult;
        try {
            rsResult = rs.makeReservation(rsSecretAuthenticationKeys, rsConfidentialReservationData);
        } catch (AuthorizationExceptionException e) {
            throw new AuthenticationException("Not authorized for reservation");
        } catch (RSExceptionException e) {
            e.printStackTrace();
            throw new ReservationException("RS exception");
        } catch (ReservervationConflictExceptionException e) {
            throw new ReservationException("Reservation conflict");
        }
        ifNull(rsResult, "rsReservation is null");

        // Map back remote result to local transport objects
        result = new ArrayList<SecretReservationKey>(
                Lists.transform(rsResult,
                        new Function<eu.wisebed.testbed.api.rs.v1.SecretReservationKey, SecretReservationKey>() {
                            @Override
                            public SecretReservationKey apply(
                                    final eu.wisebed.testbed.api.rs.v1.SecretReservationKey srk) {
                                return mapper.map(srk, SecretReservationKey.class);
                            }
                        }));
        ifNull(result, "result is null");

        return result;
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public List<PublicReservationData> getPublicReservations(final String rsEndpointUrl,
                                                             final Date current,
                                                             final Range range) throws ReservationException {

        ifNullOrEmptyArgument(rsEndpointUrl, "rsEndpointUrl is null");
        ifNullArgument(current, "current is null");

        LOGGER.info(format("getPublicReservations( %s, %s, %s )",
                rsEndpointUrl, current.toString(), range.toString()));

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

        LOGGER.debug(format("getPublicReservations#calculatedStartDate=%s, calculatedEndDate=%s", start, end));

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
     * {@inheritDoc}
     */
	@Override
	public List<ConfidentialReservationData> getPrivateReservations(
			final String rsEndpointUrl,
			List<SecretAuthenticationKey> secretAuthenticationKeys,
			final Date current,
			final Range range) throws AuthenticationException,ReservationException{

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
		return getPrivateReservations(rsEndpointUrl,secretAuthenticationKeys, from, to);
	}
	
	
    /**
     * {@inheritDoc}
     */
	@Override
	public List<ConfidentialReservationData> getPrivateReservations(
			final String rsEndpointUrl,
			final List<SecretAuthenticationKey> secretAuthenticationKeys,
			final Date from,
			final Date to) throws AuthenticationException, ReservationException {

		// check input arguments
		try{
			ifNullArgument(secretAuthenticationKeys,"SecretAuthenticationKey is null");
			ifNullOrEmptyArgument(rsEndpointUrl, "Reservation Service Endpoint URL not set");
			ifNullArgument(from, "From date is not set");
			ifNullArgument(to,"To date is not set");
		}catch(RuntimeException cause){
			throw new ReservationException(cause.getMessage());
		}

		// start and end Dates are provided by client
		final Date start = from;
		final Date end = to;
        	LOGGER.debug(format("getPublicReservations#calculatedStartDate=%s, calculatedEndDate=%s", start, end));

		// add key to a list
		final List<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey> snaaKeys = 
			new ArrayList<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey>();
		final eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey snaaKey = 
			mapper.map(secretAuthenticationKeys.get(0),
					eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey.class);
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
     * {@inheritDoc}
     */
	@Override
	public String deleteReservation(String rsEndpountUrl,
			List<SecretReservationKey> secretReservationKeys,
			String rsEndpointUrl) throws ReservationException {
		// TODO Auto-generated method stub
		return null;
	}


    private XMLGregorianCalendar convertDate2XmlGregorianCalendar(
    		final Date date) {
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
