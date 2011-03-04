package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.wiseml.Coordinate;

public interface DetailView extends IsWidget {

    void setPresenter(Presenter presenter);

    void setDescriptionCoordinate(Coordinate coordinate);

    HasText getDescription();

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);
    }
}