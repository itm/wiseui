package eu.wisebed.wiseui.client.testbedselection.view;

// import at.wizzart.gwt.widgets.client.CodeMirror;
// import at.wizzart.gwt.widgets.client.CodeMirrorConfiguration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;

public class RawWisemlViewImpl extends Composite implements RawWisemlView {

    private static RawWisemlViewImplUiBinder uiBinder = GWT.create(RawWisemlViewImplUiBinder.class);

	interface RawWisemlViewImplUiBinder extends UiBinder<Widget, RawWisemlViewImpl> {
	}

	@UiField
	HTML xml;

//    @UiField
//    CodeMirror codeMirror;
	
	public RawWisemlViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasHTML getXmlHasHTML() {
		return xml;
	}

//    public void setCodeMirrorContent(String content) {
//        codeMirror.setContent(content);
//    }
//
//    @Override
//    public String getCodeMirrorContent() {
//        return codeMirror.getContent();
//    }

    @Override
	public void setPresenter(Presenter presenter) {
		
	}
}
