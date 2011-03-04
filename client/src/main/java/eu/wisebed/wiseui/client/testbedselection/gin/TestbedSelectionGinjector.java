package eu.wisebed.wiseui.client.testbedselection.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;
import eu.wisebed.wiseui.client.testbedselection.presenter.ConfigurationPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.DetailPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.NetworkPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.TestbedSelectionPresenter;
import eu.wisebed.wiseui.client.testbedselection.view.ConfigurationView;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedselection.view.NetworkView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;

public interface TestbedSelectionGinjector extends Ginjector {

    TestbedSelectionActivity getTestbedSelectionActivity();

    ConfigurationPresenter getConfigurationPresenter();

    TestbedSelectionPresenter getTestbedSelectionPresenter();

    DetailPresenter getDetailPresenter();

    NetworkPresenter getNetworkPresenter();

    LoginDialogPresenter getLoginDialogPresenter();

    TestbedSelectionView getTestbedSelectionView();

    ConfigurationView getConfigurationView();

    DetailView getDetailView();

    NetworkView getNetworkView();

    LoginDialogView getLoginDialogView();
}
