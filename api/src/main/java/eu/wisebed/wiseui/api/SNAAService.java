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
package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;

@RemoteServiceRelativePath("snaa.rpc")
public interface SNAAService extends RemoteService {

	/**
	 * Authenticates a user to a testbed
	 * @param endpointUrl
	 * @param urn
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException
	 */
    SecretAuthenticationKey authenticate(
    		String endpointUrl,
    		String urn,
    		String username,
    		String password) 
    		throws 
    		AuthenticationException;
}
