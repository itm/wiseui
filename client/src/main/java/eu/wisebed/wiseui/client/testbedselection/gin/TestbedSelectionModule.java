package eu.wisebed.wiseui.client.testbedselection.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;
import eu.wisebed.wiseui.client.testbedselection.presenter.ConfigurationPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.DetailPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.MapPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.RawWisemlPresenter;
import eu.wisebed.wiseui.client.testbedselection.view.ConfigurationView;
import eu.wisebed.wiseui.client.testbedselection.view.ConfigurationViewImpl;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.DetailViewImpl;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogViewImpl;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.testbedselection.view.MapViewImpl;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlViewImpl;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionViewImpl;


public class TestbedSelectionModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(TestbedSelectionView.class).to(TestbedSelectionViewImpl.class).in(Singleton.class);
        bind(ConfigurationView.class).to(ConfigurationViewImpl.class).in(Singleton.class);
        bind(DetailView.class).to(DetailViewImpl.class).in(Singleton.class);
        bind(LoginDialogView.class).to(LoginDialogViewImpl.class).in(Singleton.class);
        bind(MapView.class).to(MapViewImpl.class).in(Singleton.class);
        bind(RawWisemlView.class).to(RawWisemlViewImpl.class).in(Singleton.class);

        bind(ConfigurationPresenter.class);
        bind(DetailPresenter.class);
        bind(LoginDialogPresenter.class);
        bind(MapPresenter.class);
        bind(RawWisemlPresenter.class);

        bind(TestbedSelectionActivity.class);
    }
}
