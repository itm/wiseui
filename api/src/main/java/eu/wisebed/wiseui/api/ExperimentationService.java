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
package eu.wisebed.wiseui.api;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

@RemoteServiceRelativePath("experimentation.rpc")
public interface ExperimentationService extends RemoteService {
	
	/**
	 * Starts an experiment controller {@link ExperimentController} on the server side after a request.
	 * @param sessionManagementUrl the session management url of the testbed
	 * @param secretReservationKeys List of {@link SecretReservationKeys}s
	 * @param nodeUrns List of node urns
	 * @throws ExperimentationException
	 */
	void startExperimentController(
		String sessionManagementUrl,
		List<SecretReservationKey> secretReservationKeys,
		List<String> nodeUrns) 
		throws 
		ExperimentationException;

	/**
	 * Flashes an uploaded image to the nodes participating in an experiment.
	 * @param secretReservationKeys List of {@link SecretReservationKeys}s
	 * @param imageId image's id stored in persistence
	 * @param nodeUrns List of node urns
	 * @throws ExperimentationException
	 */
	void flashExperimentImage(
			List<SecretReservationKey> secretReservationKeys,
			Integer imageId,
			List<String> nodeUrns) 
		throws 
		ExperimentationException;
	
	/**
	 * Returns an experiment message back to the client
	 * @param secretReservationKeys List of {@link SecretReservationKeys}s
	 * @return an {@link ExperimentMessage} instance
	 */
	ExperimentMessage returnExperimentMessage(
			List<SecretReservationKey> secretReservationKeys)
			throws 
			ExperimentationException;

	/**
	 * Stops an experiment controller {@link ExperimentController} on the server.
	 * @param secretReservationKeys List of {@link SecretReservationKeys}s
	 */
	void stopExperimentController(
			List<SecretReservationKey> secretReservationKeys)
			throws 
			ExperimentationException;
	
	/**
	 * Get WiseML report of the experiment
	 * @param
	 * @return a seriliazed format of the wiseml document
	 */
	String returnExperimentWiseMLReport(
			List<SecretReservationKey> secretReservationKeys)
			throws
			ExperimentationException;
	
	/** 
	 * Returns a list of uploaded {@link BinaryImageBo} on the server. 
	 */
	List<BinaryImage> returnUploadedExperimentImages()
			throws 
			ExperimentationException;
}
