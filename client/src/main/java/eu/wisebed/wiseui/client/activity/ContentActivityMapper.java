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
package eu.wisebed.wiseui.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.navigation.NavigationPlace;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionActivity;

public class ContentActivityMapper implements ActivityMapper {

    private WiseUiGinjector injector;

    /**
     * AppActivityMapper associates each Place with its corresponding
     * {@link Activity}
     *
     * @param injector GIN injector to be passed to activities
     */
    @Inject
    public ContentActivityMapper(final WiseUiGinjector injector) {
        super();
        this.injector = injector;
    }

    /**
     * Map each Place to its corresponding Activity.
     */
    public Activity getActivity(final Place place) {
    	final WiseUiPlace wiseUiPlace = (WiseUiPlace) place;
    	final NavigationPlace navigationPlace = (NavigationPlace) wiseUiPlace.get(NavigationPlace.class);
    	
    	final Integer index = navigationPlace.getIndex();
        Activity mappedActivity = null;
        if (index.equals(0)) {
            final TestbedSelectionActivity activity = injector.getTestbedSelectionActivity();
            activity.setPlace(wiseUiPlace);
            mappedActivity = activity;
        } else if (index.equals(1)) {
        	final ReservationActivity activity = injector.getReservationActivity();
        	activity.setPlace(wiseUiPlace);
            mappedActivity = activity;
        } else if (index.equals(2)) {
            mappedActivity = injector.getExperimentationActivity();
        }
        return mappedActivity;
    }
}
