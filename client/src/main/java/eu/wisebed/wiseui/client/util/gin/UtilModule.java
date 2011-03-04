package eu.wisebed.wiseui.client.util.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.client.util.MessageBox;
import eu.wisebed.wiseui.client.util.view.MessageBoxView;
import eu.wisebed.wiseui.client.util.view.MessageBoxViewImpl;

public class UtilModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(MessageBoxView.class).to(MessageBoxViewImpl.class).in(Singleton.class);
        bind(MessageBox.class);
    }

}
