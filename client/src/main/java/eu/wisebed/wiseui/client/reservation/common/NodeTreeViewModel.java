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
import com.google.gwt.view.client.SelectionModel;

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
    	
    	private NodeCell(){}
    	
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
