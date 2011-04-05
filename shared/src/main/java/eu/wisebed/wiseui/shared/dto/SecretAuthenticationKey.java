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
}
