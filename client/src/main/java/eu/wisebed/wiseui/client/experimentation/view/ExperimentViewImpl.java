package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ExperimentViewImpl extends Composite implements ExperimentView {
    private static ExperimentPanelViewImplBinder uiBinder = GWT.create(ExperimentPanelViewImplBinder.class);

    @UiTemplate("ExperimentViewImpl.ui.xml")
    interface ExperimentPanelViewImplBinder extends UiBinder<Widget, ExperimentViewImpl> {
    }
    
    private Presenter presenter;
    
    @UiField
    FlexTable buttonTable;
    @UiField 
	Label reservationID;
	@UiField 
	Label startDate;
	@UiField 
	Label stopDate;
	@UiField 
	Label status;
	@UiField 
	Label reservationTime;
	@UiField 
	Label imageFilename;
	@UiField
	Label urnPrefix;
    
	public ExperimentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}
	
	public Presenter getPresenter() {
		return presenter;
	}
	
	@Override
	public String getReservationKey() {
		return reservationID.getText();
	}
	
	@Override
	public void setReservationKey(final String text) {
		reservationID.setText(text);
	}
	
	@Override
	public String getStartDate() {
		return startDate.getText();
	}

	@Override
	public void setStartDate(final String text) {
		startDate.setText(text);
	}

	@Override
	public String getStopDate() {
		return stopDate.getText();
	}

	@Override
	public void setStopDate(final String text) {
		stopDate.setText(text);
	}

	@Override
	public String getStatus() {
		return status.getText();
	}

	@Override
	public void setStatus(final String text) {
		this.status.setText(text);
	}

	@Override
	public String getReservationTime() {
		return reservationTime.getText();
	}

	@Override
	public void setReservationTime(final String text) {
		reservationTime.setText(text);
	}
}
