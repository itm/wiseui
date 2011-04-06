package eu.wisebed.wiseui.client.reservation.view;

import java.util.Date;
import java.util.List;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.PublicReservationData;

@Singleton
public class PublicReservationsViewImpl extends Composite implements PublicReservationsView{
	
	private Presenter presenter;
	private final int WEEK_DAYS = 7;
	
	@UiTemplate("PublicReservationsViewImpl.ui.xml")
    interface PublicReservationsViewImplUiBinder extends
            UiBinder<Widget, PublicReservationsViewImpl> {
    }

	private static PublicReservationsViewImplUiBinder uiBinder = GWT
    	.create(PublicReservationsViewImplUiBinder.class);

	public void setPresenter(Presenter presenter){
		this.presenter = presenter;
	}
	
	@UiField Calendar calendar;
	
	public PublicReservationsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		initCalendar();
	}

	private void initCalendar(){
		calendar.setDate(new Date());
		calendar.setDays(WEEK_DAYS);
	}
	
	public Date getFrom(){
		return calendar.getDate();
	}
	
	public Date getTo(){
		Date to = new Date();  
		to.setTime(System.currentTimeMillis() + ((long)WEEK_DAYS) *24*60*60*1000);  
		return to;
	}
	
	/* 
	 *  Renders a list of public reservations received.
	 */
	public void renderPublicReservations(final List<PublicReservationData> publicReservations){
		// FIXME (or not?): Clear calendar to avoid redrawing same reservations
		calendar.clearAppointments();
		for (PublicReservationData r: publicReservations){
			// TODO: Work on a more descriptive representation of reservations
			Appointment reservation = new Appointment();
			reservation.setStart(r.getFrom());
			reservation.setEnd(r.getTo());
			reservation.setLocation(r.getNodeURNs().get(0));
			calendar.addAppointment(reservation);
		}
	}
}