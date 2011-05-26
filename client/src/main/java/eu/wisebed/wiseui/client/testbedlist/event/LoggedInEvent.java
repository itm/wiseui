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
package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.client.testbedlist.event.LoggedInEvent.LoggedInHandler;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;

public class LoggedInEvent extends GwtEvent<LoggedInHandler> {

    public interface LoggedInHandler extends EventHandler {
        void onLoggedIn(LoggedInEvent event);
    }

    public static final Type<LoggedInHandler> TYPE = new Type<LoggedInHandler>();

    private final SecretAuthenticationKey secretAuthenticationKey;

    public LoggedInEvent(final SecretAuthenticationKey secretAuthenticationKey) {
        this.secretAuthenticationKey = secretAuthenticationKey;
    }

    @Override
    public Type<LoggedInHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final LoggedInHandler handler) {
        handler.onLoggedIn(this);
    }

    public SecretAuthenticationKey getSecretAuthenticationKey() {
        return secretAuthenticationKey;
    }
}
