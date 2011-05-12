package eu.wisebed.wiseui.widgets;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class ImageUploadWidget extends Composite implements ImageUpload{

	@UiTemplate("ImageUploadWidget.ui.xml")
	interface ImageUploadImplUiBinder extends UiBinder<Widget,
		ImageUploadWidget>{}

	@UiField MultiUploader imagePicker = new MultiUploader();
	
	private static ImageUploadImplUiBinder uiBinder = 
		GWT.create(ImageUploadImplUiBinder.class);
	
	private Presenter presenter;
	private String imageFileName;
	private String imageFileNameField;

	public ImageUploadWidget(){
		initWidget(uiBinder.createAndBindUi(this));
        this.imagePicker.addOnFinishUploadHandler(onFinishUploaderHandler);
	}

	/**
	 * Catch response from server after finishing image upload. The server can 
	 * send information to the client about the image sent from client. Parse 
	 * this information using XML or JSON libraries. Also keeps image
	 * information for the filename and the filename's field in variables so as
	 * later to be transfered within ReservationDetails object.
	 */
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = 
		new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {	        
				Document doc = XMLParser.parse(uploader.getServerResponse());
				String field = Utils.getXmlNodeValue(doc, "file-1-field");
				String name = Utils.getXmlNodeValue(doc, "file-1-name");
				setImageFileName(name);
				setImageFileNameField(field);
			}
		}
	};

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	public void setImageFileNameField(String imageFileNameField) {
		  this.imageFileNameField = imageFileNameField;
	}
	
	public String getImageFileName() {
		return this.imageFileName;
	}

	public String getImageFileNameField() {
		return this.imageFileNameField;
	}
	/**
	 * Check if user selected an image to be flashed
	 */
	public boolean checkImageSelected() {
        return this.imagePicker.getStatus() == Status.DONE;
    }

	public void setListener(Presenter listener) {
		this.presenter = listener;
	}

	public Presenter getListener() {
		return this.presenter;
	}
}
