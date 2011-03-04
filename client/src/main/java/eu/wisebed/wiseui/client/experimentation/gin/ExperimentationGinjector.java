package eu.wisebed.wiseui.client.experimentation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.experimentation.ExperimentationActivity;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;

public interface ExperimentationGinjector extends Ginjector {

    ExperimentationView getExperimentationView();

    ExperimentationActivity getExperimentationActivity();
}
