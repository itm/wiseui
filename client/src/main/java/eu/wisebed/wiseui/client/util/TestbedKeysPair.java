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
