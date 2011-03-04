package eu.wisebed.wiseui.client.testbedselection.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;
import eu.wisebed.wiseui.client.testbedselection.presenter.ConfigurationPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.DetailPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.NetworkPresenter;
import eu.wisebed.wiseui.client.testbedselection.view.*;

public class TestbedSelectionModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(TestbedSelectionView.class).to(TestbedSelectionViewImpl.class).in(Singleton.class);
        bind(ConfigurationView.class).to(ConfigurationViewImpl.class).in(Singleton.class);
        bind(DetailView.class).to(DetailViewImpl.class).in(Singleton.class);
        bind(NetworkView.class).to(NetworkViewImpl.class).in(Singleton.class);
        bind(LoginDialogView.class).to(LoginDialogViewImpl.class).in(Singleton.class);

        bind(ConfigurationPresenter.class);
        bind(NetworkPresenter.class);
        bind(DetailPresenter.class);
        bind(LoginDialogPresenter.class);

        bind(TestbedSelectionActivity.class);
    }

}
