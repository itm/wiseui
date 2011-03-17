package eu.wisebed.wiseui.widgets.loading;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface LoadingResources extends ClientBundle {

	@Source("eu/wisebed/wiseui/widgets/loading/loading.gif")
	ImageResource loading();
	
	@Source("eu/wisebed/wiseui/widgets/loading/loading_overlay.png")
	ImageResource loadingOverlay();
}
