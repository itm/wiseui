package eu.wisebed.wiseui.client.reservation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.presenter.PublicReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.NewReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationEditPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.NodeSelectionPresenter;
import eu.wisebed.wiseui.client.reservation.view.NodeSelectionView;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.NewReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;

public interface ReservationGinjector extends Ginjector {

    ReservationActivity getReservationActivity();
    
    ReservationPresenter getReservationPresenter();

    ReservationView getReservationView();
    
    NewReservationPresenter getNewReservationPresenter();

    NewReservationView getNewReservationView();
    
    PublicReservationsView getPublicReservationsView();
    
    PublicReservationsPresenter getPublicReservationsPresenter();
    
    ReservationEditView getReservationEditView();
    
    ReservationEditPresenter getReservationEditPresenter();

    NodeSelectionView getNodeSelectionView();

    NodeSelectionPresenter getNodeSelectionPresenter();
}
