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
package eu.wisebed.wiseui.client.reservation.event;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.gwt.event.shared.GwtEvent;

public class NewReservationEvent extends GwtEvent<NewReservationEventHandler>{
	public static Type<NewReservationEventHandler> TYPE = 
		new Type<NewReservationEventHandler>();

	@Override
	public Type<NewReservationEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	private final Appointment reservation;
	
	public NewReservationEvent(final Appointment reservation){
		this.reservation = reservation;
	}
	
	public Appointment getReservation(){
		return this.reservation;
	}

	@Override
	protected void dispatch(NewReservationEventHandler handler){
		handler.onNewReservation(this);
	}
}
