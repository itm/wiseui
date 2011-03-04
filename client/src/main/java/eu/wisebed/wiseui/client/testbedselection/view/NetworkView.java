package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.IsWidget;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.wiseml.Node;

import java.util.List;

public interface NetworkView extends IsWidget {

    void setPresenter(Presenter presenter);

    void setNodes(final List<Node> nodes);

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }
}
