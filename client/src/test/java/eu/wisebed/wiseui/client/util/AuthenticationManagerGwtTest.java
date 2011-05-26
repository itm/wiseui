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

import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;

public class AuthenticationManagerGwtTest extends GWTTestCase {
	
	@Override
	public String getModuleName() {
		return "eu.wisebed.wiseui.WiseUi";
	}

	public void testAddSecretAuthenticationKey() {
		final SecretAuthenticationKey key = new SecretAuthenticationKey();
		key.setSecretAuthenticationKey("foo");
		key.setUrnPrefix("bar");
		key.setUsername("foobar");
		
		final AuthenticationManager manager1 = new AuthenticationManager();
		manager1.init();
		manager1.addSecretAuthenticationKey(key);
		
		final AuthenticationManager manager2 = new AuthenticationManager();
		manager2.init();
		final List<SecretAuthenticationKey> keys = manager2.getSecretAuthenticationKeys();
		assertEquals(1, keys.size());
		
		final SecretAuthenticationKey result = keys.get(0);
		assertEquals(key.getSecretAuthenticationKey(), result.getSecretAuthenticationKey());
		assertEquals(key.getUrnPrefix(), result.getUrnPrefix());
		assertEquals(key.getUsername(), result.getUsername());
	}
	
	public void testRemoveSecretAuthenticationKey() {
		final AuthenticationManager manager1 = new AuthenticationManager();
		manager1.init();
		manager1.removeSecretAuthenticationKey(manager1.getSecretAuthenticationKeys().get(0));
		
		final AuthenticationManager manager2 = new AuthenticationManager();
		manager2.init();
		assertEquals(manager2.getSecretAuthenticationKeys().size(), 0);
	}
}
