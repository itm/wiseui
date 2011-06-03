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
package eu.wisebed.wiseui.client.util;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

public class ReservationManagerGwtTest extends GWTTestCase{

	@Override
	public String getModuleName() {
		return "eu.wisebed.wiseui.WiseUi";
	}
	
	public void testAddSecretReservationKey() {
		final SecretAuthenticationKey authKey1 = new SecretAuthenticationKey();
		authKey1.setSecretAuthenticationKey("sak123");
		authKey1.setUrnPrefix("CTI");
		authKey1.setUsername("whantana");
		authKey1.setSecretAuthenticationKeyID(1337);
		
		final SecretReservationKey resKey1 = new SecretReservationKey();
		resKey1.setUrnPrefix("CTI");
		resKey1.setSecretReservationKey("srk123");
		
		final ReservationManager manager1 = new ReservationManager();
		manager1.init();
		manager1.addSecretReservationKey(authKey1, resKey1);
		
		final ReservationManager manager2 = new ReservationManager();
		manager2.init();
		
		final List<SecretReservationKey> keys = manager2.getSecretReservationKeys();
		assertEquals(1, keys.size());
		HashMap<SecretReservationKey,SecretAuthenticationKey> map =
			manager2.getMap();
		assertEquals(1,map.size());
		SecretReservationKey key = 
			(SecretReservationKey) (map.keySet().toArray())[0];
		assertEquals(authKey1.getSecretAuthenticationKey(),map.get(key).getSecretAuthenticationKey());
		assertEquals(authKey1.getUsername(),map.get(key).getUsername());
		assertEquals(authKey1.getSecretAuthenticationKey(),map.get(key).getSecretAuthenticationKey());
		assertEquals(authKey1.getSecretAuthenticationKeyID(),map.get(key).getSecretAuthenticationKeyID());
		
		final SecretReservationKey resKey2 = new SecretReservationKey();
		resKey2.setUrnPrefix("CTI");
		resKey2.setSecretReservationKey("srk1231234123");

		map = manager2.getMap();
		assertEquals(2,map.size());
		for(SecretReservationKey key1 : map.keySet()){
			assertEquals(authKey1.getSecretAuthenticationKey(),map.get(key1).getSecretAuthenticationKey());
			assertEquals(authKey1.getUsername(),map.get(key1).getUsername());
			assertEquals(authKey1.getSecretAuthenticationKey(),map.get(key1).getSecretAuthenticationKey());
			assertEquals(authKey1.getSecretAuthenticationKeyID(),map.get(key1).getSecretAuthenticationKeyID());
		}
	}
	
	public void testRemoveSecretReservationKey(){
		
	}
}
