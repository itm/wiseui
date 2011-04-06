package eu.wisebed.wiseui.client.testbedlist.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

import java.util.List;

@ImplementedBy(TestbedListViewImpl.class)
public interface TestbedListView extends IsWidget {

    void setPresenter(Presenter presenter);

    void setTestbedConfigurationSelectionModel(SelectionModel<TestbedConfiguration> selectionModel);

    void setConfigurations(List<TestbedConfiguration> configurations);

    public interface Presenter {


    }
}
