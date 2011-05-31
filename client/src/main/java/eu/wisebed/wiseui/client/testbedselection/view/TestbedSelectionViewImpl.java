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
package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionParams;

@Singleton
public class TestbedSelectionViewImpl extends Composite implements TestbedSelectionView {

    private static TestbedSelectionViewImplUiBinder uiBinder = GWT.create(TestbedSelectionViewImplUiBinder.class);

    interface TestbedSelectionViewImplUiBinder extends UiBinder<Widget, TestbedSelectionViewImpl> {
    }

    @UiField
    SimplePanel contentPanel;
    @UiField
    PushButton reloadButton;
    @UiField
    ToggleButton mapToggleButton;
    @UiField
    ToggleButton detailToggleButton;
    @UiField
    ToggleButton rawToggleButton;

    private Presenter presenter;

    @Inject
    public TestbedSelectionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("reloadButton")
    public void handleReloadClick(final ClickEvent event) {
        presenter.reload();
    }

    @UiHandler("mapToggleButton")
    public void handleMapAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionParams.MAP_VIEW.getValue());
    }

    @UiHandler("detailToggleButton")
    public void handleDetailAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionParams.DETAIL_VIEW.getValue());
    }

    @UiHandler("rawToggleButton")
    public void handleRawAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionParams.RAW_WISEML_VIEW.getValue());
    }

    @Override
    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public AcceptsOneWidget getContentContainer() {
        return contentPanel;
    }

    @Override
    public HasEnabled getReloadEnabled() {
        return reloadButton;
    }

	@Override
	public void setContentSelection(final String view) {
		mapToggleButton.setDown(TestbedSelectionParams.MAP_VIEW.getValue().equals(view));
		detailToggleButton.setDown(TestbedSelectionParams.DETAIL_VIEW.getValue().equals(view));
		rawToggleButton.setDown(TestbedSelectionParams.RAW_WISEML_VIEW.getValue().equals(view));
	}
}