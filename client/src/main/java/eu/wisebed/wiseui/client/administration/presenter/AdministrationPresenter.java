package eu.wisebed.wiseui.client.administration.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.administration.event.CancelConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.RemoveConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.SaveConfigurationEvent;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.AdministrationView.Presenter;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class AdministrationPresenter implements Presenter, ConfigurationSelectedHandler {

	private final EventBus eventBus;
	private final AdministrationView view;
	private TestbedConfiguration configuration;
	
	@Inject
	public AdministrationPresenter(final EventBus eventBus, final AdministrationView view) {
		this.eventBus = eventBus;
		this.view = view;
		bind();
	}
	
	private void bind() {
		
	}
	
	@Override
	public void setPlace(final AdministrationPlace place) {
		
	}

	@Override
	public void create() {
		eventBus.fireEvent(new CreateConfigurationEvent());
	}

	@Override
	public void save() {
		eventBus.fireEvent(new SaveConfigurationEvent());
	}

	@Override
	public void remove() {
		eventBus.fireEvent(new RemoveConfigurationEvent(configuration));
	}

	@Override
	public void cancel() {
		eventBus.fireEvent(new CancelConfigurationEvent());
	}

	@Override
	public void onTestbedConfigurationSelected(ConfigurationSelectedEvent event) {
		configuration = event.getConfiguration();
	}

}
