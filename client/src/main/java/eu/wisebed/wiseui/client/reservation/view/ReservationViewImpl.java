package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.reservation.common.Parameters;

@Singleton
public class ReservationViewImpl extends Composite implements ReservationView {

	@UiTemplate("ReservationViewImpl.ui.xml")
    interface ReservationViewImplUiBinder extends
            UiBinder<Widget, ReservationViewImpl> {
    }

	private static ReservationViewImplUiBinder uiBinder = GWT
    	.create(ReservationViewImplUiBinder.class);

	public ReservationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private Presenter presenter;
	
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	@UiField SimplePanel testbedsPanel;
	@UiField SimplePanel parametersPanel;
	@UiField Button reserveButton;
	@UiField ToggleButton newReservationButton;
	@UiField ToggleButton allReservationsButton;
	    
	/**
	 * Initialize reservation view by internally calling initialization 
	 * procedure for each widget the view consists of.
	 */
	public void enableReservationView(){
		reserveButton(true);
		newReservationButton(true);
		allReservationsButton(true);
	}

	public AcceptsOneWidget getParametersPanel(){
		return parametersPanel;
	}
	
	public AcceptsOneWidget getTestbedsPanel(){
		return testbedsPanel;
	}
	
	public void disableReservationView(){
		reserveButton(false);
		newReservationButton(false);
		allReservationsButton(false);
	}

	/**
	 * Toggle activity of reservation button.
	 * @param status True if active.
	 */
	public void reserveButton(boolean status){
		reserveButton.setEnabled(status);
	}
	
	public void newReservationToggleButton(boolean status){
		newReservationButton.setEnabled(status);
	}

	/**
	 * Toggle activity of 'all reservations' button.
	 * @param status True if active.
	 */
	private void newReservationButton(boolean status){
		newReservationButton.setEnabled(status);
	}

	/**
	 * Toggle activity of 'all reservations' button.
	 * @param status True if active.
	 */
	private void allReservationsButton(boolean status){
		allReservationsButton.setEnabled(status);
	}
	
	/**
	 * Tells the presenter to show all reservations
	 * 
	 * @param e ClickEvent
	 */
	@UiHandler("newReservationButton")
	public void onClickNewReservationButton(ClickEvent e) {
		presenter.gotoSubview(Parameters.NEW_VIEW);
	}
	
	/**
	 * Tells the presenter to show all reservations
	 * 
	 * @param e ClickEvent
	 */
	@UiHandler("allReservationsButton")
	public void onClickAllReservationsButton(ClickEvent e) {
		presenter.gotoSubview(Parameters.ALL_VIEW);
	}
	
	@Override
	public void setSubview(final String view) {
		newReservationButton.setDown(Parameters.NEW_VIEW.equals(view));
		allReservationsButton.setDown(Parameters.ALL_VIEW.equals(view));
	}
}