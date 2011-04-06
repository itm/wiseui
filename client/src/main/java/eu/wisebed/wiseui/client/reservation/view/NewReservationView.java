package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(NewReservationViewImpl.class)
public interface NewReservationView extends IsWidget {

    void setPresenter(Presenter presenter);


    public interface Presenter {}
}
