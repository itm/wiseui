package eu.wisebed.wiseui.client.administration.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class ConfigurationFormViewImpl extends Composite implements ConfigurationFormView {

	private static ConfigurationFormViewImplUiBinder uiBinder = GWT.create(ConfigurationFormViewImplUiBinder.class);

    interface ConfigurationFormViewImplUiBinder extends UiBinder<Widget, ConfigurationFormViewImpl> {
    }

    @UiField
    TextBox nameTextBox;
    @UiField
    TextBox testbedUrlTextBox;
    @UiField
    TextBox snaaEndpointUrlTextBox;
    @UiField
    TextBox rsEndpointUrlTextBox;
    @UiField
    TextBox sessionManagementEndpointUrlTextBox;
    @UiField
    ListBox isFederatedListBox;
    @UiField
    TextBox urnPrefixTextBox;
    @UiField
    Button addButton;
    @UiField
    ListBox urnPrefixListBox;
    @UiField
    Button removeButton;
    
    private Presenter presenter;
    
    public ConfigurationFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
    
    @UiHandler("addButton")
    public void handleAddButtonClicked(final ClickEvent event) {
    	presenter.add();
    }
    
    @UiHandler("removeButton")
    public void handleRemoveButtonClicked(final ClickEvent event) {
    	presenter.remove();
    }
    
    @UiHandler("isFederatedListBox")
    public void handleFederatedChange(final ChangeEvent event) {
    	presenter.setIsFederated(isFederatedListBox.getValue(isFederatedListBox.getSelectedIndex()));
    }

	@Override
	public HasText getNameHasText() {
		return nameTextBox;
	}

	@Override
	public HasText getTestbedUrlHasText() {
		return testbedUrlTextBox;
	}
	
	@Override
	public HasText getSnaaEndpointUrlHasText() {
		return snaaEndpointUrlTextBox;
	}

	@Override
	public HasText getRsEndpointUrlHasText() {
		return rsEndpointUrlTextBox;
	}

	@Override
	public HasText getSessionManagementEndpointUrlHasText() {
		return sessionManagementEndpointUrlTextBox;
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasText getUrnPrefixHasText() {
		return urnPrefixTextBox;
	}
}
