package eu.wisebed.wiseui.widgets.experimentpanel;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface ExperimentPanelView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	
    void setButtons(String... buttons);
    
	String getReservationID();
	
	void setReservationID(String ID);
	
	String getStartDate();
	
	void setStartDate(String date);
	
	String getStopDate();
	
	void setStopDate(String date);
	
	String getStatus();
	
	void setStatus(String status);
	
	String getReservationTime();
	
	void setReservationTime(String time);
	
	String getImageFilename();
	
	void setImageFilename(String filename);
	
	void fillNodeTabPanel(List<String> urns);
	
	public interface Presenter {
        void buttonClicked(String button);
	}
}
