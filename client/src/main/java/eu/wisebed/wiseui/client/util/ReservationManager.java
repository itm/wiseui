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
package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

@Singleton
public class ReservationManager {

	/**
	 * Cookie validity set to 1 month.
	 * TODO finalize this period
	 */
	private static final long VALIDITY = 30 *1000 * 60 * 60 * 24;

	private static final String NAME =  ReservationManager.class.getName();

	private static final String SEPARATOR = "&";

	private static final String PREFIX = NAME + SEPARATOR;

	private final List<SecretReservationKey> secretReservationKeys = Lists.newLinkedList();

	private final HashMap<SecretReservationKey,SecretAuthenticationKey > map = 
		new HashMap<SecretReservationKey,SecretAuthenticationKey>();
	
	private final HashMap<Appointment, SecretReservationKey> privateReservationsMap = 
		new HashMap<Appointment, SecretReservationKey>();

	/**
	 * Load all authentication keys from the cookie.
	 */
	public void init() {

		GWT.log("Init Reservation Manager");
		if (!Cookies.isCookieEnabled()) {
			GWT.log("Cookies disabled");
			return;
		}

		final Collection<String> names = 
			Collections2.filter(Cookies.getCookieNames(), new Predicate<String>() {
				@Override
				public boolean apply(String name) {
					return name.startsWith(PREFIX);
				}
			});
		
		final Collection<TestbedKeysPair> keyPairs =
			Collections2.transform(names, new Function<String, TestbedKeysPair>() {
				@Override
				public TestbedKeysPair apply(String name) {
					return deserialize(Cookies.getCookie(name));
				}
			});
		for(TestbedKeysPair keyPair : keyPairs){
			secretReservationKeys.add(keyPair.getReservationKey());
			map.put(keyPair.getReservationKey(),keyPair.getAuthenticationKey());
		}
	}
	
	private TestbedKeysPair deserialize(final String token) {
		final String[] tokens = token.split(SEPARATOR, 7);
		final SecretAuthenticationKey authKey = new SecretAuthenticationKey();
		final SecretReservationKey resKey = new SecretReservationKey();
		authKey.setUrnPrefix(tokens[1]);
		authKey.setUsername(tokens[2]);
		authKey.setSecretAuthenticationKey(tokens[3]);
		authKey.setSecretAuthenticationKeyID((new Integer(tokens[4])).intValue());
		resKey.setUrnPrefix(tokens[5]);
		resKey.setSecretReservationKey(tokens[6]);
		final TestbedKeysPair keyPair = new TestbedKeysPair(authKey,resKey);
		return keyPair;
	}
	
	private String serialize(final TestbedKeysPair keyPair) {
		final SecretAuthenticationKey authKey = keyPair.getAuthenticationKey();
		final SecretReservationKey resKey = keyPair.getReservationKey();
		return Joiner.on(SEPARATOR).join(
				NAME,
				authKey.getUrnPrefix(),
				authKey.getUsername(),
				authKey.getSecretAuthenticationKey(),
				authKey.getSecretAuthenticationKeyID(),
				resKey.getUrnPrefix(),
				resKey.getSecretReservationKey()
		);
	}

	private String toCookieName(final SecretReservationKey key) {
		return PREFIX + key.getSecretReservationKey();
	}

	public void addSecretReservationKey(final SecretAuthenticationKey authKey,
			final SecretReservationKey resKey) {
		map.put(resKey,authKey);
		secretReservationKeys.add(resKey);
		if (Cookies.isCookieEnabled()) {
			final Date expiration =  new Date(System.currentTimeMillis() + VALIDITY);
			TestbedKeysPair keyPair = new TestbedKeysPair();
			Cookies.setCookie(toCookieName(resKey), serialize(keyPair), expiration);
		}
	}
	
	public void removeSecretReservationKey(final SecretReservationKey key) {
		map.remove(key.getUrnPrefix());
		secretReservationKeys.remove(key);
		if (Cookies.isCookieEnabled()) {
			Cookies.removeCookie(toCookieName(key));
		}
	}

	public List<SecretReservationKey> getSecretReservationKeys() {
		return secretReservationKeys;
	}

	public void addReservation(final Appointment reservation, final SecretReservationKey rsKey){
		privateReservationsMap.put(reservation, rsKey);
	}
	
	public void removeReservation(final Appointment reservation){
		privateReservationsMap.remove(reservation);
	}

	/**
	 * Return the map
	 * @return SecretReservationKey-to-SecretAuthenticationKey map
	 */
	public HashMap<SecretReservationKey,SecretAuthenticationKey> getMap(){
		return map;
	}
	
	/**
	 * Return the private reservations map
	 * @return Appointment-to-String map
	 */
	public HashMap<Appointment, SecretReservationKey> getPrivateReservationsMap(){
		return privateReservationsMap;
	}
	
	/**
	 * Return set of filter secret reservation keys according to urn prefix of keys
	 */
	public List<SecretReservationKey> getFilteredSecretReservationKeys(final String urnPrefix)
	{
		List<SecretReservationKey> filteredKeys = 
			new ArrayList<SecretReservationKey>();
		for(SecretReservationKey key : map.keySet()) {
			if(key.getUrnPrefix() == urnPrefix){
				filteredKeys.add(key);
			}
		}
		return filteredKeys;
	}
}
