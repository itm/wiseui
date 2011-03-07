package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;

public class RawWisemlViewImpl extends Composite implements RawWisemlView {

    private static RawWisemlViewImplUiBinder uiBinder = GWT.create(RawWisemlViewImplUiBinder.class);

<<<<<<< HEAD
    interface RawWisemlViewImplUiBinder extends UiBinder<Widget, RawWisemlViewImpl> {
    }

    public RawWisemlViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }
=======
	interface RawWisemlViewImplUiBinder extends UiBinder<Widget, RawWisemlViewImpl> {
	}
	
	@UiField
	HTML xml;
	
	public RawWisemlViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasHTML getXmlHasHTML() {
		return xml;
	}
>>>>>>> c1e550e1c2d9d78cf7e0f0e785c229680f9ffe85
}
