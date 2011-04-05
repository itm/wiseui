package eu.wisebed.wiseui.client.testbedselection.common;

import java.util.ArrayList;
import java.util.List;

import eu.wisebed.wiseui.shared.dto.Node;

public class NodeGroup {

	private final String name;
	
	private final List<Node> nodes = new ArrayList<Node>();
	
	public NodeGroup(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
}
