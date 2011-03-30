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
