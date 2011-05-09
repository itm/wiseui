package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

public class SecretReservationKey implements Serializable {

	private static final long serialVersionUID = -1825088754599715036L;
	
	private String urnPrefix;
	
	private String secretReservationKey;

	public SecretReservationKey() {
		
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
