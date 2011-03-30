package eu.wisebed.wiseui.client.administration.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;


/**
 * View implementation for the Administration area.
 * 
 * @author Malte Legenhausen
 */
@Singleton
public class AdministrationViewImpl extends Composite implements AdministrationView {

    private static AdministrationViewImplUiBinder uiBinder = GWT.create(AdministrationViewImplUiBinder.class);

    interface AdministrationViewImplUiBinder extends UiBinder<Widget, AdministrationViewImpl> {
    }
    
    @UiField
    SimplePanel configurationContainer;
    @UiField
    SimplePanel contentContainer;
    @UiField
    Button newButton;
    @UiField
    Button saveButton;
    @UiField
    Button removeButton;
    @UiField
    Button cancelButton;
    
    private Presenter presenter;

    public AdministrationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {
    	this.presenter = presenter;
    }
    
    @UiHandler("newButton")
    public void handleNewButtonClicked(final ClickEvent event) {
    	presenter.create();
    }
    
    @UiHandler("saveButton")
    public void handleSaveButtonClicked(final ClickEvent event) {
    	presenter.save();
    }
    
    @UiHandler("removeButton") 
    public void handleRemoveButtonClicked(final ClickEvent event) {
    	presenter.remove();
    }
    
    @UiHandler("cancelButton") 
    public void handleCancelButtonClicked(final ClickEvent event) {
    	presenter.cancel();
    }

	@Override
	public AcceptsOneWidget getConfigurationContainer() {
		return configurationContainer;
	}

	@Override
	public AcceptsOneWidget getContentContainer() {
		return contentContainer;
	}

	@Override
	public HasEnabled getCreateHasEnabled() {
		return newButton;
	}

	@Override
	public HasEnabled getSaveHasEnabled() {
		return saveButton;
	}

	@Override
	public HasEnabled getRemoveHasEnabled() {
		return removeButton;
	}

	@Override
	public HasEnabled getCancelHasEnabled() {
		return cancelButton;
	}
}
