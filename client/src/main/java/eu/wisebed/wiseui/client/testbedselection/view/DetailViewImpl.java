package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

public class DetailViewImpl extends Composite implements DetailView {

    private static DetailViewImplUiBinder uiBinder = GWT.create(DetailViewImplUiBinder.class);

    interface DetailViewImplUiBinder extends UiBinder<Widget, DetailViewImpl> {
    }
    @UiField
    SimplePanel treeContainer;
    @UiField
    Label idLabel;
    @UiField
    Label positionLabel;
    @UiField
    Label gatewayLabel;
    @UiField
    Label descriptionLabel;
    @UiField
    Label programDetailsLabel;

    public DetailViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(final Presenter presenter) {
    }

    private CellTree createTree(final TreeViewModel model) {
        final CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
        final CellTree cellTree = new CellTree(model, null, res);
        cellTree.setAnimationEnabled(true);
        return cellTree;
    }

    @Override
    public void setTreeViewModel(TreeViewModel model) {
        final CellTree tree = createTree(model);
        treeContainer.clear();
        treeContainer.add(tree);
    }

    @Override
    public HasText getNodeIdHasText() {
        return idLabel;
    }

    @Override
    public HasText getNodePositionHasText() {
        return positionLabel;
    }

    @Override
    public HasText getNodeGatewayHasText() {
        return gatewayLabel;
    }

    @Override
    public HasText getNodeProgramDetailsHasText() {
        return programDetailsLabel;
    }

    @Override
    public HasText getNodeDescriptionHasText() {
        return descriptionLabel;
    }
}
