package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView.Presenter;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class RawWisemlPresenter implements Presenter, ConfigurationSelectedHandler {

	private final EventBus eventBus;
	
	private final RawWisemlView view;
	
	private final SessionManagementServiceAsync sessionManagementService;
	
	private TestbedConfiguration configuration;
	
	@Inject
	public RawWisemlPresenter(final EventBus eventBus, 
							  final RawWisemlView view,
							  final SessionManagementServiceAsync sessionManagementService) {
		this.eventBus = eventBus;
		this.view = view;
		this.sessionManagementService = sessionManagementService;
		bind();
		view.getXmlHasHTML().setText("Select a Testbed Configuration.");
	}
	
	private void bind() {
		eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
	}
	
	@Override
	public void onTestbedConfigurationSelected(ConfigurationSelectedEvent event) {
		configuration = event.getConfiguration();
		view.getXmlHasHTML().setText("Loading WiseML...");
		sessionManagementService.getWisemlAsXml(configuration.getSessionmanagementEndointUrl(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				view.getXmlHasHTML().setHTML("<pre>" + SafeHtmlUtils.htmlEscape(result) + "</pre>");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.getXmlHasHTML().setText("Unable to load WiseML.");
			}
		});
	}
}