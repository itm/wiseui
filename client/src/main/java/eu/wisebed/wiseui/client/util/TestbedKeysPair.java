package eu.wisebed.wiseui.client.util;

import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

/**
 * A class to pair two testbed keys. One authentication key and one reservation key
 * This class is not meant to point the type of the relationship between those two keys
 * but to ease the development of the ReservationManager.
 */
public class TestbedKeysPair {
	
	
	private SecretReservationKey reservationKey;
	private SecretAuthenticationKey authenticationKey;
	
	public TestbedKeysPair(){
	}
	
	public TestbedKeysPair(SecretAuthenticationKey authenticationKey,
			final SecretReservationKey reservationKey) {
		this.setReservationKey(reservationKey);
		this.setAuthenticationKey(authenticationKey);
	}

	public void setReservationKey(SecretReservationKey reservationKey) {
		this.reservationKey = reservationKey;
	}

	public SecretReservationKey getReservationKey() {
		return reservationKey;
	}

	public void setAuthenticationKey(SecretAuthenticationKey authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public SecretAuthenticationKey getAuthenticationKey() {
		return authenticationKey;
	}
}
