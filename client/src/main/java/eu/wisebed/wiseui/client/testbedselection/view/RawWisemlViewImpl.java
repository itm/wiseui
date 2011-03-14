package eu.wisebed.wiseui.client.testbedselection.view;

import com.alexgorbatchev.syntaxhighlighter.client.Highlighter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RawWisemlViewImpl extends Composite implements RawWisemlView {

    private static RawWisemlViewImplUiBinder uiBinder = GWT.create(RawWisemlViewImplUiBinder.class);

    interface RawWisemlViewImplUiBinder extends UiBinder<Widget, RawWisemlViewImpl> {
    }

    @UiField
    Highlighter xml;

    public RawWisemlViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Highlighter getHighlighter() {
        return xml;
    }

    @Override
    public void setPresenter(Presenter presenter) {

    }
}
