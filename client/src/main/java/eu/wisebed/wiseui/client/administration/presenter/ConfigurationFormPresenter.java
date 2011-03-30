package eu.wisebed.wiseui.client.administration.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView;
import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView.Presenter;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class ConfigurationFormPresenter implements Presenter, ConfigurationSelectedHandler {

	private final EventBus eventBus;
	private final ConfigurationFormView view;
	private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();
	private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	private TestbedConfiguration configuration;
	
	@Inject
	public ConfigurationFormPresenter(final EventBus eventBus, final ConfigurationFormView view) {
		this.eventBus = eventBus;
		this.view = view;
		urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
		view.setUrnPrefixSelectionModel(selectionModel);
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
	}

	@Override
	public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
		configuration = event.getConfiguration();
		view.getNameHasText().setText(configuration.getName());
		view.getTestbedUrlHasText().setText(configuration.getTestbedUrl());
		view.getSnaaEndpointUrlHasText().setText(configuration.getSnaaEndpointUrl());
		view.getRsEndpointUrlHasText().setText(configuration.getRsEndpointUrl());
		view.getSessionManagementEndpointUrlHasText().setText(configuration.getSessionmanagementEndpointUrl());
		urnPrefixProvider.setList(configuration.getUrnPrefixList());
	}

	@Override
	public void add() {
		final String urnPrefix = view.getUrnPrefixHasText().getText();
		urnPrefixProvider.getList().add(urnPrefix);
		urnPrefixProvider.refresh();
	}

	@Override
	public void remove() {
		final String urnPrefix = selectionModel.getSelectedObject();
		urnPrefixProvider.getList().remove(urnPrefix);
		urnPrefixProvider.refresh();
	}

	@Override
	public void setIsFederated(final String federated) {
		GWT.log("Is Federated: " + federated);
	}
}
