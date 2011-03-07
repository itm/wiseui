package eu.wisebed.wiseui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class AuthenticationManager {

    private static final String SEPERATOR = "&";

    private static final String PREFIX = AuthenticationManager.class.getName() + SEPERATOR;

    private final List<SecretAuthenticationKey> secretAuthenticationKeys = new ArrayList<SecretAuthenticationKey>();

    /**
     * Load all authentication keys from the cookie.
     */
    public void init() {
        GWT.log("Init Authentication Manager");
        if (!Cookies.isCookieEnabled()) {
            GWT.log("Cookies disabled");
            return;
        }

        for (final String name : Cookies.getCookieNames()) {
            if (name.startsWith(PREFIX)) {
                final String token = Cookies.getCookie(name);
                secretAuthenticationKeys.add(deserialize(token));
            }
        }
    }

    private SecretAuthenticationKey deserialize(final String token) {
        final String[] tokens = token.split(SEPERATOR, 3);
        final SecretAuthenticationKey key = new SecretAuthenticationKey();
        key.setUrnPrefix(tokens[0]);
        key.setUsername(tokens[1]);
        key.setSecretAuthenticationKey(tokens[2]);
        return key;
    }

    private String serialize(final SecretAuthenticationKey key) {
        final StringBuilder builder = new StringBuilder();
        builder.append(AuthenticationManager.class.getName()).append(SEPERATOR);
        builder.append(key.getUrnPrefix()).append(SEPERATOR);
        builder.append(key.getUsername()).append(SEPERATOR);
        builder.append(key.getSecretAuthenticationKey());
        return builder.toString();
    }

    public void addSecretAuthenticationKey(final SecretAuthenticationKey key) {
        secretAuthenticationKeys.add(key);
        if (Cookies.isCookieEnabled()) {
            final String name = PREFIX + key.getUrnPrefix();
            Cookies.setCookie(name, serialize(key), new Date());
        }
    }

    public List<SecretAuthenticationKey> getSecretAuthenticationKeys() {
        return secretAuthenticationKeys;
    }
}
