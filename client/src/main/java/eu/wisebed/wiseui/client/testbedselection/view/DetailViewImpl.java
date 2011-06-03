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
package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.Capability;
import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;


/**
 * View for the testbed details.
 * 
 * @author Malte Legenhausen
 */
@Singleton
public class DetailViewImpl extends Composite implements DetailView {
	
	private static DetailViewImplUiBinder uiBinder = GWT.create(DetailViewImplUiBinder.class);

	interface DetailViewImplUiBinder extends UiBinder<Widget, DetailViewImpl> {
	}

	@UiField
	SimplePanel treeContainer;
	
	@UiField
	Label description;
	@UiField
	Label nodeIdLabel;
	@UiField
	Label nodeTypeLabel;
	@UiField
	Label nodePositionLabel;
	@UiField
	Label nodeGatewayLabel;
	@UiField
	Label nodeDescriptionLabel;
	@UiField
	Label nodeProgramDetailsLabel;
	@UiField
	CellTable<Capability> capabilitesTable;
	@UiField
	CaptionPanel container;

	public DetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiFactory
    protected CellTable<Capability> createCellTable() {
        final CellTable<Capability> cellTable = new CellTable<Capability>();
        final Column<Capability, String> nameColumn = new Column<Capability, String>(new TextCell()) {
			@Override
			public String getValue(final Capability value) {
				return value.getName();
			}
		};
        cellTable.addColumn(nameColumn, "Name");
        
        final Column<Capability, String> datatypeColumn = new Column<Capability, String>(new TextCell()) {
        	@Override
        	public String getValue(final Capability value) {
        		return value.getDatatype().toString();
        	}
        };
        cellTable.addColumn(datatypeColumn, "Datatype");
        
        final Column<Capability, String> unitsColumn = new Column<Capability, String>(new TextCell()) {
        	@Override
        	public String getValue(final Capability value) {
        		return value.getUnit().toString();
        	}
        };
        cellTable.addColumn(unitsColumn, "Units");
        return cellTable;
    }
	
	public void setPresenter(final Presenter presenter) {

	}

	private CellTree createTree(final TreeViewModel model) {
		final CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		final CellTree cellTree = new CellTree(model, null, res);
		cellTree.setAnimationEnabled(true);
		return cellTree;
	}

	@Override
	public void setTreeViewModel(final TreeViewModel model) {
		final CellTree tree = createTree(model);
		treeContainer.clear();
		treeContainer.add(tree);
	}

	@Override
	public HasText getNodeIdHasText() {
		return nodeIdLabel;
	}

	@Override
	public HasText getNodePositionHasText() {
		return nodePositionLabel;
	}

	@Override
	public HasText getNodeGatewayHasText() {
		return nodeGatewayLabel;
	}

	@Override
	public HasText getNodeProgramDetailsHasText() {
		return nodeProgramDetailsLabel;
	}

	@Override
	public HasText getNodeDescriptionHasText() {
		return nodeDescriptionLabel;
	}

	@Override
	public void showMessage(final String message) {
		treeContainer.clear();
		treeContainer.add(new Label(message));
	}

	@Override
	public HasData<Capability> getCapababilitesList() {
		return capabilitesTable;
	}

	@Override
	public HasText getDescriptionHasText() {
		return description;
	}

	@Override
	public HasText getNodeTypeHasText() {
		return nodeTypeLabel;
	}

	@Override
	public HasLoadingIndicator getLoadingIndicator() {
		return container;
	}
}