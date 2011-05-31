/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.util;

import java.util.HashMap;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

@Singleton
public class ReservationManager {

	private final HashMap<Appointment, SecretReservationKey> privateReservationsMap = 
		new HashMap<Appointment, SecretReservationKey>();

	/**
	 * Load all authentication keys from the cookie.
	 */
	public void init() {
		GWT.log("Init Reservation Manager");
	}

	public void addReservation(final Appointment reservation, final SecretReservationKey rsKey){
		privateReservationsMap.put(reservation, rsKey);
	}
	
	public void removeReservation(final Appointment reservation){
		privateReservationsMap.remove(reservation);
	}
	
	/**
	 * Return the private reservations map
	 * @return Appointment-to-String map
	 */
	public HashMap<Appointment, SecretReservationKey> getPrivateReservationsMap(){
		return privateReservationsMap;
	}
}
