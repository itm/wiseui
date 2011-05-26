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
package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@RemoteServiceRelativePath("experimentation.rpc")
public interface ExperimentationService extends RemoteService {
	void bindAndStartExperimentController(final int reservationID) 
		throws ReservationException,ExperimentationException;
	void flashExperimentImage(final int reservationID) 
		throws ReservationException,ExperimentationException;
	ExperimentMessage getNextUndeliveredMessage(final int reservationID)
		throws ExperimentationException;
	void terminateExperiment(final int reservationID) 
		throws ExperimentationException;

}
