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
package eu.wisebed.wiseui.widgets;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;
import eu.wisebed.wiseui.widgets.loading.LoadingIndicator;

public class CaptionPanel extends Composite implements HasWidgets, HasLoadingIndicator {

	private static CaptionPanelUiBinder uiBinder = GWT.create(CaptionPanelUiBinder.class);

    interface CaptionPanelUiBinder extends UiBinder<Widget, CaptionPanel> {
    }
	
	@UiField
	Label caption;
	
	@UiField
	SimplePanel content;
	
	private LoadingIndicator loadingIndicator;
	
	public CaptionPanel() {
		this("");
	}
	
	@UiConstructor
	public CaptionPanel(final String caption) {
		initWidget(uiBinder.createAndBindUi(this));
		this.caption.setText(caption);
	}
	
	public HasText getCaption() {
		return caption;
	}
	
	@Override
	public void add(final Widget widgets) {
		content.add(widgets);
	}

	@Override
	public void clear() {
		content.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return content.iterator();
	}

	@Override
	public boolean remove(final Widget widgets) {
		return content.remove(widgets);
	}

	@Override
	public void showLoading(String text) {
		loadingIndicator = LoadingIndicator.on(content).show(text);
	}

	@Override
	public void hideLoading() {
		if (loadingIndicator != null) {
			loadingIndicator.hide();
		}
	}
}
