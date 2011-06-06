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
package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

@Singleton
public class ReservationManager {

	private Map<Appointment, PublicReservationData> publicReservations
            = new HashMap<Appointment, PublicReservationData>();

	private Map<Appointment, ConfidentialReservationData> confidentialReservations
            = new HashMap<Appointment, ConfidentialReservationData>();

    public ReservationManager() {
    }

    public void init() {

    }

    public Map<Appointment, PublicReservationData> getPublicReservations() {
        return publicReservations;
    }

    public Map<Appointment, ConfidentialReservationData> getConfidentialReservations() {
        return confidentialReservations;
    }

    public List<PublicReservationData> getAllReservations() {
        final List<PublicReservationData> result = new ArrayList<PublicReservationData>();

        for (PublicReservationData publicReservationData : publicReservations.values()) {
            result.add(publicReservationData);
        }

        return result;
    }
}
