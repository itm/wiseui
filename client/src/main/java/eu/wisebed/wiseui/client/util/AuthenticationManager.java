package eu.wisebed.wiseui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Manager for storing a session when logging in to a testbed.
 * 
 * @author Malte Legenhausen
 */
@Singleton
public class AuthenticationManager {

	// Cookie duration in hours
	private static final int COOKIE_DURATION = 1000 * 60 * 60 * 24;
	
    private static final String SEPARATOR = "&";

    private static final String PREFIX = AuthenticationManager.class.getName() + SEPARATOR;

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
        final String[] tokens = token.split(SEPARATOR, 3);
        final SecretAuthenticationKey key = new SecretAuthenticationKey();
        key.setUrnPrefix(tokens[0]);
        key.setUsername(tokens[1]);
        key.setSecretAuthenticationKey(tokens[2]);
        return key;
    }

    private String serialize(final SecretAuthenticationKey key) {
        final StringBuilder builder = new StringBuilder();
        builder.append(AuthenticationManager.class.getName()).append(SEPARATOR);
        builder.append(key.getUrnPrefix()).append(SEPARATOR);
        builder.append(key.getUsername()).append(SEPARATOR);
        builder.append(key.getSecretAuthenticationKey());
        return builder.toString();
    }

    public void addSecretAuthenticationKey(final SecretAuthenticationKey key) {
        secretAuthenticationKeys.add(key);
        if (Cookies.isCookieEnabled()) {
            final String name = PREFIX + key.getUrnPrefix();
            Cookies.setCookie(name, serialize(key), getCookieExpirationDate());
        }
    }

    public List<SecretAuthenticationKey> getSecretAuthenticationKeys() {
        return secretAuthenticationKeys;
    }
    
    /**
     * Get all WiseUi related cookies
     * @return cookie name
     */
    public String getWiseUiCookies(){
        for (final String name : Cookies.getCookieNames()) {
            if (name.startsWith(PREFIX)) 
                return Cookies.getCookie(name);
        }
        return null;
    }
    
    /**
     * Get cookie expiration date object
     * @return expiration expiration date object
     */
    private Date getCookieExpirationDate(){
    	Date expiration = new Date();
    	expiration.setTime(expiration.getTime() + COOKIE_DURATION);
    	return expiration;
    }
}
