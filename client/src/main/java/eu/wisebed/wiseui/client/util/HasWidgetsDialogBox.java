package eu.wisebed.wiseui.client.util;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class HasWidgetsDialogBox extends DialogBox implements HasWidgets {

    @Override
    public void add(final Widget w) {
        final Widget widget = getWidget();
        if (widget != null && widget instanceof HasWidgets) {
            ((HasWidgets) getWidget()).add(w);
        } else {
            super.add(w);
        }
    }
}
