package eu.wisebed.wiseui.client.util;

import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

public class AuthenticationManagerTest extends GWTTestCase {
	
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
