/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.rpc;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.wisebed.testbed.api.rs.RSServiceHelper;
import eu.wisebed.api.rs.AuthorizationExceptionException;
import eu.wisebed.api.rs.GetReservations;
import eu.wisebed.api.rs.RS;
import eu.wisebed.api.rs.RSExceptionException;
import eu.wisebed.api.rs.ReservervationConflictExceptionException;
import eu.wisebed.api.rs.ReservervationNotFoundExceptionException;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;
import static java.lang.String.format;

@Singleton
public class ReservationServiceImpl extends RemoteServiceServlet implements ReservationService {

    private static final long serialVersionUID = -7715272862718944674L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private static final String FROM_KEY = "from";

    private static final String TO_KEY = "to";

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
                rsEndpointUrl, secretAuthenticationKeys, confidentialReservationData));

        // Init remote reservation service with the given endpoint URL
        final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);
        ifNull(rs, "rs is null");

        // Init result of remote call with local object representation
        List<SecretReservationKey> result;

        // Map local transport objects to remote objects
        List<eu.wisebed.api.rs.SecretAuthenticationKey> rsSecretAuthenticationKeys
                = new ArrayList<eu.wisebed.api.rs.SecretAuthenticationKey>(
                Lists.transform(secretAuthenticationKeys,
                        new Function<SecretAuthenticationKey, eu.wisebed.api.rs.SecretAuthenticationKey>() {
                            @Override
                            public eu.wisebed.api.rs.SecretAuthenticationKey apply(
                                    final SecretAuthenticationKey s) {
                                return mapper.map(s, eu.wisebed.api.rs.SecretAuthenticationKey.class);
                            }
                        }));
        eu.wisebed.api.rs.ConfidentialReservationData rsConfidentialReservationData =
                mapper.map(confidentialReservationData, eu.wisebed.api.rs.ConfidentialReservationData.class);

        // Call the remote makeReservation operation and receive result
        List<eu.wisebed.api.rs.SecretReservationKey> rsResult;
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
                        new Function<eu.wisebed.api.rs.SecretReservationKey, SecretReservationKey>() {
                            @Override
                            public SecretReservationKey apply(
                                    final eu.wisebed.api.rs.SecretReservationKey srk) {
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
                                                             final Date pivotDate,
                                                             final Range dateRange) throws ReservationException {

        ifNullOrEmptyArgument(rsEndpointUrl, "rsEndpointUrl is null");
        ifNullArgument(pivotDate, "pivotDate is null");

        LOGGER.info(format("getPublicReservations( %s, %s, %s )",
                rsEndpointUrl, pivotDate, dateRange));

        /* Convert from dateRange to date to avoid client-site date calculation */
        Map<String, Date> fromAndTo = convertRange2Date(pivotDate, dateRange);
        final Date start = fromAndTo.get(FROM_KEY);
        final Date end = fromAndTo.get(TO_KEY);

        LOGGER.debug(format("getPublicReservations#calculatedStartDate=%s, calculatedEndDate=%s", start, end));

        final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);
        ifNull(rs, "rs is null!");

        List<eu.wisebed.api.rs.PublicReservationData> resultList = null;
        try {
            resultList = rs.getReservations(
                    convertDate2XmlGregorianCalendar(start),
                    convertDate2XmlGregorianCalendar(end));
        } catch (RSExceptionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<PublicReservationData>(Lists.transform(resultList,
                new Function<eu.wisebed.api.rs.PublicReservationData, PublicReservationData>() {
                    @Override
                    public PublicReservationData apply(final eu.wisebed.api.rs.PublicReservationData r) {
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
            final List<SecretAuthenticationKey> secretAuthenticationKeys,
            final Date pivotDate,
            final Range dateRange) throws AuthenticationException, ReservationException {

        ifNullOrEmptyArgument(rsEndpointUrl, "rsEndpointUrl is null");
        ifNullArgument(secretAuthenticationKeys, "secretAuthenticationKeys is null");
        ifNullArgument(pivotDate, "pivotDate is null");

        LOGGER.info(format("getPrivateReservations( %s, %s, %s )", rsEndpointUrl, pivotDate.toString(), dateRange.toString()));

        /* Convert from dateRange to date to avoid client-site date calculation */
        Map<String, Date> fromAndTo = convertRange2Date(pivotDate, dateRange);
        final Date from = fromAndTo.get(FROM_KEY);
        final Date to = fromAndTo.get(TO_KEY);

        // call getPrivateReservations with distinct from, to date objects
        return getPrivateReservations(rsEndpointUrl, secretAuthenticationKeys, from, to);
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

        ifNullArgument(secretAuthenticationKeys, "SecretAuthenticationKey is null");
        ifNullOrEmptyArgument(rsEndpointUrl, "Reservation Service Endpoint URL not set");
        ifNullArgument(from, "From date is not set");
        ifNullArgument(to, "To date is not set");

        // start and end Dates are provided by client
        LOGGER.debug(format("getPrivateReservations#calculatedStartDate=%s, calculatedEndDate=%s", from, to));

        // add key to a list
        final List<eu.wisebed.api.rs.SecretAuthenticationKey> snaaKeys =
                new ArrayList<eu.wisebed.api.rs.SecretAuthenticationKey>();
        final eu.wisebed.api.rs.SecretAuthenticationKey snaaKey =
                mapper.map(secretAuthenticationKeys.get(0),
                        eu.wisebed.api.rs.SecretAuthenticationKey.class);
        snaaKeys.add(snaaKey);

        // reservation system proxy
        final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);

        // set and return results
        List<eu.wisebed.api.rs.ConfidentialReservationData> resultList;
        try {
            GetReservations reservation = new GetReservations();
            reservation.setFrom(convertDate2XmlGregorianCalendar(from));
            reservation.setTo(convertDate2XmlGregorianCalendar(to));
            resultList = rs.getConfidentialReservations(snaaKeys, reservation);
        } catch (final RSExceptionException cause) {
            throw new ReservationException(cause.getMessage());
        }

        // filter to the data related to the authenticated user
        final String authenticatedUsername = snaaKeys.get(0).getUsername();
        List<eu.wisebed.api.rs.ConfidentialReservationData> filteredResultList
                = new ArrayList<eu.wisebed.api.rs.ConfidentialReservationData>();
        for (eu.wisebed.api.rs.ConfidentialReservationData confidentialReservationData : resultList) {
            String reservationUsername = confidentialReservationData.getData().get(0).getUsername();
            if (reservationUsername != null && reservationUsername.equals(authenticatedUsername)) {
                filteredResultList.add(confidentialReservationData);
            }
        }

        // use Lists.transform to map the results to DTOs
        return new ArrayList<ConfidentialReservationData>(Lists.transform(filteredResultList,
                new Function<eu.wisebed.api.rs.ConfidentialReservationData, ConfidentialReservationData>() {
                    @Override
                    public ConfidentialReservationData apply(
                            final eu.wisebed.api.rs.ConfidentialReservationData r) {
                        return mapper.map(r, ConfidentialReservationData.class);
                    }
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReservation(final String rsEndpointUrl,
                                  final List<SecretAuthenticationKey> secretAuthenticationKeys,
                                  final List<SecretReservationKey> secretReservationKeys)
            throws ReservationException {

        ifNullOrEmptyArgument(rsEndpointUrl, "rsEndpointUrl is null");
        ifNullArgument(secretAuthenticationKeys, "secretAuthenticationKeys is null");
        ifNullArgument(secretReservationKeys, "secretReservationKeys is null");

        final RS rs = RSServiceHelper.getRSService(rsEndpointUrl);
        final List<eu.wisebed.api.rs.SecretAuthenticationKey> snaaKeys =
                new ArrayList<eu.wisebed.api.rs.SecretAuthenticationKey>();
        snaaKeys.add(mapper.map(secretAuthenticationKeys.get(0),
                eu.wisebed.api.rs.SecretAuthenticationKey.class));

        final List<eu.wisebed.api.rs.SecretReservationKey> rsKeys =
                new ArrayList<eu.wisebed.api.rs.SecretReservationKey>();
        rsKeys.add(mapper.map(secretReservationKeys.get(0),
                eu.wisebed.api.rs.SecretReservationKey.class));

        try {
            rs.deleteReservation(snaaKeys, rsKeys);
        } catch (final RSExceptionException cause) {
            throw new ReservationException(cause.getMessage());
        } catch (ReservervationNotFoundExceptionException cause) {
            throw new ReservationException(cause.getMessage());
        }
    }


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

    private Map<String, Date> convertRange2Date(final Date pivotDate, final Range dateRange) {
        final int ONE_DAY = 1;
        final int WEEK = 7;
        final int RESULT_SIZE = 2;

        final Map<String, Date> fromAndTo = new HashMap<String, Date>(RESULT_SIZE);

        Date from = null;
        Date to = null;
        final Calendar calendar = Calendar.getInstance();

        if (Range.ONE_DAY == dateRange) {
            calendar.setTime(pivotDate);
            calendar.add(Calendar.DAY_OF_MONTH, ONE_DAY);
            from = pivotDate;
            to = calendar.getTime();
        } else if (Range.WEEK == dateRange) {
            calendar.setTime(pivotDate);
            calendar.add(Calendar.DAY_OF_MONTH, WEEK);
            from = pivotDate;
            to = calendar.getTime();
        } else if (Range.MONTH == dateRange) {
            calendar.setTime(pivotDate);
            final int firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
            from = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
            to = calendar.getTime();
        }

        // If one of the dates would be Null, this is considered as an serious error,
        // thus we throw an RuntimeException via the ifNull-method.
        // We don't want to add Null to the result map.
        ifNull(from, "from is null");
        ifNull(to, "to is null");

        fromAndTo.put(FROM_KEY, from);
        fromAndTo.put(TO_KEY, to);

        return fromAndTo;
    }
}
