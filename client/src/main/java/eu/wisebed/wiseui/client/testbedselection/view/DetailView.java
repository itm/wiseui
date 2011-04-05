package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.dto.Capability;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;


/**
 * Interface for a view that shows all details of a testbed.
 * 
 * @author Malte Legenhausen
 */
@ImplementedBy(DetailViewImpl.class)
public interface DetailView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    void setTreeViewModel(TreeViewModel model);
    
    HasText getDescriptionHasText();
    
    HasText getNodeIdHasText();
    
    HasText getNodeTypeHasText();
    
    HasText getNodePositionHasText();
    
    HasText getNodeGatewayHasText();
    
    HasText getNodeProgramDetailsHasText();
    
    HasText getNodeDescriptionHasText();
    
    void showMessage(String message);
    
    HasData<Capability> getCapababilitesList();
    
    HasLoadingIndicator getLoadingIndicator();

    /**
     * The presenter for a DetailView.
     * 
     * @author Malte Legenhausen
     */
    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }	
}