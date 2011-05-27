package eu.wisebed.wiseui.client.reservation.common;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import eu.wisebed.wiseui.client.testbedselection.common.NodeGroup;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedTreeViewModel;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

import java.util.ArrayList;
import java.util.List;

public class NodeTreeViewModel extends TestbedTreeViewModel {

    private final MultiSelectionModel<Node> nodeSelectionModel;

    private final MultiSelectionModel<NodeGroup> nodeGroupSelectionModel;

    public NodeTreeViewModel(final TestbedConfiguration configuration,
                             final List<Node> nodes,
                             final MultiSelectionModel<Node> nodeSelectionModel,
                             final MultiSelectionModel<NodeGroup> nodeGroupSelectionModel) {
        super(configuration, nodes, nodeSelectionModel);
        this.nodeSelectionModel = nodeSelectionModel;
        this.nodeGroupSelectionModel = nodeGroupSelectionModel;
    }

    private static class NodeGroupCell extends AbstractCell<NodeGroup> {
        @Override
        public void render(final Context context, final NodeGroup value, final SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName()).appendEscaped(" (");
                sb.append(value.getNodes().size()).appendEscaped(" Nodes)");
            }
        }
    }

    private static class NodeCell extends AbstractCell<Node> {
        /*
         * A simple cell used for rendering node information.
         */
        @Override
        public void render(final Context context, final Node value, final SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getId());
            }
        }
    }

    @Override
    public NodeInfo<NodeGroup> getNodeGroups() {
        final ListDataProvider<NodeGroup> dataProvider = new ListDataProvider<NodeGroup>(groups);
        List<HasCell<NodeGroup, ?>> hasCells = new ArrayList<HasCell<NodeGroup, ?>>();
        hasCells.add(new HasCell<NodeGroup, NodeGroup>() {
            /*
             * Add node information.
             */
            private NodeGroupCell cell = new NodeGroupCell();

            public Cell<NodeGroup> getCell() {
                return cell;
            }

            public FieldUpdater<NodeGroup, NodeGroup> getFieldUpdater() {
                return null;
            }

            public NodeGroup getValue(NodeGroup object) {
                return object;
            }
        });
        hasCells.add(new HasCell<NodeGroup, Boolean>() {
            /*
             * Add a checkbox to select indicate a node is selected.
             */
            private CheckboxCell checkbox = new CheckboxCell(true, false);

            public Cell<Boolean> getCell() {
                return checkbox;
            }

            public FieldUpdater<NodeGroup, Boolean> getFieldUpdater() {
                return null;
            }

            @Override
            public Boolean getValue(NodeGroup object) {
                return nodeGroupSelectionModel.isSelected(object);
            }
        });
        Cell<NodeGroup> nodeGroupCell = new CompositeCell<NodeGroup>(hasCells) {
            /*
             * Create a composite cell which consist of a checkbox and a regular cell.
             */
            @Override
            public void render(Context context, NodeGroup value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, NodeGroup value,
                                      SafeHtmlBuilder sb, HasCell<NodeGroup, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");

            }
        };
        return new DefaultNodeInfo<NodeGroup>(dataProvider, nodeGroupCell, nodeGroupSelectionModel, null);
    }

    private NodeInfo<Node> getNodes(final NodeGroup group) {
        final ListDataProvider<Node> dataProvider = new ListDataProvider<Node>(group.getNodes());
        List<HasCell<Node, ?>> hasCells = new ArrayList<HasCell<Node, ?>>();
        hasCells.add(new HasCell<Node, Node>() {
            /*
             * Add node information.
             */
            private NodeCell cell = new NodeCell();

            public Cell<Node> getCell() {
                return cell;
            }

            public FieldUpdater<Node, Node> getFieldUpdater() {
                return null;
            }

            public Node getValue(Node object) {
                return object;
            }
        });
        hasCells.add(new HasCell<Node, Boolean>() {
            /*
             * Add a checkbox to select indicate a node is selected.
             */
            private CheckboxCell checkbox = new CheckboxCell(true, false);

            public Cell<Boolean> getCell() {
                return checkbox;
            }

            public FieldUpdater<Node, Boolean> getFieldUpdater() {
                return null;
            }

            public Boolean getValue(Node object) {
                return nodeSelectionModel.isSelected(object);
            }
        });
        Cell<Node> nodeCell = new CompositeCell<Node>(hasCells) {
            /*
             * Create a composite cell which consist of a checkbox and a regular cell.
             */
            @Override
            public void render(Context context, Node value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, Node value,
                                      SafeHtmlBuilder sb, HasCell<Node, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
        return new DefaultNodeInfo<Node>(dataProvider, nodeCell, nodeSelectionModel, null);
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(final T value) {
        NodeInfo<?> nodeInfo = null;
        if (value == null) {
            nodeInfo = getTestbedConfiguration();
        } else if (value instanceof TestbedConfiguration) {
            nodeInfo = getNodeGroups();
        } else if (value instanceof NodeGroup) {
            nodeInfo = getNodes((NodeGroup) value);
        } else if (value instanceof Node) {
            nodeInfo = getCapabilities((Node) value);
        }
        return nodeInfo;
    }

    @Override
    public boolean isLeaf(final Object value) {
        return value instanceof Node;
    }
}
