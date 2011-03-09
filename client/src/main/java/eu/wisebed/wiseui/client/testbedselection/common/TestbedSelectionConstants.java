package eu.wisebed.wiseui.client.testbedselection.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * Constants for the testbed-selection-view and -place.
 *
 * @author Sönke Nommensen
 */
public interface TestbedSelectionConstants extends Constants {
    public static final TestbedSelectionConstants INSTANCE = GWT.create(TestbedSelectionConstants.class);

    @DefaultStringValue("selection")
    String testbedSelectionString();

    @DefaultStringValue("view")
    String testbedViewString();

    @DefaultStringValue("map")
    String mapView();

    @DefaultStringValue("detail")
    String detailView();

    @DefaultStringValue("raw")
    String rawWisemlView();
}
