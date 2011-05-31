package eu.wisebed.wiseui.client.experimentation.view;


import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(FlashExperimentImageViewImpl.class)
public interface FlashExperimentImageView extends IsWidget {

	void show(String title);
	
	void hide();
	
	void setPresenter(Presenter presenter);
	
	public interface Presenter {
		void submit();
		
		void cancel();

	} 
}
