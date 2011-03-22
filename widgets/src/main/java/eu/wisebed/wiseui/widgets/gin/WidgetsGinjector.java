package eu.wisebed.wiseui.widgets.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

@GinModules({WidgetsModule.class})
public interface WidgetsGinjector extends Ginjector {

    MessageBox getMessageBox();
}
