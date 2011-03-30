package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;


public class ExperimentViewImpl extends Composite implements ExperimentView, ClickHandler {
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

	@UiField 
	TabLayoutPanel nodeTabPanel;
    
	public ExperimentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public String getReservationID() {
		return reservationID.getText();
	}
	
	@Override
	public void setReservationID(final String text) {
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
	
	@Override
	public String getImageFilename() {
		return imageFilename.getText();
	}

	@Override
	public void setImageFilename(final String text) {
		imageFilename.setText(text);
	}
	
	@Override
	public String getUrnPrefix() {
		return urnPrefix.getText();
	}
	@Override
	public void setUrnPrefix(final String text) {
		urnPrefix.setText(text);
	}
	
	@Override
	public void fillNodeTabPanel(final List<String> urns) {
		for(String s: urns) {
			nodeTabPanel.add(new TextArea(),s);
		}
	}
		
    @Override
    public void onClick(final ClickEvent event) {
        final Button button = (Button) event.getSource();
        presenter.buttonClicked(button.getText());
    }
    
    @Override
    public void setButtons(final String... buttons) {
        buttonTable.clear();
        int i = 0;
        for (String label : buttons) {
            final Button button = new Button(label, this);
            button.setWidth("100px");
            buttonTable.setWidget(0, i++, button);
        }
    }

	@Override
	public void showHideNodeOutput() {
		boolean isVisible = this.nodeTabPanel.isVisible();
		this.nodeTabPanel.setVisible(!isVisible);
	}
}
