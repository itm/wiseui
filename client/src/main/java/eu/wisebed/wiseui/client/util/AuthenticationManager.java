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
package eu.wisebed.wiseui.client.util;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;


/**
 * Manager for storing a session when logging in to a testbed.
 *
 * @author Malte Legenhausen
 */
@Singleton
public class AuthenticationManager {

    /**
     * Cookie validity set to 1 hour.
     */
    private static final long VALIDITY = 1000 * 60 * 60;

    private static final String NAME = AuthenticationManager.class.getName();

    private static final String SEPARATOR = "&";

    private static final String PREFIX = NAME + SEPARATOR;

    private final List<SecretAuthenticationKey> secretAuthenticationKeys = Lists.newLinkedList();

    private final Map<String, SecretAuthenticationKey> map = new HashMap<String, SecretAuthenticationKey>();

    /**
     * Load all authentication keys from the cookie.
     */
    public void init() {

        GWT.log("Init Authentication Manager");
        if (!Cookies.isCookieEnabled()) {
            GWT.log("Cookies disabled");
            return;
        }

        final Collection<String> names = Collections2.filter(Cookies.getCookieNames(), new Predicate<String>() {
            @Override
            public boolean apply(String name) {
                return name.startsWith(PREFIX);
            }
        });
        final Collection<SecretAuthenticationKey> keys =
                Collections2.transform(names, new Function<String, SecretAuthenticationKey>() {
                    @Override
                    public SecretAuthenticationKey apply(String name) {
                        return deserialize(Cookies.getCookie(name));
                    }
                });
        secretAuthenticationKeys.addAll(keys);

        for (SecretAuthenticationKey key : keys) {
            map.put(key.getUrnPrefix(), key);
        }
    }

    private SecretAuthenticationKey deserialize(final String token) {
        final String[] tokens = token.split(SEPARATOR, 5);
        final SecretAuthenticationKey key = new SecretAuthenticationKey();
        key.setUrnPrefix(tokens[1]);
        key.setUsername(tokens[2]);
        key.setSecretAuthenticationKey(tokens[3]);
        key.setSecretAuthenticationKeyID(new Integer(tokens[4]));
        return key;
    }

    private String serialize(final SecretAuthenticationKey key) {
        return Joiner.on(SEPARATOR).join(NAME,
                key.getUrnPrefix(),
                key.getUsername(),
                key.getSecretAuthenticationKey(),
                key.getSecretAuthenticationKeyID()
        );
    }

    private String toCookieName(final SecretAuthenticationKey key) {
        return PREFIX + key.getUrnPrefix();
    }

    public void addSecretAuthenticationKey(final SecretAuthenticationKey key) {
        map.put(key.getUrnPrefix(), key);
        secretAuthenticationKeys.add(key);
        if (Cookies.isCookieEnabled()) {
            final Date expiration = new Date(System.currentTimeMillis() + VALIDITY);
            Cookies.setCookie(toCookieName(key), serialize(key), expiration);
        }
    }

    public List<SecretAuthenticationKey> getSecretAuthenticationKeys() {
        return secretAuthenticationKeys;
    }

    public Map<String, SecretAuthenticationKey> getMap() {
        return map;
    }

    public void removeSecretAuthenticationKey(final SecretAuthenticationKey key) {
        map.remove(key.getUrnPrefix());
        secretAuthenticationKeys.remove(key);
        if (Cookies.isCookieEnabled()) {
            Cookies.removeCookie(toCookieName(key));
        }
    }

    /**
     * Get all WiseUi related cookies
     *
     * @return cookie name
     */
    public List<String> getUrnPrefixByCookie() {
        final List<String> urnPrefix = new ArrayList<String>();
        for (final String name : Cookies.getCookieNames()) {
            if (name.startsWith(PREFIX)) {
                urnPrefix.add(deserialize(Cookies.getCookie(name))
                        .getUrnPrefix());
            }
        }
        return urnPrefix;
    }

    /**
     * Very simple mechanism to determine, whether the user connected to the current testbed.
     * TODO: Do this per URN prefix and reduce computational time!
     *
     * @param testbedConfiguration The {@link TestbedConfiguration} to be checked.
     * @return Whether the user is connected to the current testbed or not.
     */
    public boolean isAuthenticated(final TestbedConfiguration testbedConfiguration) {
    	ifNullArgument(testbedConfiguration, "testbedConfiguration is null!");
        ifNull(testbedConfiguration.getUrnPrefixList(), "testbedConfiguration.getUrnPrefixList() is null!");

        boolean isAuthenticated = true;
        for (String urnPrefix : testbedConfiguration.getUrnPrefixList()) {
            if (!map.containsKey(urnPrefix)) {
                isAuthenticated = false;
                break;
            }
        }

        return isAuthenticated;
    }

    /**
     * TODO Review and maybe improve detection mechanism
     * @param testbedConfiguration
     * @return
     */
    public String getAuthenticatedUserName(final TestbedConfiguration testbedConfiguration) {
        String userName = "-1"; // TODO Is this really what we want (use an empty string...)?
        if (isAuthenticated(testbedConfiguration)) {
            final String firstUrnPrefix = testbedConfiguration.getUrnPrefixList().get(0);
            final SecretAuthenticationKey secretAuthenticationKey = map.get(firstUrnPrefix);
            if (secretAuthenticationKey != null) {
                userName = secretAuthenticationKey.getUsername();
                GWT.log("AuthenticationManager.getAuthenticatedUserName: " + userName + " for prefix: " + firstUrnPrefix);
            }
        }
        return userName;
    }
}
