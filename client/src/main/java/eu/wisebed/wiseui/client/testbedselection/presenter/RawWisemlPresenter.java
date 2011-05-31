/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

/**
 * The presenter for the {@link RawWisemlView}.
 *
 * @author Malte Legenhausen
 */
public class RawWisemlPresenter implements Presenter, ConfigurationSelectedHandler, PlaceChangeEvent.Handler {

	private final EventBusManager eventBus;
	
	private final RawWisemlView view;
	
	private final SessionManagementServiceAsync sessionManagementService;
	
	private TestbedConfiguration configuration;
	
	@Inject
	public RawWisemlPresenter(final EventBus eventBus, 
				  final RawWisemlView view,
				  final SessionManagementServiceAsync sessionManagementService) {
		this.eventBus = new EventBusManager(eventBus);
		this.view = view;
		this.sessionManagementService = sessionManagementService;
		view.getXmlHasHTML().setText("Select a Testbed Configuration.");
        bind();
	}
	
	public void bind() {
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
		eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
	}
	
	@Override
	public void onTestbedSelected(final TestbedSelectedEvent event) {
		configuration = event.getConfiguration();
		view.getLoadingIndicator().showLoading("Loading Testbed");
		view.getXmlHasHTML().setText("");
		sessionManagementService.getWisemlAsXml(configuration.getSessionmanagementEndpointUrl(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(final String result) {
				view.getXmlHasHTML().setHTML("<pre>" + SafeHtmlUtils.htmlEscape(result) + "</pre>");
				view.getLoadingIndicator().hideLoading();
			}
			
			@Override
			public void onFailure(final Throwable caught) {
				view.getXmlHasHTML().setText("Unable to load WiseML.");
				view.getLoadingIndicator().hideLoading();
			}
		});
	}

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		eventBus.removeAll();
	}
}