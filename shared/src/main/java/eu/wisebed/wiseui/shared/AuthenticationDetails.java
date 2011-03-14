package eu.wisebed.wiseui.shared;

import java.io.Serializable;

public class AuthenticationDetails implements Serializable{
	
	private static final long serialVersionUID = -2901127313017385214L;
	private int userid;
	private String username;
	private String password;
	private String secretAuthKey;
	private String urnPrefix;
	
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

	public void setSecretAuthenticationKey(final String secretAuthKey) {
		this.secretAuthKey = secretAuthKey;
	}

	public String getSecretAuthenticationKey() {
		return secretAuthKey;
	}

	public void setUrnPrefix(final String urnPrefix) {
		this.urnPrefix = urnPrefix;
	}

	public String getUrnPrefix() {
		return urnPrefix;
	}
}
