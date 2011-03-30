package eu.wisebed.wiseui.client.administration.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.administration.AdministrationPlace;


/**
 * Administration view definition interface.
 * 
 * @author Malte Legenhausen
 */
@ImplementedBy(AdministrationViewImpl.class)
public interface AdministrationView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    AcceptsOneWidget getConfigurationContainer();
    
    AcceptsOneWidget getContentContainer();
    
    HasEnabled getCreateHasEnabled();
    
    HasEnabled getSaveHasEnabled();
    
    HasEnabled getRemoveHasEnabled();
    
    HasEnabled getCancelHasEnabled();

    /**
     * Presenter for the AdministrationView.
     * 
     * @author Malte Legenhausen
     */
    public interface Presenter {
    	void setPlace(AdministrationPlace place);
    	
    	void create();
    	
    	void save();
    	
    	void remove();
    	
    	void cancel();
    }
}
