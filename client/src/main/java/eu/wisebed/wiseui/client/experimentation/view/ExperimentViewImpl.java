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
package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ExperimentViewImpl extends Composite implements ExperimentView {
    private static ExperimentPanelViewImplBinder uiBinder = GWT.create(ExperimentPanelViewImplBinder.class);

    @UiTemplate("ExperimentViewImpl.ui.xml")
    interface ExperimentPanelViewImplBinder extends UiBinder<Widget, ExperimentViewImpl> {
    }
    
    private Presenter presenter;
    
    @UiField 
	Label secretReservationKey;
	@UiField 
	Label startDate;
	@UiField 
	Label stopDate;
	@UiField
	Label experimentTiming;
	@UiField
	Label status;
	@UiField
	Label username;
	
	
	public ExperimentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}
	
	public Presenter getPresenter() {
		return presenter;
	}
	
	@Override
	public String getSecretReservationKey() {
		return secretReservationKey.getText();
	}
	
	@Override
	public void setSecretReservationKey(final String text) {
		secretReservationKey.setText(text);
	}
	
	@Override
	public String getStartDate() {
		return startDate.getText();
	}

	@Override
	public void setStartDate(final String text) {
		startDate.setText(text);
	}

	@Override
	public String getStopDate() {
		return stopDate.getText();
	}

	@Override
	public void setStopDate(final String text) {
		stopDate.setText(text);
	}

	@Override
	public void setExperimentTiming(String timing) {
		experimentTiming.setText(timing);
	}

	@Override
	public String getExperimentTiming() {
		return experimentTiming.getText();
	}

	@Override
	public void setStatus(String value) {
		status.setText(value);
	}

	@Override
	public String getStatus() {
		return status.getText();
	}

	@Override
	public void setNodeUrns(List<String> nodeUrns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNodeUrns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUsername(String username) {
		this.username.setText(username);
	}

	@Override
	public String getUsername() {
		return username.getText();
	}
}
