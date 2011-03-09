package eu.wisebed.wiseui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Sönke Nommensen
 */
public interface WiseUiResources extends ClientBundle {
    public static final WiseUiResources INSTANCE = GWT.create(WiseUiResources.class);

//    @Source("WiseUi.css")
//    public CssResource css();

    @Source("testbed-configurations.xml")
    public TextResource testbedConfiguration();
}
