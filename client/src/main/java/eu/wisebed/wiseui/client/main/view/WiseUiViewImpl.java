package eu.wisebed.wiseui.client.main.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class WiseUiViewImpl extends Composite implements WiseUiView {

    interface WiseUiViewImplUiBinder extends UiBinder<Widget, WiseUiViewImpl> {
    }

    private static WiseUiViewImplUiBinder uiBinder = GWT.create(WiseUiViewImplUiBinder.class);

    @UiField
    SimplePanel navigationPanel;
    @UiField
    SimplePanel contentPanel;

    public WiseUiViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        setWidth("100%");
        setHeight("100%");
    }

    public AcceptsOneWidget getNavigationPanel() {
        return navigationPanel;
    }

    public AcceptsOneWidget getContentPanel() {
        return contentPanel;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
