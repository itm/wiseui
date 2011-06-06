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
package eu.wisebed.wiseui.client.testbedselection.common;


public class UrnPrefixInfo {

    public enum State {
        NOT_AUTHENTICATED("Not authenticated"),
        AUTHENTICATE("Authenticate..."),
        SUCCESS("Successful"),
        FAILED("Failed due to an error"),
        CANCELED("Canceled"),
        SKIPPED("Skipped");

        private final String value;

        private State(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final String urnPrefix;

    private State state;

    private boolean checked;

    public UrnPrefixInfo(final String urnPrefix) {
        this.urnPrefix = urnPrefix;
        state = State.NOT_AUTHENTICATED;
        checked = true;
    }

    public String getUrnPrefix() {
        return urnPrefix;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;
    }
}
