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
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

import java.util.Date;
import java.util.List;

/**
 * The client side stub for the remote reservation service.
 */
@RemoteServiceRelativePath("reservation.rpc")
public interface ReservationService extends RemoteService {

    ConfidentialReservationData makeReservation(
    		SecretAuthenticationKey key,
            String rsEndpointUrl,
            ReservationDetails data) 
    		throws 
            AuthenticationException, ReservationException;

    List<PublicReservationData> getPublicReservations(
    		String rsEndpointUrl, 
    		Date current, 
    		Range range)
    		throws
    		ReservationException;
    
    List<ConfidentialReservationData> getPrivateReservations(
    		SecretAuthenticationKey key,
    		String rsEndpountUrl,
    		Date current,
    		Range range
    		)
    		throws
    		AuthenticationException, ReservationException;
   
    List<ConfidentialReservationData> getPrivateReservations(
    		SecretAuthenticationKey key,
    		String rsEndpountUrl,
    		Date from,
    		Date to
    		)
    		throws
    		AuthenticationException, ReservationException;
    
    String deleteReservation(
    		SecretReservationKey key, 
    		String rsEndpointUrl)
    		throws 
    		ReservationException;

    enum Range {
        ONE_DAY,
        WEEK,
        MONTH
    }
}
