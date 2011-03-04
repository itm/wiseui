package eu.wisebed.wiseui.client.administration.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.client.administration.AdministrationActivity;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.AdministrationViewImpl;

public class AdministrationModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(AdministrationView.class).to(AdministrationViewImpl.class).in(Singleton.class);

        bind(AdministrationActivity.class);
    }

}
