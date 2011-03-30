package eu.wisebed.wiseui.client.testbedselection.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.TestbedConfiguration;

@ImplementedBy(ConfigurationViewImpl.class)
public interface ConfigurationView extends IsWidget {

    void setPresenter(Presenter presenter);

    void setTestbedConfigurationSelectionModel(SelectionModel<TestbedConfiguration> selectionModel);

    void setConfigurations(List<TestbedConfiguration> configurations);

    public interface Presenter {

    	
    }
}
