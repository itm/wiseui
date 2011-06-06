/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.presenter.NodeSelectionPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.PublicReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationEditPresenter;
import eu.wisebed.wiseui.client.reservation.view.NodeSelectionView;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 */
public class ReservationActivity extends AbstractActivity implements ReservationView.Presenter {

    private WiseUiGinjector injector;
    private WiseUiPlace place;

    @Inject
    public ReservationActivity(final WiseUiGinjector injector) {
        this.injector = injector;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        initReservationPanel(panel);
    }

    private void initReservationPanel(final AcceptsOneWidget panel) {
        GWT.log("Init Reservation Panel");

        final ReservationView reservationView = injector.getReservationView();

        setPlace(place);
        reservationView.setPresenter(this);
        panel.setWidget(reservationView.asWidget());

        initNodeSelectionPanel(reservationView);

        initPublicReservationsPanel(reservationView);

        initReservationEdit();
    }

    private void initNodeSelectionPanel(final ReservationView reservationView) {
        GWT.log("Init Node Selection Panel");
        final NodeSelectionPresenter nodeSelectionPresenter = injector.getNodeSelectionPresenter();
        final NodeSelectionView nodeSelectionView = injector.getNodeSelectionView();
        nodeSelectionView.setPresenter(nodeSelectionPresenter);
        reservationView.getNodeSelectionPanel().setWidget(nodeSelectionView.asWidget());
    }

    private void initPublicReservationsPanel(final ReservationView reservationView) {
        GWT.log("Init Public Reservation Panel");
        final PublicReservationsPresenter publicReservationsPresenter = injector.getPublicReservationsPresenter();
        final PublicReservationsView publicReservationsView = injector.getPublicReservationsView();
        publicReservationsView.setPresenter(publicReservationsPresenter);
        reservationView.getReservationPanel().setWidget(publicReservationsView.asWidget());
    }
    
    private void initReservationEdit(){
    	GWT.log("Init Edit Reservation Panel");
    	final ReservationEditPresenter reservationEditPresenter = injector.getReservationEditPresenter();
    	final ReservationEditView reservationEditView = injector.getReservationEditView();
    	reservationEditView.setPresenter(reservationEditPresenter);
    }

    public void setPlace(final WiseUiPlace place) {
        this.place = place;
    }
}
