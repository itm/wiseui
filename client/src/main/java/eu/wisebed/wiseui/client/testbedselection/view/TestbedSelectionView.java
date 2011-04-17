package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(TestbedSelectionViewImpl.class)
public interface TestbedSelectionView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    AcceptsOneWidget getContentContainer();

    HasEnabled getReloadEnabled();
    
    void setContentSelection(String view);

    public interface Presenter {
    	
    	void setContentSelection(String view);

        void reload();
    }
}
