package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@Singleton
public class ExperimentationViewImpl extends Composite implements
        ExperimentationView {

    private static ExperimentationViewImplUiBinder uiBinder = GWT
            .create(ExperimentationViewImplUiBinder.class);

    interface ExperimentationViewImplUiBinder extends
            UiBinder<Widget, ExperimentationViewImpl> {
    }
    
    private Presenter presenter;
        
    @UiField
    CaptionPanel container;
    @UiField
    SimplePanel experimentContainer;
    
    public ExperimentationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {
        GWT.log("setPresenter( " + presenter.toString() + " )");
    	this.presenter = presenter;
    }

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public HasLoadingIndicator getLoadingIndicator() {
		return container;
	}

	@Override
	public void renderUserReservations() {
		container.showLoading("Loading Experiments");		
		container.hideLoading();
	}
}
