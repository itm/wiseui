package eu.wisebed.wiseui.client.administration.presenter;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.administration.event.CancelConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.CancelConfigurationEvent.CancelConfigurationHandler;
import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent.CreateConfigurationHandler;
import eu.wisebed.wiseui.client.administration.event.SaveConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.SaveConfigurationEvent.SaveConfigurationHandler;
import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView;
import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView.Presenter;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class ConfigurationFormPresenter implements Presenter, ConfigurationSelectedHandler, SaveConfigurationHandler, CancelConfigurationHandler, CreateConfigurationHandler {

	private final EventBus eventBus;
	private final ConfigurationFormView view;
	private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();
	private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	private TestbedConfiguration configuration = new TestbedConfiguration();
	
	@Inject
	public ConfigurationFormPresenter(final EventBus eventBus, final ConfigurationFormView view) {
		this.eventBus = eventBus;
		this.view = view;
		urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
		view.setUrnPrefixSelectionModel(selectionModel);
		view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
		loadConfigurationToView();
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
		eventBus.addHandler(SaveConfigurationEvent.TYPE, this);
		eventBus.addHandler(CancelConfigurationEvent.TYPE, this);
		eventBus.addHandler(CreateConfigurationEvent.TYPE, this);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
			}
		});
	}
	
	private void showNewConfigurationInfo() {
		view.getInfoHasText().setText("You are about to create a new Testbed configuration. Fill out the form and press the save button.");
		view.setInfoVisibility(true);
	}
	
	private void loadConfigurationToView() {
		view.getNameHasText().setText(configuration.getName());
		view.getTestbedUrlHasText().setText(configuration.getTestbedUrl());
		view.getSnaaEndpointUrlHasText().setText(configuration.getSnaaEndpointUrl());
		view.getRsEndpointUrlHasText().setText(configuration.getRsEndpointUrl());
		view.getSessionManagementEndpointUrlHasText().setText(configuration.getSessionmanagementEndpointUrl());
		view.setFederatedSelectedIndex(configuration.isFederated() ? 0 : 1);
		urnPrefixProvider.setList(new ArrayList<String>(configuration.getUrnPrefixList()));
	}
	
	public void setPlace(final AdministrationPlace place) {
		if (place.getSelection() == null) {
			showNewConfigurationInfo();
		}
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
		view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
	}
	
	@Override
	public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
		configuration = event.getConfiguration();
		loadConfigurationToView();
		view.setInfoVisibility(false);
	}

	@Override
	public void onSaveConfiguration(final SaveConfigurationEvent event) {
		configuration.setName(view.getNameHasText().getText());
		configuration.setTestbedUrl(view.getTestbedUrlHasText().getText());
		configuration.setSnaaEndpointUrl(view.getSnaaEndpointUrlHasText().getText());
		configuration.setRsEndpointUrl(view.getRsEndpointUrlHasText().getText());
		configuration.setSessionmanagementEndpointUrl(view.getSessionManagementEndpointUrlHasText().getText());
		configuration.setFederated(view.getFederatedSelectedIndex() != 0);
		configuration.setUrnPrefixList(urnPrefixProvider.getList());
		
		GWT.log("Saving testbed configuration: " + configuration.getName());
		//TODO: Call remote service for saving testbed.
	}

	@Override
	public void onCancelConfiguration(final CancelConfigurationEvent event) {
		loadConfigurationToView();
	}

	@Override
	public void onCreateConfiguration(CreateConfigurationEvent event) {
		configuration = new TestbedConfiguration();
		loadConfigurationToView();
		showNewConfigurationInfo();
	}
}
