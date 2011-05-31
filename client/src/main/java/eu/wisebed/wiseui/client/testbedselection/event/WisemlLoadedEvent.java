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
package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.shared.dto.Wiseml;

public class WisemlLoadedEvent extends GwtEvent<WisemlLoadedHandler> {

    public interface WisemlLoadedHandler extends EventHandler {
        void onWisemlLoaded(WisemlLoadedEvent event);
    }

    public static final Type<WisemlLoadedHandler> TYPE = new Type<WisemlLoadedHandler>();

    private final Wiseml wiseml;

    public WisemlLoadedEvent(final Wiseml wiseml) {
        this.wiseml = wiseml;
    }

    @Override
    public Type<WisemlLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final WisemlLoadedHandler handler) {
        handler.onWisemlLoaded(this);
    }

    public Wiseml getWiseml() {
        return wiseml;
    }
}
