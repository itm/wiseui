/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedlist.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedlist.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.presenter.DetailPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.MapPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.RawWisemlPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.TestbedSelectionPresenter;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;
import eu.wisebed.wiseui.client.util.DefaultsHelper;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.Wiseml;


/**
 * Activity for the testbed selection part of the wiseui.
 * 
 * @author Malte Legenhausen
 */
public class TestbedSelectionActivity extends AbstractActivity implements ConfigurationSelectedHandler {
	
    private final SessionManagementServiceAsync sessionManagementService;
    
    private final WiseUiGinjector injector;
    
    private WiseUiPlace place;

    private EventBus eventBus;

    @Inject
    public TestbedSelectionActivity(final WiseUiGinjector injector,
                                    final SessionManagementServiceAsync sessionManagementService) {
        this.injector = injector;
        this.sessionManagementService = sessionManagementService;
    }

    public void setPlace(final WiseUiPlace place) {
        this.place = place;
    }

    /**
     * Invoked by the ActivityManager to start a new {@link com.google.gwt.activity.shared.Activity}.
     */
    public void start(final AcceptsOneWidget containerWidget, final EventBus eventBus) {
        this.eventBus = eventBus;
        initTestbedSelectionPart(containerWidget);
        
        bind();
    }
    
    public void bind() {
    	eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
    }
    
    private void initTestbedSelectionPart(final AcceptsOneWidget container) {
        GWT.log("Init Testbed Selection Part");
        final TestbedSelectionPresenter testbedSelectionPresenter = injector.getTestbedSelectionPresenter();
        testbedSelectionPresenter.setPlace(place);
        final TestbedSelectionView testbedSelectionView = injector.getTestbedSelectionView();
        testbedSelectionView.setPresenter(testbedSelectionPresenter);
        final TestbedSelectionPlace testbedSelectionPlace = (TestbedSelectionPlace) place.get(TestbedSelectionPlace.class);
        initContentPart(testbedSelectionView, testbedSelectionPlace.getView());
        container.setWidget(testbedSelectionView.asWidget());
    }
    
    private void initContentPart(final TestbedSelectionView testbedSelectionView, final String view) {
        if (TestbedSelectionConstants.MAP_VIEW.equals(view)) {
            initMapPart(testbedSelectionView);
        }
        else if (TestbedSelectionConstants.DETAIL_VIEW.equals(view)) {
            initDetailPart(testbedSelectionView);
        }
        else if (TestbedSelectionConstants.RAW_WISEML_VIEW.equals(view)) {
            initRawWisemlPart(testbedSelectionView);
        }
        else {
            initMapPart(testbedSelectionView);
        }
    }

    private void initDetailPart(final TestbedSelectionView testbedSelectionView) {
        GWT.log("Init Testbed Detail Part");
        final DetailPresenter detailPresenter = injector.getDetailPresenter();
        final DetailView detailView = injector.getDetailView();
        detailView.setPresenter(detailPresenter);
        testbedSelectionView.getContentContainer().setWidget(detailView);
    }

    private void initMapPart(final TestbedSelectionView testbedSelectionView) {
        GWT.log("Init Testbed Map Part");
        final MapPresenter mapPresenter = injector.getMapPresenter();
        final MapView mapView = injector.getMapView();
        mapView.setPresenter(mapPresenter);
        testbedSelectionView.getContentContainer().setWidget(mapView);
    }
    
    private void initRawWisemlPart(final TestbedSelectionView testbedSelectionView) {
    	GWT.log("Init Testbed Raw WiseML Part");
    	final RawWisemlPresenter rawWisemlPresenter = injector.getRawWisemlPresenter();
    	final RawWisemlView rawWisemlView = injector.getRawWisemlView();
    	rawWisemlView.setPresenter(rawWisemlPresenter);
    	testbedSelectionView.getContentContainer().setWidget(rawWisemlView);
    }

    public void onTestbedSelected(final TestbedSelectedEvent event) {
        final TestbedConfiguration configuration = event.getConfiguration();
        final AsyncCallback<Wiseml> callback = new AsyncCallback<Wiseml>() {

            public void onSuccess(final Wiseml result) {
            	if (result != null) {
            		result.setSetup(DefaultsHelper.apply(result.getSetup()));
            	}
                eventBus.fireEvent(new WisemlLoadedEvent(result));
            }

            public void onFailure(final Throwable caught) {
                eventBus.fireEvent(new ThrowableEvent(caught));
            }
        };
        final String url = configuration.getSessionmanagementEndpointUrl().trim();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                sessionManagementService.getWiseml(url, callback);
            }
        });
    }
}
