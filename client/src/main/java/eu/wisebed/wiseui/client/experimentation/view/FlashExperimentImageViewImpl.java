package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

public class FlashExperimentImageViewImpl extends HasWidgetsDialogBox implements FlashExperimentImageView{
	
	private static FlashExperimentImageViewImplBinder uiBinder = GWT.create(FlashExperimentImageViewImplBinder.class);
	
	@UiTemplate("FlashExperimentImageViewImpl.ui.xml")
	interface FlashExperimentImageViewImplBinder extends UiBinder<Widget, FlashExperimentImageViewImpl> {
	}

	private Presenter presenter;
	
    @UiField
    Button flashButton;
    
    @UiField
    Button cancelButton;
    
//   @UiField
//   CaptionPanel uploadedImagesContainer;
//    
//   @UiField
//   CaptionPanel imageUploadContainer;
//    
//   @UiField
//   ImageUploadWidget imageUploadWidget;
        
	public FlashExperimentImageViewImpl(){
        uiBinder.createAndBindUi(this);
        
        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
	}
	
    @UiFactory
    protected FlashExperimentImageViewImpl createDialog() {
        return this;
    }
		
	@UiHandler("flashButton")
	public void handleFlashButtonClick(final ClickEvent event) {
		presenter.submit();
	}
	
	@UiHandler("cancelButton")
	public void handleCancelButtonClick(final ClickEvent event) {
		presenter.cancel();
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
}
