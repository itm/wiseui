package eu.wisebed.wiseui.shared;

import java.io.Serializable;
import java.util.Set;

import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

public class AuthenticationDetails implements Serializable{
	
	private static final long serialVersionUID = -2901127313017385214L;
	private int userid;
	private String username;
	private String password;
	private Set<SecretAuthenticationKey> secretAuthenticationKeys;
	
	public AuthenticationDetails(){}
	
	public AuthenticationDetails(final int userid, final String username, 
		final String password){
		this.userid = userid;
		this.username = username;
		this.password = password;
	}
	
	public final void setUserid(final int userid){
		this.userid = userid;
	}
	
	public final void setUsername(final String username){
		this.username = username;
	}
	
	public final void setPassword(final String password){
		this.password = password;
	}
	
	public final int getUserid(){
		return this.userid;
	} 

	public final String getUsername(){
		return this.username;
	}
	
	public final String getPassword(){
		return this.password;
	}

	public void setSecretAuthenticationKeys(final Set<SecretAuthenticationKey> 
			secretAuthenticationKeys) {
		this.secretAuthenticationKeys = secretAuthenticationKeys;
	}

	public Set<SecretAuthenticationKey> getSecretAuthenticationKeys() {
		return secretAuthenticationKeys;
	}
}
