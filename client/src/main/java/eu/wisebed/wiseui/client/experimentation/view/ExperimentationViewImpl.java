package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ExperimentationViewImpl extends Composite implements
        ExperimentationView {

    private static ExperimentationViewImplUiBinder uiBinder = GWT
            .create(ExperimentationViewImplUiBinder.class);

    interface ExperimentationViewImplUiBinder extends
            UiBinder<Widget, ExperimentationViewImpl> {
    }

    public ExperimentationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {

    }
}
