package eu.wisebed.wiseui.client.experimentation.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.experimentation.ExperimentationActivity;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentViewImpl;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationViewImpl;

public class ExperimentationModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(ExperimentationView.class).to(ExperimentationViewImpl.class).in(Singleton.class);
        bind(ExperimentView.class).to(ExperimentViewImpl.class);
        
        bind(ExperimentationPresenter.class);
        bind(ExperimentPresenter.class);
        
        bind(ExperimentationActivity.class);
    }

}
