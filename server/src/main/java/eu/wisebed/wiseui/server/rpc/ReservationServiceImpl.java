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
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.testbed.api.rs.RSServiceHelper;
import eu.wisebed.testbed.api.rs.v1.RS;
import eu.wisebed.testbed.api.rs.v1.RSExceptionException;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
//import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
//import eu.wisebed.testbed.api.wsn.v22.SecretReservationKey;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.SensorDetails;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@Singleton
public class ReservationServiceImpl extends RemoteServiceServlet implements ReservationService {

    private static final long serialVersionUID = -7715272862718944674L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final Mapper mapper;

    @Inject
    public ReservationServiceImpl(final Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves network from the WISEBED web services of a testbed.
     *
     * @return <code>ArrayList</code> with sensor information.
     */
    public List<Node> getNodeList(
            final String sessionManagementEndpointUrl) {

        SessionManagement sessionManagement = WSNServiceHelper.
        	getSessionManagementService(sessionManagementEndpointUrl);
        String serializedWiseML = sessionManagement.getNetwork();

        // TODO FIXME!!!
        ArrayList<Node> nodeList = null;
        //WiseMLInfoExtractor.
        //	getNodeList(serializedWiseML);

        return nodeList;
    }

    /**
     * Make reservation of some nodes in a specific time span
     *
     * @param data    ,an <code>ReservationDetails</code> object
     *                               containing the necessary information to make a reservation.
     */
    public SecretReservationKey makeReservation(SecretAuthenticationKey secretAuthenticationKey,
                                  String rsEndpointUrl, ReservationDetails data)
            throws AuthenticationException, ReservationException,
            ReservationConflictException {
        // TODO: Add functionality while integrating
    	
    	SecretReservationKey key = null;
    	
        return key;
    }

    /**
     * Cancels a reservation on services and deletes the appropriate reservation
     * entry from the persistent model
     *
     * @param reservationId, ID of the cancelled reservation.
     */
    public String cancelReservation(final String sessionId,
                                    final int reservationId) throws ReservationException {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Get all reservations made by a user.
     *
     * @return an <code>ArrayList</code> of <code>ReservationDetails</code>.
     *         objects that are the reservations madey by a user.
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
     *
     * @param <code>userID</code>,      a user ID.
     * @param <code>reservation</code>, the reservation to save into the
     *                                  persistent store.
     */
    public final static void saveReservation(final int userID,
                                             final ReservationDetails reservation) {

        // TODO: Add functionality while integrating
    }

    /**
     * Given a list of the sensor IDs fetch all sensor details with the
     * corresponding IDs.
     *
     * @param sensorIDs, a list of integers as sensor IDs.
     * @return a <code>Set</code> of <code>SensorDetails</code> objects.
     */
    public static final Set<SensorDetails> fetchSensors(
            final ArrayList<Integer> sensorIDs) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Given a reservation ID fetch the corresponding secret reservation keys.
     *
     * @param reservationID, ID of a reservationID.
     * @return a <code>SecretReservationKey</code> object or <code>null</code>
     *         if reservation does not exist
     */
    public static final SecretReservationKey fetchSecretReservationKey(
            final int reservationID) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Given the ID of a reservation delete the corresponding entry from the
     * persistent model. Also get the appropriate reservation details from
     * hibernate's cache as far as the reservations have been already loaded
     * in the current session.
     *
     * @param <code>reservationID</code>, ID of a reservationID.
     */
    private static final void deleteReservation(final int reservationID) {
        // TODO: Add functionality while integrating
    }

    /**
     * Given a user ID fetch all reservations have been made for client with
     * this ID
     *
     * @param <code>userID</code>, a user ID.
     * @return a <code>List</code> containing <code>ReservationDetails</code>
     */
    public final static ArrayList<ReservationDetails>
    fetchAllReservationsForUser(final int userID) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Temporarily populating sensors' table. Just keep a record of the
     * existing sensor infrastructure for development needs.
     */
    private final static void saveSensorInfrastructure(
            final ArrayList<SensorDetails> sensors) {
        // TODO: Add functionality while integrating
    }

    /**
     * Given a a reservaition ID fetch the imageFileNameField of the reservation
     * made by this user.
     *
     * @param <code>reservationID</code>, a reservation ID.
     * @return experiment image filename
     */
    public static String fetchImageFileNameField(
            final int reservationID) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Given a user ID fetch last reservations' ID
     *
     * @param <code>userID</code>, a user ID.
     * @return a reservation ID if exists, else -1.
     */
    public static Integer fetchReservationID(final int userID) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Given a a reservation ID fetch the related node URNs.
     * by this user.
     *
     * @param <code>reservationID</code>, a reservation ID.
     * @return a <code>List</code> of node URNs.
     */
    public static List<String> fetchNodeURNs(final int reservationID) {
        // TODO: Add functionality while integrating
        return null;
    }

    /**
     * Given a a reservation ID fetch the imageFilename of the reservation made
     * by this user.
     *
     * @param reservationID, The ID of the reservation.
     * @return a <code>List</code> of node URNs.
     */
    public static String fetchImageFileName(final int reservationID) {
        // TODO: Add functionality while integrating
        return null;
    }


    @Override
    public List<PublicReservationData> getPublicReservations(final String rsEndpointUrl,
                                                             final Date current,
                                                             final Range range) {
        ifNullOrEmptyArgument(rsEndpointUrl, "Reservation Service Endpoint URL not set!");
        ifNullArgument(current, "current not set!");

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

        LOGGER.info("getPublicReservations( start: " + start + ", end: " + end);

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
        } catch (DatatypeConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return xmlGregorianCalendar;
    }

}