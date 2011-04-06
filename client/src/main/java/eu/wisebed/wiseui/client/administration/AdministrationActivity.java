package eu.wisebed.wiseui.client.administration;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.administration.presenter.AdministrationPresenter;
import eu.wisebed.wiseui.client.administration.presenter.ConfigurationFormPresenter;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.ConfigurationFormView;

/**
 * The activity for the administration part of WiseUi.
 * 
 * @author Malte Legenhausen
 */
public class AdministrationActivity extends AbstractActivity {

	private final WiseUiGinjector injector;
	
	private AdministrationPlace place;
	
    @Inject
    public AdministrationActivity(final WiseUiGinjector injector) {
        this.injector = injector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    	initTestbedSelectionPart(panel);
    }

	private void initTestbedSelectionPart(final AcceptsOneWidget container) {
        GWT.log("Init Administration Part");
        final AdministrationPresenter testbedSelectionPresenter = injector.getAdministrationPresenter();
        testbedSelectionPresenter.setPlace(place);
        final AdministrationView testbedSelectionView = injector.getAdministrationView();
        testbedSelectionView.setPresenter(testbedSelectionPresenter);
        initContentPart(testbedSelectionView);
        container.setWidget(testbedSelectionView.asWidget());
    }
    
    private void initContentPart(final AdministrationView testbedSelectionView) {
		GWT.log("Init Administration Content Part");
		final ConfigurationFormPresenter configurationFormPresenter = injector.getConfigurationFormPresenter();
		configurationFormPresenter.setPlace(place);
		final ConfigurationFormView configurationFormView = injector.getConfigurationFormView();
		configurationFormView.setPresenter(configurationFormPresenter);
		testbedSelectionView.getContentContainer().setWidget(configurationFormView);
	}

	public void setPlace(final AdministrationPlace place) {
		this.place = place;
	}
}
