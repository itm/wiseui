package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ExperimentViewImpl extends Composite implements ExperimentView {
    private static ExperimentPanelViewImplBinder uiBinder = GWT.create(ExperimentPanelViewImplBinder.class);

    @UiTemplate("ExperimentViewImpl.ui.xml")
    interface ExperimentPanelViewImplBinder extends UiBinder<Widget, ExperimentViewImpl> {
    }
    
    private Presenter presenter;
    
    @UiField 
	Label secretReservationKey;
	@UiField 
	Label startDate;
	@UiField 
	Label stopDate;
	@UiField
	Label experimentTiming;
	@UiField
	Label status; 
	
	
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
	public String getSecretReservationKey() {
		return secretReservationKey.getText();
	}
	
	@Override
	public void setSecretReservationKey(final String text) {
		secretReservationKey.setText(text);
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
	public void setExperimentTiming(String timing) {
		experimentTiming.setText(timing);
	}

	@Override
	public String getExperimentTiming() {
		return experimentTiming.getText();
	}

	@Override
	public void setStatus(String value) {
		status.setText(value);
	}

	@Override
	public String getStatus() {
		return status.getText();
	}

	@Override
	public void setNodeUrns(List<String> nodeUrns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNodeUrns() {
		// TODO Auto-generated method stub
		return null;
	}
}
