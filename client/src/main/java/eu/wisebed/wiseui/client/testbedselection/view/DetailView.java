package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.TreeViewModel;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.wiseml.Node;

public interface DetailView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    void setTreeViewModel(TreeViewModel model);
    
    void setNodeDetails(Node node);

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }

	
}