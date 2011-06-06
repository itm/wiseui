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

/**
 * @author Malte Legenhausen
 */
public class SecretAuthenticationKey implements Serializable {

    private static final long serialVersionUID = 9132290399485303471L;

    private int secretAuthenticationKeyID;

    private String username;

    private String secretAuthenticationKey;

    private String urnPrefix;

    public SecretAuthenticationKey() {

    }

    public int getSecretAuthenticationKeyID() {
        return secretAuthenticationKeyID;
    }

    public void setSecretAuthenticationKeyID(int secretAuthenticationKeyID) {
        this.secretAuthenticationKeyID = secretAuthenticationKeyID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getSecretAuthenticationKey() {
        return secretAuthenticationKey;
    }

    public void setSecretAuthenticationKey(final String secretAuthenticationKey) {
        this.secretAuthenticationKey = secretAuthenticationKey;
    }

    public String getUrnPrefix() {
        return urnPrefix;
    }

    public void setUrnPrefix(final String urnPrefix) {
        this.urnPrefix = urnPrefix;
    }

    @Override
    public String toString() {
        return "SecretAuthenticationKey{" +
                "secretAuthenticationKeyID=" + secretAuthenticationKeyID +
                ", username='" + username + '\'' +
                ", secretAuthenticationKey='" + secretAuthenticationKey + '\'' +
                ", urnPrefix='" + urnPrefix + '\'' +
                '}';
    }
}
