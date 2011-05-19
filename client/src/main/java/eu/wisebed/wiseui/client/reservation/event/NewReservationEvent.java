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
