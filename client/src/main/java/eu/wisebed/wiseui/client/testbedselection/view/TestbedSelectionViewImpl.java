package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;

public class TestbedSelectionViewImpl extends Composite implements TestbedSelectionView {

    private static TestbedSelectionViewImplUiBinder uiBinder = GWT.create(TestbedSelectionViewImplUiBinder.class);

    interface TestbedSelectionViewImplUiBinder extends UiBinder<Widget, TestbedSelectionViewImpl> {
    }

    @UiField
    SimplePanel configurationContainer;
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button reloadButton;
    @UiField
    Button loginButton;
    @UiField
    Anchor mapAnchor;
    @UiField
    Anchor detailAnchor;
    @UiField
    Anchor rawAnchor;

    private Presenter presenter;

    @Inject
    public TestbedSelectionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("reloadButton")
    public void handleReloadClick(final ClickEvent event) {
        presenter.reload();
    }

    @UiHandler("loginButton")
    public void handleLoginClick(final ClickEvent event) {
        presenter.showLoginDialog();
    }

    @UiHandler("mapAnchor")
    public void handleMapAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionConstants.MAP_VIEW);
    }

    @UiHandler("detailAnchor")
    public void handleDetailAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionConstants.DETAIL_VIEW);
    }

    @UiHandler("rawAnchor")
    public void handleRawAnchorClicked(final ClickEvent event) {
        presenter.setContentSelection(TestbedSelectionConstants.RAW_WISEML_VIEW);
    }

    @Override
    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public AcceptsOneWidget getContentContainer() {
        return contentPanel;
    }

    @Override
    public HasEnabled getLoginEnabled() {
        return loginButton;
    }

    @Override
    public HasEnabled getReloadEnabled() {
        return reloadButton;
    }

	@Override
	public AcceptsOneWidget getConfigurationContainer() {
		return configurationContainer;
	}
}