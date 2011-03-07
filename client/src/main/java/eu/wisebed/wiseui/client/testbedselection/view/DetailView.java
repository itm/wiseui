package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.TreeViewModel;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.wiseml.Capability;

public interface DetailView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    void setTreeViewModel(TreeViewModel model);
    
    HasText getNodeIdHasText();
    
    HasText getNodePositionHasText();
    
    HasText getNodeGatewayHasText();
    
    HasText getNodeProgramDetailsHasText();
    
    HasText getNodeDescriptionHasText();
    
    void showMessage(String message);
    
    HasData<Capability> getCapababilitesList();

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }

	
}