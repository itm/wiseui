package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NodeDetailViewImpl extends Composite implements NodeDetailView {

	private static NodeDetailViewImplUiBinder uiBinder = GWT.create(NodeDetailViewImplUiBinder.class);

	interface NodeDetailViewImplUiBinder extends UiBinder<Widget, NodeDetailViewImpl> {
	}
	
	public NodeDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {

	}

}
