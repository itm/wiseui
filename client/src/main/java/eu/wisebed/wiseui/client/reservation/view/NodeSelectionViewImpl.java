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
package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

/**
 * @author Soenke Nommensen
 */
@Singleton
public class NodeSelectionViewImpl extends Composite implements NodeSelectionView {

    @UiTemplate("NodeSelectionViewImpl.ui.xml")
    interface NodeSelectionViewImplUiBinder extends UiBinder<Widget, NodeSelectionViewImpl> {
    }

    private static NodeSelectionViewImplUiBinder uiBinder = GWT.create(NodeSelectionViewImplUiBinder.class);

    private Presenter presenter;

    @UiField
    CaptionPanel container;
    @UiField
    SimplePanel treeContainer;

    public NodeSelectionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
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

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public HasLoadingIndicator getLoadingIndicator() {
        return container;
    }

    public CaptionPanel getContainer() {
        return container;
    }
}