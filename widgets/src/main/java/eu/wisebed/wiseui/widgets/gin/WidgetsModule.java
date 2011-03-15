package eu.wisebed.wiseui.widgets.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanel;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelViewImpl;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBoxView;
import eu.wisebed.wiseui.widgets.messagebox.MessageBoxViewImpl;

public class WidgetsModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(MessageBoxView.class).to(MessageBoxViewImpl.class).in(Singleton.class);
        bind(MessageBox.class);
        bind(ExperimentPanelView.class).to(ExperimentPanelViewImpl.class);
        bind(ExperimentPanel.class);
    }

}
