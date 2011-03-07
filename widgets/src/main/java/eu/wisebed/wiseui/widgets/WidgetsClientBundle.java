package eu.wisebed.wiseui.widgets;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WidgetsClientBundle extends ClientBundle {

    @Source("eu/wisebed/wiseui/widgets/messagebox/success.png")
    ImageResource getSuccessImageResource();

    @Source("eu/wisebed/wiseui/widgets/messagebox/warning.png")
    ImageResource getWarningImageResource();

    @Source("eu/wisebed/wiseui/widgets/messagebox/error.png")
    ImageResource getErrorImageResource();
}
