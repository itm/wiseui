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
package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class ReservationViewImpl extends Composite implements ReservationView {

	@UiTemplate("ReservationViewImpl.ui.xml")
    interface ReservationViewImplUiBinder extends
            UiBinder<Widget, ReservationViewImpl> {
    }

	private static ReservationViewImplUiBinder uiBinder = GWT
    	.create(ReservationViewImplUiBinder.class);

	public ReservationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private Presenter presenter;

    @UiField
    SimplePanel reservationPanel;
    @UiField
    SimplePanel nodeSelectionPanel;

    public SimplePanel getReservationPanel() {
        return reservationPanel;
    }

    public SimplePanel getNodeSelectionPanel() {
        return nodeSelectionPanel;
    }

    public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}
    
    public Presenter getPresenter() {
    	return presenter;
    }
}