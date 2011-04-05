package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ImplementedBy(NewReservationViewImpl.class)
public interface NewReservationView extends IsWidget {

    void setPresenter(Presenter presenter);

    void renderNodes(List<Node> nodeList);

    void enableNewReservationView();

    boolean checkReservationDetails();

    ReservationDetails getReservationDetails();

    Date getDateValue();

    Integer[] getStartTime();

    Integer[] getStopTime();

    ArrayList<Node> getNodesSelected();

    String getImageFileName();

    String getImageFileNameField();


    public interface Presenter {
        boolean checkReservationDetails();
    }
}
