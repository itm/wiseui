/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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

import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey;

public abstract class APIKeysUtil {

	public static List<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey> 
	copySnaaToRs(final List<SecretAuthenticationKey> snaaKeys) {
		List<eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey> newKeys =
			new ArrayList<
			eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey>();
		for (SecretAuthenticationKey snaaKey : snaaKeys) {
			eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey key =
				new eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey();
			key.setSecretAuthenticationKey(snaaKey.
					getSecretAuthenticationKey());
			key.setUrnPrefix(snaaKey.getUrnPrefix());
			key.setUsername(snaaKey.getUsername());
			newKeys.add(key);
		}
		return newKeys;
	}

	public static List<eu.wisebed.testbed.api.wsn.v22.SecretReservationKey> 
	copyRsToWsn(final List<SecretReservationKey> rsKeys) {
		List<eu.wisebed.testbed.api.wsn.v22.SecretReservationKey> newKeys =
			new ArrayList<
			eu.wisebed.testbed.api.wsn.v22.SecretReservationKey>();
		for (SecretReservationKey rsKey : rsKeys) {
			eu.wisebed.testbed.api.wsn.v22.SecretReservationKey newKey =
				new eu.wisebed.testbed.api.wsn.v22.SecretReservationKey();
			newKey.setSecretReservationKey(rsKey.getSecretReservationKey());
			newKey.setUrnPrefix(rsKey.getUrnPrefix());
			newKeys.add(newKey);
		}
		return newKeys;
	}
}
