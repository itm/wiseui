/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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

import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.navigation.NavigationActivity;

public class NavigationActivityMapper implements ActivityMapper {

    private final NavigationActivity activity;

    @Inject
    public NavigationActivityMapper(final NavigationActivity activity) {
        this.activity = activity;
    }

    public Activity getActivity(final Place place) {
        activity.setPlace((WiseUiPlace) place);
        return activity;
    }
}
