package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.ReservationDetails;
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
    VerticalPanel experimentContainer;
 
    @UiField
    PushButton refreshExperimentsButton;

    
    public ExperimentationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {
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
	public void renderUserReservations(List<ReservationDetails> reservations){
	}
	
    @UiHandler("refreshExperimentsButton")
    public void handleRefreshClick(final ClickEvent event) {
        presenter.refreshUserExperiments();
    }

}
