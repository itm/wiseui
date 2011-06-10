/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.experimentation.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.experimentation.event.RefreshWiseMLEvent;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentWiseMLOutputView;
import eu.wisebed.wiseui.client.util.EventBusManager;

public class ExperimentWiseMLOutputPresenter implements ExperimentWiseMLOutputView.Presenter{

	private ExperimentWiseMLOutputView view;
	private EventBusManager eventBus;
	
	@Inject
	public ExperimentWiseMLOutputPresenter(
			final ExperimentWiseMLOutputView view,
			final EventBus eventBus) {
		this.setView(view);
		this.view.setPresenter(this);
		this.eventBus = new EventBusManager(eventBus);
	}

	public void setView(ExperimentWiseMLOutputView view) {
		this.view = view;
	}

	public ExperimentWiseMLOutputView getView() {
		return view;
	}

	@Override
	public void fireRefreshWiseMLEvent() {
		final ExperimentWiseMLOutputPresenter source = this;
		eventBus.fireEventFromSource(new RefreshWiseMLEvent(), source);
	}
}
