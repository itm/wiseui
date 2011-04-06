package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.testbedlist.TestbedListActivity;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionConstants;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
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
import eu.wisebed.wiseui.client.util.DefaultsHelper;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.Wiseml;


/**
 * Activity for the testbed selection part of the wiseui.
 * 
 * @author Malte Legenhausen
 */
public class TestbedSelectionActivity extends AbstractActivity implements ConfigurationSelectedHandler {

    private final SessionManagementServiceAsync sessionManagementService;

    private TestbedSelectionPlace place;

    private WiseUiGinjector injector;

    private EventBus eventBus;

    @Inject
    public TestbedSelectionActivity(final WiseUiGinjector injector,
                                    final SessionManagementServiceAsync sessionManagementService) {
        this.injector = injector;
        this.sessionManagementService = sessionManagementService;
    }

    public void setPlace(final TestbedSelectionPlace place) {
        this.place = place;
    }

    /**
     * Invoked by the ActivityManager to start a new {@link com.google.gwt.activity.shared.Activity}.
     */
    public void start(final AcceptsOneWidget containerWidget, final EventBus eventBus) {
        this.eventBus = eventBus;
        initTestbedSelectionPart(containerWidget);
        initLoginDialogPart();
        bind();
    }

    private void bind() {
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
    }

    private void initTestbedSelectionPart(final AcceptsOneWidget container) {
        GWT.log("Init Testbed Selection Part");
        final TestbedSelectionPresenter testbedSelectionPresenter = injector.getTestbedSelectionPresenter();
        testbedSelectionPresenter.setPlace(place);
        final TestbedSelectionView testbedSelectionView = injector.getTestbedSelectionView();
        testbedSelectionView.setPresenter(testbedSelectionPresenter);
        initConfigurationPart(testbedSelectionView);
        initContentPart(testbedSelectionView, place.getView());
        container.setWidget(testbedSelectionView.asWidget());
    }
    
    private void initContentPart(final TestbedSelectionView testbedSelectionView, final String view) {
        if (TestbedSelectionConstants.MAP_VIEW.equals(view)) {
            initMapPart(testbedSelectionView);
        }
        else if (TestbedSelectionConstants.DETAIL_VIEW.equals(view)) {
            initDetailPart(testbedSelectionView);
        }
        else if (TestbedSelectionConstants.RAW_WISEML_VIEW.equals(view)) {
            initRawWisemlPart(testbedSelectionView);
        }
        else {
            initMapPart(testbedSelectionView);
        }
    }

    private void initConfigurationPart(final TestbedSelectionView testbedSelectionView) {
        GWT.log("Init Testbed Configuration Part");
        final TestbedListActivity testbedListActivity = injector.getConfigurationPresenter();
        testbedListActivity.setPlace(place);
        final TestbedListView testbedListView = injector.getTestbedListView();
        testbedListView.setPresenter(testbedListActivity);
        testbedSelectionView.getConfigurationContainer().setWidget(testbedListView);
    }

    private void initDetailPart(final TestbedSelectionView testbedSelectionView) {
        GWT.log("Init Testbed Detail Part");
        final DetailPresenter detailPresenter = injector.getDetailPresenter();
        detailPresenter.setPlace(place);
        final DetailView detailView = injector.getDetailView();
        detailView.setPresenter(detailPresenter);
        testbedSelectionView.getContentContainer().setWidget(detailView);
    }

    private void initMapPart(final TestbedSelectionView testbedSelectionView) {
        GWT.log("Init Testbed Map Part");
        final MapPresenter mapPresenter = injector.getMapPresenter();
        mapPresenter.setPlace(place);
        final MapView mapView = injector.getMapView();
        mapView.setPresenter(mapPresenter);
        testbedSelectionView.getContentContainer().setWidget(mapView);
    }
    
    private void initRawWisemlPart(final TestbedSelectionView testbedSelectionView) {
    	GWT.log("Init Testbed Raw WiseML Part");
    	final RawWisemlPresenter rawWisemlPresenter = injector.getRawWisemlPresenter();
    	final RawWisemlView rawWisemlView = injector.getRawWisemlView();
    	rawWisemlView.setPresenter(rawWisemlPresenter);
    	testbedSelectionView.getContentContainer().setWidget(rawWisemlView);
    }

    private void initLoginDialogPart() {
        GWT.log("Init Login Dialog Part");
        final LoginDialogPresenter loginDialogPresenter = injector.getLoginDialogPresenter();
        loginDialogPresenter.setPlace(place);
        final LoginDialogView loginDialogView = injector.getLoginDialogView();
        loginDialogView.setPresenter(loginDialogPresenter);
    }

    public void onTestbedConfigurationSelected(final TestbedSelectedEvent event) {
        final TestbedConfiguration configuration = event.getConfiguration();
        final AsyncCallback<Wiseml> callback = new AsyncCallback<Wiseml>() {

            public void onSuccess(final Wiseml result) {
            	if (result != null) {
            		result.setSetup(DefaultsHelper.apply(result.getSetup()));
            	}
                eventBus.fireEvent(new WisemlLoadedEvent(result));
            }

            public void onFailure(final Throwable caught) {
                eventBus.fireEvent(new ThrowableEvent(caught));
            }
        };
        final String url = configuration.getSessionmanagementEndpointUrl().trim();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                sessionManagementService.getWiseml(url, callback);
            }
        });
    }
}
