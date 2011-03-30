package eu.wisebed.wiseui.client.administration.gin;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.administration.AdministrationActivity;
import eu.wisebed.wiseui.client.administration.presenter.AdministrationPresenter;
import eu.wisebed.wiseui.client.administration.presenter.ConfigurationFormPresenter;
import eu.wisebed.wiseui.client.administration.presenter.ConfigurationPresenter;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView;


/**
 * Injector for the administration area of WiseUi.
 * 
 * @author Malte Legenhausen
 */
public interface AdministrationGinjector extends Ginjector {

    AdministrationView getAdministrationView();

    AdministrationActivity getAdministrationActivity();
    
	AdministrationPresenter getAdministrationPresenter();
	
	ConfigurationPresenter getAdministrationConfigurationPresenter();
	
	ConfigurationFormView getConfigurationFormView();
	
	ConfigurationFormPresenter getConfigurationFormPresenter();
}
