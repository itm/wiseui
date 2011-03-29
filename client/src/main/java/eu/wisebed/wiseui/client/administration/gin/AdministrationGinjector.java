package eu.wisebed.wiseui.client.administration.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.administration.AdministrationActivity;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;


/**
 * Injector for the administration area of WiseUi.
 * 
 * @author Malte Legenhausen
 */
public interface AdministrationGinjector extends Ginjector {

    AdministrationView getAdministrationView();

    AdministrationActivity getAdministrationActivity();
}
