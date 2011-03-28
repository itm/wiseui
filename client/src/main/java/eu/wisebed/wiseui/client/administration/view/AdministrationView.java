package eu.wisebed.wiseui.client.administration.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;


/**
 * Administration view definition interface.
 * 
 * @author Malte Legenhausen
 */
@ImplementedBy(AdministrationViewImpl.class)
public interface AdministrationView extends IsWidget {

    void setPresenter(Presenter presenter);

    /**
     * Presenter for the AdministrationView.
     * 
     * @author Malte Legenhausen
     */
    public interface Presenter {

    }
}
