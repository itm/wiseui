package eu.wisebed.wiseui.client.testbedlist.presenter;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedlist.event.EditTestbedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.RefreshTestbedListEvent;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

public class TestbedEditPresenter implements Presenter, EditTestbedEvent.Handler {

	private static final String DEFAULT_NEW_TITLE = "New Testbed Configuration";
	
	private final EventBusManager eventBus;
	
	private final TestbedEditView view;
	
	private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();
	
	private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	
	private TestbedConfiguration configuration;
	
	private String title = DEFAULT_NEW_TITLE;
	
	@Inject
	public TestbedEditPresenter(final EventBus eventBus, final TestbedEditView view) {
		this.eventBus = new EventBusManager(eventBus);
		this.view = view;
		
		urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
		view.setFederatedItems(Arrays.asList("Yes", "No"));
		view.setUrnPrefixSelectionModel(selectionModel);
		view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(EditTestbedEvent.TYPE, this);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(final SelectionChangeEvent event) {
				view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
			}
		});
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
	
	@Override
	public void submit() {
		if (!view.validate()) return;
		
		configuration.setName(view.getNameHasText().getText());
		configuration.setTestbedUrl(view.getTestbedUrlHasText().getText());
		configuration.setSnaaEndpointUrl(view.getSnaaEndpointUrlHasText().getText());
		configuration.setRsEndpointUrl(view.getRsEndpointUrlHasText().getText());
		configuration.setSessionmanagementEndpointUrl(view.getSessionManagementEndpointUrlHasText().getText());
		configuration.setFederated(view.getFederatedSelectedIndex() != 0);
		configuration.setUrnPrefixList(urnPrefixProvider.getList());
		
		//TODO: Do here the save operation.
		MessageBox.success(title, configuration.getName() + " was successfully saved.", new MessageBox.Callback() {
			
			@Override
			public void onButtonClicked(final Button button) {
				view.hide();
				eventBus.fireEventFromSource(new RefreshTestbedListEvent(), TestbedEditPresenter.this);
			}
		});
	}

	@Override
	public void cancel() {
		MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {
			
			@Override
			public void onButtonClicked(final Button button) {
				if (Button.OK.equals(button)) {
					view.hide();
				}
			}
		});
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
	public void onEditTestbed(final EditTestbedEvent event) {
		configuration = Objects.firstNonNull(event.getConfiguration(), new TestbedConfiguration());
		title = Objects.firstNonNull(configuration.getName(), DEFAULT_NEW_TITLE);
		loadConfigurationToView();
		view.show(title);
	}
}
