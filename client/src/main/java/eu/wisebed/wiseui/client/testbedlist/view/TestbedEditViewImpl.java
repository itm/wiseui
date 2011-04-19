package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Singleton;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.actions.FocusAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.description.PopupDescription;
import eu.maydu.gwt.validation.client.description.PopupDescription.Location;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;
import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

@Singleton
public class TestbedEditViewImpl extends HasWidgetsDialogBox implements TestbedEditView {

    private static TestbedEditViewImplUiBinder uiBinder = GWT.create(TestbedEditViewImplUiBinder.class);

    interface TestbedEditViewImplUiBinder extends UiBinder<Widget, TestbedEditViewImpl> {
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
    CellList<String> urnPrefixList;
    @UiField
    Button removeButton;
    @UiField
    Button submitButton;
    @UiField
    Button cancelButton;
    
    private Presenter presenter;
    
    private final ValidationMessages messages = new TestbedEditValidationMessages();
    private final PopupDescription popupDescription = new PopupDescription(messages, Location.BOTTOM);
    private final ValidationProcessor validator = new DefaultValidationProcessor(messages);

    public TestbedEditViewImpl() {
		uiBinder.createAndBindUi(this);
		
		final FocusAction focusAction = new FocusAction();		
		validator.addValidators("name", new StringLengthValidator(nameTextBox, 3, Integer.MAX_VALUE)
			.addActionForFailure(focusAction)
			.addActionForFailure(new StyleAction("validationFailedBorder"))
		);
		popupDescription.addDescription("nameDescription", nameTextBox);
		
		validator.addValidators("testbedUrl", new StringLengthValidator(testbedUrlTextBox, 3, Integer.MAX_VALUE)
			.addActionForFailure(focusAction)
			.addActionForFailure(new StyleAction("validationFailedBorder"))
		);
		popupDescription.addDescription("testbedUrlDescription", testbedUrlTextBox);
		
		validator.addValidators("snaaEndpointUrl", new StringLengthValidator(snaaEndpointUrlTextBox, 3, Integer.MAX_VALUE)
			.addActionForFailure(focusAction)
			.addActionForFailure(new StyleAction("validationFailedBorder"))
		);
		popupDescription.addDescription("snaaEndpointUrlDescription", snaaEndpointUrlTextBox);
		
        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
	}
    
    @UiFactory
    protected TestbedEditViewImpl createDialog() {
        return this;
    }
    
    @UiFactory
    protected CellList<String> createCellList() {
    	return new CellList<String>(new TextCell());
    }
    
    @UiHandler("submitButton")
    public void onSubmit(final ClickEvent event) {
    	presenter.submit();
    }
    
    @UiHandler("cancelButton")
    public void onCancel(final ClickEvent event) {
    	presenter.cancel();
    }
    
    @UiHandler("addButton")
    public void handleAddButtonClicked(final ClickEvent event) {
    	presenter.add();
    }
    
    @UiHandler("removeButton")
    public void handleRemoveButtonClicked(final ClickEvent event) {
    	presenter.remove();
    }

	@Override
	public void show(final String title) {
		setText(title);
		center();
		show();
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
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
	public HasText getUrnPrefixHasText() {
		return urnPrefixTextBox;
	}

	@Override
	public HasData<String> getUrnPrefixHasData() {
		return urnPrefixList;
	}

	@Override
	public void setUrnPrefixSelectionModel(final SelectionModel<String> selectionModel) {
		urnPrefixList.setSelectionModel(selectionModel);
	}

	@Override
	public void setFederatedItems(final List<String> items) {
		isFederatedListBox.clear();
		for (final String item : items) {
			isFederatedListBox.addItem(item);
		}
	}

	@Override
	public void setFederatedSelectedIndex(final int index) {
		isFederatedListBox.setSelectedIndex(index);
	}

	@Override
	public int getFederatedSelectedIndex() {
		return isFederatedListBox.getSelectedIndex();
	}

	@Override
	public HasEnabled getUrnPrefixRemoveHasEnabled() {
		return removeButton;
	}

	@Override
	public boolean validate() {
		return validator.validate();
	}
}
