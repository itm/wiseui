package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ExperimentViewImpl.class)
public interface ExperimentView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	    
	String getSecretReservationKey();
	
	void setSecretReservationKey(String key);
	
	String getStartDate();
	
	void setStartDate(String date);
	
	String getStopDate();
	
	void setStopDate(String date);
	
	void setExperimentTiming(String timing);
	
	String getExperimentTiming();
	
	void setStatus(String value);
	
	String getStatus();
	
	void setNodeUrns(List<String> nodeUrns);
	
	List<String> getNodeUrns();
	
	public interface Presenter {
	}
}
