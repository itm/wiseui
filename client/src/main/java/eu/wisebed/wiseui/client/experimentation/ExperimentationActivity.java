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
package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;

public class ExperimentationActivity extends AbstractActivity {

    private final WiseUiGinjector injector;
    private WiseUiPlace place;
    
    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector){
    	this.injector = injector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    	initExperimentationPanel(panel);
    }
    
    private void initExperimentationPanel(final AcceptsOneWidget panel) {
    	GWT.log("Intializing Experimentation Panel");
    	final ExperimentationPresenter presenter = injector.getExperimentationPresenter();
    	final ExperimentationView view = injector.getExperimentationView();
    	
    	presenter.setPlace(place);
    	view.setPresenter(presenter);
    	panel.setWidget(view.asWidget());
    }
    
    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
    }
}
