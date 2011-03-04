package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

@RemoteServiceRelativePath("snaa.rpc")
public interface SNAAService extends RemoteService {

    SecretAuthenticationKey authenticate(String endpointUrl, String urn, String username, String password) throws AuthenticationException;
}
