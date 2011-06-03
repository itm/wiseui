/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.testbedlist.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.famfamfam.FamFamFamResources;

import java.util.List;


@Singleton
public class TestbedListViewImpl extends Composite implements TestbedListView {

    private static TestbedListViewImplUiBinder uiBinder = GWT.create(TestbedListViewImplUiBinder.class);

    interface TestbedListViewImplUiBinder extends UiBinder<Widget, TestbedListViewImpl> {
    }

    @UiField
    CellList<TestbedConfiguration> configurationList;
    @UiField
    PushButton addButton;
    @UiField
    PushButton editButton;
    @UiField
    PushButton deleteButton;
    @UiField
    PushButton loginButton;
    @UiField
    PushButton refreshButton;

    private final FamFamFamResources images = GWT.create(FamFamFamResources.class);

    private Presenter presenter;

    public TestbedListViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    class TestbedCell extends AbstractCell<TestbedConfiguration> {

        private final String connectedHtml;
        private final String disconnectedHtml;

        public TestbedCell(final ImageResource connected, final ImageResource disconnected) {
            this.connectedHtml = AbstractImagePrototype.create(connected).getHTML();
            this.disconnectedHtml = AbstractImagePrototype.create(disconnected).getHTML();
        }

        @Override
        public void render(final Cell.Context context,
                           final TestbedConfiguration configuration,
                           final SafeHtmlBuilder builder) {
            if (configuration != null) {
                builder.appendHtmlConstant("<div class=\"celllist-entry\">");
                builder.appendEscaped(configuration.getName()).appendEscaped(" ");
                builder.appendHtmlConstant("<div style=\"float:right\">");
                if (presenter.isAuthenticated(configuration)) {
                    builder.appendHtmlConstant(connectedHtml).appendEscaped(" ");
                } else {
                    builder.appendHtmlConstant(disconnectedHtml).appendEscaped(" ");
                }
                builder.appendHtmlConstant("</div></div>");
            }
        }
    }

    @UiFactory
    public CellList<TestbedConfiguration> createTestbedConfigurationCellList() {
        return new CellList<TestbedConfiguration>(new TestbedCell(images.bulletGreen(), images.bulletOrange()));
    }

    @UiHandler("loginButton")
    public void handleLoginClick(final ClickEvent event) {
        presenter.showLoginDialog();
    }

    @UiHandler("addButton")
    public void handleAddClick(final ClickEvent event) {
        presenter.showNewTestbedDialog();
    }

    @UiHandler("editButton")
    public void handleEditClick(final ClickEvent event) {
        presenter.showEditTestbedDialog();
    }

    @UiHandler("deleteButton")
    public void handleDeleteClick(final ClickEvent event) {
        presenter.deleteTestbed();
    }

    @UiHandler("refreshButton")
    public void handleRefreshClick(final ClickEvent event) {
        presenter.refresh();
    }


    @Override
    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setConfigurations(final List<TestbedConfiguration> configurations) {
        configurationList.setRowCount(configurations.size());
        configurationList.setRowData(0, configurations);
    }

    @Override
    public HasEnabled getLoginEnabled() {
        return loginButton;
    }

    @Override
    public HasEnabled getTestbedEditEnabled() {
        return editButton;
    }

    @Override
    public HasEnabled getTestbedDeleteEnabled() {
        return deleteButton;
    }

    @Override
    public void setTestbedConfigurationSelectionModel(final SelectionModel<TestbedConfiguration> selectionModel) {
        configurationList.setSelectionModel(selectionModel);
    }
}
