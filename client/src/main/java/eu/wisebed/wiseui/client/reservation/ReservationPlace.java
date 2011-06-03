/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.reservation;

import eu.wisebed.wiseui.client.reservation.common.ReservationParams;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class ReservationPlace extends KeyValuePlace {

    public ReservationPlace() {
    	this(ReservationParams.ALL.getValue());
    }
    
    public ReservationPlace(final String view) {
        set(ReservationParams.VIEW.getValue(), view);
    }
   
    public String getView() {
		return Objects2.nullOrToString(get(ReservationParams.VIEW.getValue()));
	}
}
