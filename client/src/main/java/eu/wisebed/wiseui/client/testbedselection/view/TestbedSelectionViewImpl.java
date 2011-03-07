package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TestbedSelectionViewImpl extends Composite implements TestbedSelectionView {

    private static TestbedSelectionViewImplUiBinder uiBinder = GWT.create(TestbedSelectionViewImplUiBinder.class);

    interface TestbedSelectionViewImplUiBinder extends UiBinder<Widget, TestbedSelectionViewImpl> {
    }

    @UiField
    SimplePanel configurationContainer;
    @UiField
    SimplePanel contentPanel;
    @UiField
    ListBox contentListBox;
    @UiField
    Button reloadButton;
    @UiField
    Button loginButton;

    private Presenter presenter;

    @Inject
    public TestbedSelectionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        contentListBox.addItem("Map");
        contentListBox.addItem("Details");
        contentListBox.addItem("Raw WiseML");
    }
    
    @UiHandler("contentListBox")
    public void handleListBoxChange(final ChangeEvent event) {
    	presenter.setContentSelection(contentListBox.getSelectedIndex());
    }

    @UiHandler("reloadButton")
    public void handleReloadClick(final ClickEvent event) {
        presenter.reload();
    }

    @UiHandler("loginButton")
    public void handleLoginClick(final ClickEvent event) {
        presenter.showLoginDialog();
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

	@Override
	public void setContentSelection(Integer index) {
		contentListBox.setSelectedIndex(index);
	}
}