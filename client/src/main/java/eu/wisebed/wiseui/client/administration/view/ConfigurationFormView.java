package eu.wisebed.wiseui.client.administration.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;

@ImplementedBy(ConfigurationFormViewImpl.class)
public interface ConfigurationFormView extends IsWidget {

	HasText getNameHasText();
	
	HasText getTestbedUrlHasText();
	
	HasText getSnaaEndpointUrlHasText();
	
	HasText getRsEndpointUrlHasText();
	
	HasText getSessionManagementEndpointUrlHasText();
	
	HasText getUrnPrefixHasText();
	
	HasData<String> getUrnPrefixHasData();
	
	void setPresenter(Presenter presenter);
	
	void setUrnPrefixSelectionModel(SelectionModel<String> selectionModel);
	
	public interface Presenter {
		void setIsFederated(String federated);
		
		void add();
		
		void remove();
	}
}
