package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import eu.wisebed.wiseui.shared.wiseml.Node;

import java.util.ArrayList;
import java.util.List;

public class NetworkViewImpl extends Composite implements NetworkView {

    private static NetworkViewImplUiBinder uiBinder = GWT
            .create(NetworkViewImplUiBinder.class);

    interface NetworkViewImplUiBinder extends UiBinder<Widget, NetworkViewImpl> {
    }

    @UiField
    CellTable<Node> nodeTable;

    public NetworkViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        nodeTable.setWidth("100%");

        final TextColumn<Node> idColumn = new TextColumn<Node>() {

            @Override
            public String getValue(final Node node) {
                return node.getId();
            }
        };
        nodeTable.addColumn(idColumn, "ID");

        final TextColumn<Node> nodeTypeColumn = new TextColumn<Node>() {

            @Override
            public String getValue(final Node node) {
                return node.getNodeType();
            }
        };
        nodeTable.addColumn(nodeTypeColumn, "Node-Type");

        final TextColumn<Node> positionColumn = new TextColumn<Node>() {

            @Override
            public String getValue(final Node node) {
                return node.getPosition().toString();
            }
        };
        nodeTable.addColumn(positionColumn, "Position");

        final TextColumn<Node> descriptionColumn = new TextColumn<Node>() {

            @Override
            public String getValue(final Node node) {
                return node.getDescription();
            }
        };
        nodeTable.addColumn(descriptionColumn, "Description");

        setNodes(new ArrayList<Node>(0));
    }

    public void setPresenter(final Presenter presenter) {

    }

    public void setNodes(final List<Node> nodes) {
        nodeTable.setRowCount(nodes.size());
        nodeTable.setPageSize(nodes.size());
        nodeTable.setRowData(0, nodes);
    }

}
