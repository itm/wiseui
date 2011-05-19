/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;

public class ReservationPresenter implements Presenter{

    private WiseUiPlace place;
    private ReservationServiceAsync reservationService;
    private EventBus eventBus;

    @Inject
    public ReservationPresenter(final ReservationServiceAsync reservationService,
                                final EventBus eventBus) {
        this.reservationService = reservationService;
        this.eventBus = eventBus;
    }

    public void setPlace(final WiseUiPlace place) {
        this.place = place;
    }

}