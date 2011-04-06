package eu.wisebed.wiseui.client.testbedselection.gin;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.testbedlist.TestbedListActivity;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;
import eu.wisebed.wiseui.client.testbedselection.presenter.DetailPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.MapPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.RawWisemlPresenter;
import eu.wisebed.wiseui.client.testbedselection.presenter.TestbedSelectionPresenter;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;

public interface TestbedSelectionGinjector extends Ginjector {

    TestbedSelectionActivity getTestbedSelectionActivity();

    TestbedListActivity getConfigurationPresenter();

    TestbedSelectionPresenter getTestbedSelectionPresenter();

    DetailPresenter getDetailPresenter();

    MapPresenter getMapPresenter();

    LoginDialogPresenter getLoginDialogPresenter();

    TestbedSelectionView getTestbedSelectionView();

    TestbedListView getTestbedListView();

    DetailView getDetailView();

    MapView getMapView();

    LoginDialogView getLoginDialogView();

    RawWisemlPresenter getRawWisemlPresenter();

    RawWisemlView getRawWisemlView();
}
