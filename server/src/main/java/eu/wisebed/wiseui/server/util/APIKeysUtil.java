/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.server.util;

import java.util.ArrayList;
import java.util.List;

import eu.wisebed.api.snaa.SecretAuthenticationKey;

public abstract class APIKeysUtil {

	public static List<SecretAuthenticationKey>
	copySnaaToRs(final List<SecretAuthenticationKey> snaaKeys) {
		List<SecretAuthenticationKey> newKeys =
			new ArrayList<
			SecretAuthenticationKey>();
		for (SecretAuthenticationKey snaaKey : snaaKeys) {
			SecretAuthenticationKey key =
				new SecretAuthenticationKey();
			key.setSecretAuthenticationKey(snaaKey.
					getSecretAuthenticationKey());
			key.setUrnPrefix(snaaKey.getUrnPrefix());
			key.setUsername(snaaKey.getUsername());
			newKeys.add(key);
		}
		return newKeys;
	}

	public static List<eu.wisebed.api.sm.SecretReservationKey>
	copyRsToWsn(final List<eu.wisebed.api.rs.SecretReservationKey> rsKeys) {
		List<eu.wisebed.api.sm.SecretReservationKey> newKeys =
			new ArrayList<
			eu.wisebed.api.sm.SecretReservationKey>();
		for (eu.wisebed.api.rs.SecretReservationKey rsKey : rsKeys) {
			eu.wisebed.api.sm.SecretReservationKey newKey =
				new eu.wisebed.api.sm.SecretReservationKey();
			newKey.setSecretReservationKey(rsKey.getSecretReservationKey());
			newKey.setUrnPrefix(rsKey.getUrnPrefix());
			newKeys.add(newKey);
		}
		return newKeys;
	}
}
