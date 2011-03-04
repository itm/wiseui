package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

import java.util.List;

public interface ConfigurationView extends IsWidget {

    void setPresenter(Presenter presenter);

    void setTestbedConfigurationSelectionModel(SelectionModel<TestbedConfiguration> selectionModel);

    void setConfigurations(List<TestbedConfiguration> configurations);

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }
}
