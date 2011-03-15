package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView;

public class ExperimentationViewImpl extends Composite implements
        ExperimentationView {

    private static ExperimentationViewImplUiBinder uiBinder = GWT
            .create(ExperimentationViewImplUiBinder.class);

    interface ExperimentationViewImplUiBinder extends
            UiBinder<Widget, ExperimentationViewImpl> {
    }
    
    private Presenter presenter;
        
    @UiField
    FlowPanel experimentContainer;
    
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
	public void initView(List<ExperimentPanelView> panels) {
		for(ExperimentPanelView panel : panels){
			experimentContainer.add(panel);
		}
	}
}
