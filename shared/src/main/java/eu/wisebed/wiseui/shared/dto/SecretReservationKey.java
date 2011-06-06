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
package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

public class SecretReservationKey implements Serializable {

    private static final long serialVersionUID = -1825088754599715036L;

    private String urnPrefix;

    private String secretReservationKey;

    public SecretReservationKey() {

    }

    public SecretReservationKey(String urnPrefix, String secretReservationKey) {
        this.urnPrefix = urnPrefix;
        this.secretReservationKey = secretReservationKey;
    }

    public void setUrnPrefix(final String urnPrefix) {
        this.urnPrefix = urnPrefix;
    }

    public String getUrnPrefix() {
        return urnPrefix;
    }

    public void setSecretReservationKey(final String secretReservationKey) {
        this.secretReservationKey = secretReservationKey;
    }

    public String getSecretReservationKey() {
        return secretReservationKey;
    }
}
