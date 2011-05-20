package eu.wisebed.wiseui.client.experimentation.gin;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.experimentation.ExperimentationActivity;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;

public interface ExperimentationGinjector extends Ginjector {

    ExperimentationView getExperimentationView();

    ExperimentationActivity getExperimentationActivity();
    
    ExperimentationPresenter getExperimentationPresenter();
    
    ExperimentPresenter getExperimentPresenter();
    
    ExperimentView getExperimentView();
 }
