package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ExperimentViewImpl.class)
public interface ExperimentView extends IsWidget {
	
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
	
	String getUrnPrefix();
	
	void setUrnPrefix(String urnPrefix);
	
	void setImageFilename(String filename);
	
	void showHideNodeOutput();

	void fillNodeTabPanel(List<String> urns);
	
	void printExperimentMessage(String sourceNodeUrn,String level, String data, String timeStamp);
	
	void printRequestStatus(String nodeUrn, String msg, String value);
	
	public interface Presenter {
        void buttonClicked(String button);
	}

}
