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
package eu.wisebed.wiseui.client.reservation.common;

import java.util.ArrayList;
import java.util.List;

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

public class NodeTreeViewModel extends TestbedTreeViewModel{

	private Cell<Node> nodeCell;

	private final MultiSelectionModel<Node> nodeSelectionModel;
	
    public NodeTreeViewModel(final TestbedConfiguration configuration, final List<Node> nodes, final MultiSelectionModel<Node> nodeSelectionModel) {
    	super(configuration, nodes, nodeSelectionModel);
    	this.nodeSelectionModel = nodeSelectionModel;
    }
    
    private static class NodeCell extends AbstractCell<Node>{
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

    private NodeInfo<Node> getNodes(final NodeGroup group) {
        final ListDataProvider<Node> dataProvider = new ListDataProvider<Node>(group.getNodes());
        List<HasCell<Node, ?>> hasCells = new ArrayList<HasCell<Node, ?>>();
        hasCells.add(new HasCell<Node, Boolean>(){
        	/*
        	 * Add a checkbox to select indicate a node is selected.
        	 */
        	private CheckboxCell checkbox = new CheckboxCell(true, false);
        	
        	public Cell<Boolean> getCell(){
        		return checkbox;
        	}
        	
        	public FieldUpdater<Node, Boolean> getFieldUpdater(){
        		return null;
        	}
        	
        	public Boolean getValue(Node object){
        		return nodeSelectionModel.isSelected(object);
        	}
        });
        hasCells.add(new HasCell<Node, Node>(){

        	/*
        	 * Add node information.
        	 */
        	private NodeCell cell  = new NodeCell();
        	
            public Cell<Node> getCell(){
            	return cell; 
            }
            
            public FieldUpdater<Node, Node> getFieldUpdater(){
            	return null;
            }
            
            public Node getValue(Node object){
            	return object;
            }
        });
        nodeCell = new CompositeCell<Node>(hasCells) {
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
