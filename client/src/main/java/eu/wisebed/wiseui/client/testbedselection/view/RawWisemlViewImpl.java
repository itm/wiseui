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
package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@Singleton
public class RawWisemlViewImpl extends Composite implements RawWisemlView {

    private static RawWisemlViewImplUiBinder uiBinder = GWT.create(RawWisemlViewImplUiBinder.class);

    interface RawWisemlViewImplUiBinder extends UiBinder<Widget, RawWisemlViewImpl> {
    }

    @UiField
    HTML xml;
    
    @UiField
    CaptionPanel container;

    public RawWisemlViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HasHTML getXmlHasHTML() {
        return xml;
    }

    @Override
    public void setPresenter(final Presenter presenter) {

    }

	@Override
	public HasLoadingIndicator getLoadingIndicator() {
		return container;
	}
}
