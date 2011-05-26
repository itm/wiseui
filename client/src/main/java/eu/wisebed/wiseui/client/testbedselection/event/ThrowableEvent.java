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
package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ThrowableEvent extends GwtEvent<ThrowableEvent.ThrowableHandler> {

    public interface ThrowableHandler extends EventHandler {

        void onThrowable(ThrowableEvent event);
    }

    public static final Type<ThrowableHandler> TYPE = new Type<ThrowableHandler>();
    private final Throwable throwable;

    public ThrowableEvent(final Throwable throwable) {
        this.throwable = throwable;
    }

    protected void dispatch(final ThrowableHandler handler) {
        handler.onThrowable(this);
    }

    @Override
    public Type<ThrowableHandler> getAssociatedType() {
        return TYPE;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
