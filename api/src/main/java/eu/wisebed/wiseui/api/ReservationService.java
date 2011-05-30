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
package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

import java.util.Date;
import java.util.List;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {

	/**
	 * Make a reservation for the given time span and with the given credentials.
	 *
	 * @param rsEndpointUrl reservation endpoint URL
	 * @param secretAuthenticationKeys List of {@link SecretAuthenticationKey}s
	 * @param confidentialReservationData {@link ConfidentialReservationData} to perform a reservation
	 * @return List of {@link SecretReservationKey}s
	 * @throws AuthenticationException
	 * @throws ReservationException
	 */
	List<SecretReservationKey> makeReservation(
			String rsEndpointUrl,
			List<SecretAuthenticationKey> secretAuthenticationKeys,
			ConfidentialReservationData confidentialReservationData)
			throws
			AuthenticationException, ReservationException;
	/**
	 * Get public reservations of all the testbed's users.
	 *
	 * @param rsEndpointUrl reservation endpoint URL
	 * @param current Pivot date for applying the range
	 * @param range Date range (e.g. day, week, month)
	 * @return List of public reservation data
	 * @throws ReservationException
	 */
	List<PublicReservationData> getPublicReservations(
			String rsEndpointUrl,
			Date current,
			Range range) throws ReservationException;

	/**
	 * Get private reservations of a the testbed user.
	 *
	 * @param rsEndpointUrl reservation endpoint URL
	 * @param secretAuthenticationKeys List of {@link SecretAuthenticationKey}s
	 * @param current Pivot date for applying the range
	 * @param range Date range (e.g. day, week, month)
	 * @return List of confidential reservation data
	 * @throws AuthenticationException
	 * @throws ReservationException
	 */
	List<ConfidentialReservationData> getPrivateReservations(
			String rsEndpountUrl,
			List<SecretAuthenticationKey> secretAuthenticationKeys,
			Date current,
			Range range)
			throws
			AuthenticationException, ReservationException;
	
	/**
	 * Get private reservations of a the testbed user.
	 *
	 * @param rsEndpointUrl reservation endpoint URL
	 * @param secretAuthenticationKeys List of {@link SecretAuthenticationKey}s
	 * @param current Pivot date for applying the range
	 * @param from Date
	 * @param to Date
	 * @return List of confidential reservation data
	 * @throws AuthenticationException
	 * @throws ReservationException
	 */
	List<ConfidentialReservationData> getPrivateReservations(
			String rsEndpountUrl,
			List<SecretAuthenticationKey> secretAuthenticationKeys,
			Date from,
			Date to)
			throws
			AuthenticationException, ReservationException;

	/**
	 * Delete a reservation
	 * @param secretReservationKeys List of {@link SecretReservationKeys}s
	 * @param rsEndpointUrl reservation endpoint URL
	 * @return
	 * @throws ReservationException
	 */
	String deleteReservation(
			String rsEndpointUrl,
			List<SecretAuthenticationKey> secretAuthenticationKeys,
			List<SecretReservationKey> secretReservationKeys)
	throws
	ReservationException;

	enum Range {

		ONE_DAY,
		WEEK,
		MONTH
	}
}
