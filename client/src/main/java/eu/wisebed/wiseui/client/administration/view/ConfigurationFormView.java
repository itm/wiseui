package eu.wisebed.wiseui.client.administration.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;

/**
 * View interface for the testbed configuration form.
 * 
 * @author Malte Legenhausen
 */
@ImplementedBy(ConfigurationFormViewImpl.class)
public interface ConfigurationFormView extends IsWidget {

	HasText getNameHasText();
	
	HasText getTestbedUrlHasText();
	
	HasText getSnaaEndpointUrlHasText();
	
	HasText getRsEndpointUrlHasText();
	
	HasText getSessionManagementEndpointUrlHasText();
	
	HasText getUrnPrefixHasText();
	
	HasData<String> getUrnPrefixHasData();
	
	HasEnabled getUrnPrefixRemoveHasEnabled();
	
	void setPresenter(Presenter presenter);
	
	void setUrnPrefixSelectionModel(SelectionModel<String> selectionModel);
	
	void setFederatedItems(List<String> items);
	
	void setFederatedSelectedIndex(int index);
	
	int getFederatedSelectedIndex();
	
	HasText getInfoHasText();
	
	void setInfoVisibility(boolean visibility);
	
	/**
	 * Presenter for the testbed configuration form view.
	 * 
	 * @author Malte Legenhausen
	 */
	public interface Presenter {
		
		void add();
		
		void remove();
	}
}
