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
package eu.wisebed.wiseui.client.reservation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.presenter.PublicReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationEditPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.NodeSelectionPresenter;
import eu.wisebed.wiseui.client.reservation.view.NodeSelectionView;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;

public interface ReservationGinjector extends Ginjector {

    ReservationActivity getReservationActivity();

    ReservationView getReservationView();
        
    PublicReservationsView getPublicReservationsView();
    
    PublicReservationsPresenter getPublicReservationsPresenter();
    
    ReservationEditView getReservationEditView();
    
    ReservationEditPresenter getReservationEditPresenter();

    NodeSelectionView getNodeSelectionView();

    NodeSelectionPresenter getNodeSelectionPresenter();
}
