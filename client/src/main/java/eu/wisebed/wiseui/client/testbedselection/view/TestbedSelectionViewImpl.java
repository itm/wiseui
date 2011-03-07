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
import com.google.gwt.user.client.ui.DeckPanel;
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
    DeckPanel contentDeckPanel;
    @UiField
    ListBox contentListBox;
    @UiField
    Button reloadButton;
    @UiField
    Button loginButton;
    private final SimplePanel rawWisemlContainer = new SimplePanel();
    private final SimplePanel mapContainer = new SimplePanel();
    private final SimplePanel detailContainer = new SimplePanel();
    private Presenter presenter;

    @Inject
    public TestbedSelectionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        contentDeckPanel.add(mapContainer);
        contentListBox.addItem("Map");

        contentDeckPanel.add(detailContainer);
        contentListBox.addItem("Details");

        contentDeckPanel.add(rawWisemlContainer);
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
    public AcceptsOneWidget getRawWisemlContainer() {
        return rawWisemlContainer;
    }

    @Override
    public AcceptsOneWidget getDetailContainer() {
        return detailContainer;
    }

    @Override
    public AcceptsOneWidget getMapContainer() {
        return mapContainer;
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
    public void setContentSelection(final Integer index) {
        contentDeckPanel.showWidget(index);
    }
}
