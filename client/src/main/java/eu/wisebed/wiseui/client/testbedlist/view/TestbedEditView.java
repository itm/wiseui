package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;

@ImplementedBy(TestbedEditViewImpl.class)
public interface TestbedEditView extends IsWidget {

	HasText getNameHasText();
	
	HasText getTestbedUrlHasText();
	
	HasText getSnaaEndpointUrlHasText();
	
	HasText getRsEndpointUrlHasText();
	
	HasText getSessionManagementEndpointUrlHasText();
	
	HasText getUrnPrefixHasText();
	
	HasData<String> getUrnPrefixHasData();
	
	HasEnabled getUrnPrefixRemoveHasEnabled();
	
	void setUrnPrefixSelectionModel(SelectionModel<String> selectionModel);
	
	void setFederatedItems(List<String> items);
	
	void setFederatedSelectedIndex(int index);
	
	int getFederatedSelectedIndex();
	
	void setPresenter(Presenter presenter);
	
	void show(String title);
	
    void hide();
    
	boolean validate();
	
	public interface Presenter {
		
		void submit();
		
		void cancel();
		
		void add();
		
		void remove();
	}
}
