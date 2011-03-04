package eu.wisebed.wiseui.client.util;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UtilClientBundle extends ClientBundle {

    @Source("eu/wisebed/wiseui/client/util/success.png")
    ImageResource getSuccessImageResource();

    @Source("eu/wisebed/wiseui/client/util/warning.png")
    ImageResource getWarningImageResource();

    @Source("eu/wisebed/wiseui/client/util/error.png")
    ImageResource getErrorImageResource();
}
