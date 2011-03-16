package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;

public class Defaults implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4688994277860356914L;
	
	private NodeProperties node;
	
	public Defaults() {
		
	}

	public NodeProperties getNode() {
		return node;
	}

	public void setNode(final NodeProperties node) {
		this.node = node;
	}
}
