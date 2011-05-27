/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedselection.common;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import eu.wisebed.wiseui.shared.dto.Capability;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Malte Legenhausen
 */
public class TestbedTreeViewModel implements TreeViewModel {

    protected final TestbedConfiguration configuration;

    protected final List<NodeGroup> groups;

    public final SelectionModel<Node> nodeSelectionModel;

    private final int nodeCount;

    public TestbedTreeViewModel(final TestbedConfiguration configuration, final List<Node> nodes, final SelectionModel<Node> nodeSelectionModel) {
        this.configuration = configuration;
        this.groups = initGroups(nodes);
        this.nodeSelectionModel = nodeSelectionModel;
        this.nodeCount = nodes.size();
    }

    public static List<NodeGroup> initGroups(final List<Node> nodes) {
        final Map<String, NodeGroup> groups = new HashMap<String, NodeGroup>();
        for (final Node node : nodes) {
            String nodeType = node.getNodeType();
            if (nodeType == null || nodeType.isEmpty()) {
                nodeType = "Unknown";
            }
            if (!groups.containsKey(nodeType)) {
                groups.put(nodeType, new NodeGroup(nodeType));
            }
            final NodeGroup group = groups.get(nodeType);
            group.getNodes().add(node);
        }
        return new ArrayList<NodeGroup>(groups.values());
    }

    public NodeInfo<TestbedConfiguration> getTestbedConfiguration() {
        final ListDataProvider<TestbedConfiguration> provider = new ListDataProvider<TestbedConfiguration>(Arrays.asList(configuration));
        final Cell<TestbedConfiguration> cell = new AbstractCell<TestbedConfiguration>() {
            @Override
            public void render(final Context context, final TestbedConfiguration value, final SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getName()).appendEscaped(" (");
                    sb.append(nodeCount).appendEscaped(" Nodes)");
                }
            }
        };
        return new DefaultNodeInfo<TestbedConfiguration>(provider, cell);
    }

    public NodeInfo<NodeGroup> getNodeGroups() {
        final ListDataProvider<NodeGroup> dataProvider = new ListDataProvider<NodeGroup>(groups);
        final Cell<NodeGroup> cell = new AbstractCell<NodeGroup>() {
            @Override
            public void render(final Context context, final NodeGroup value, final SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getName()).appendEscaped(" (");
                    sb.append(value.getNodes().size()).appendEscaped(" Nodes)");
                }
            }
        };
        return new DefaultNodeInfo<NodeGroup>(dataProvider, cell);
    }

    private NodeInfo<Node> getNodes(final NodeGroup group) {
        final ListDataProvider<Node> dataProvider = new ListDataProvider<Node>(group.getNodes());
        final Cell<Node> cell = new AbstractCell<Node>() {
            @Override
            public void render(final Context context, final Node value, final SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getId());
                }
            }
        };
        return new DefaultNodeInfo<Node>(dataProvider, cell, nodeSelectionModel, null);
    }

    public NodeInfo<?> getCapabilities(final Node value) {
        final ListDataProvider<Capability> dataProvider = new ListDataProvider<Capability>(value.getCapability());
        final Cell<Capability> cell = new AbstractCell<Capability>() {
            @Override
            public void render(final Context context, final Capability value, final SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getName()).appendEscaped(" (");
                    sb.appendEscaped(value.getDatatype().toString()).appendEscaped(", ");
                    sb.appendEscaped(value.getUnit().toString()).appendEscaped(")");
                }
            }
        };
        return new DefaultNodeInfo<Capability>(dataProvider, cell);
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
        return value instanceof Capability;
    }
}
