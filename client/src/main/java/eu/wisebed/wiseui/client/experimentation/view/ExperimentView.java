package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ExperimentViewImpl.class)
public interface ExperimentView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	    
	String getReservationKey();
	
	void setReservationKey(String key);
	
	String getStartDate();
	
	void setStartDate(String date);
	
	String getStopDate();
	
	void setStopDate(String date);
	
	String getStatus();
	
	void setStatus(String status);
	
	String getReservationTime();
	
	void setReservationTime(String time);
				
	public interface Presenter {
	}

}
