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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter;
import static eu.wisebed.wiseui.shared.common.Checks.ifNull;

@Singleton
public class ExperimentationManager {

	final private List<ExperimentPresenter> inactiveExperiments = 
		new ArrayList<ExperimentPresenter>();
	final private List<ExperimentPresenter> activeExperiments = 
		new ArrayList<ExperimentPresenter>();
	
	public void init(){
		GWT.log("Init Experimentation Manager");
	}

	
	/**
	 * Adds an {@link ExperimentPresenter} to the active experiment list.
	 * @param experiment an {@link ExperimentPresenter} instance
	 * @return
	 */
	public boolean addExperimentToActiveList(final ExperimentPresenter experiment) {
		
		//if experiment is already in the list don't add it
		String key = experiment.getSecretReservationKeyValue();
		try{
			ifNull(getExperimentFromActiveList(key),"");
		}catch(RuntimeException cause) {
			return false;
		}
		
		// else add it
		return activeExperiments.add(experiment);
	}
	
	/**
	 * Adds an {@link ExperimentPresenter} to the inactive experiment list.
	 * @param experiment an {@link ExperimentPresenter} instance
	 * @return
	 */
	public boolean addExperimentToInActiveList(final ExperimentPresenter experiment) {
		
		//if experiment is already in the list don't add it
		String key = experiment.getSecretReservationKeyValue();
		try{
			ifNull(getExperimentFromInactiveList(key),"");
		}catch(RuntimeException cause) {
			return false;
		}
		
		// else add it
		return inactiveExperiments.add(experiment);
	}
	
	/**
	 * Removes an {@link ExperimentPresenter} from the active experiment list.
	 * @param experiment
	 * @return
	 */
	public boolean removeExperimentFromActiveList(final ExperimentPresenter experiment) {
		
		//if experiment is already in the list don't add it
		String key = experiment.getSecretReservationKeyValue();
		ExperimentPresenter foundExperiment = getExperimentFromActiveList(key);
		try{
			ifNull(foundExperiment,"");
		}catch(RuntimeException cause) {
			return false;
		}
		
		return activeExperiments.remove(foundExperiment);
	}
	
	/**
	 * Removes an {@link ExperimentPresenter} from the inactive experiment list.
	 * @param experiment
	 * @return
	 */
	public boolean removeExperimentFromInActiveList(final ExperimentPresenter experiment) {
		
		//if experiment is already in the list don't add it
		String key = experiment.getSecretReservationKeyValue();
		ExperimentPresenter foundExperiment = getExperimentFromInactiveList(key);
		try{
			ifNull(foundExperiment,"");
		}catch(RuntimeException cause) {
			return false;
		}
		
		return inactiveExperiments.remove(foundExperiment);
	}
	
	/**
	 * Retrieves an {@link ExperimentPresenter} instance if it is already 
	 * in the active list.Else returns null.
	 * @param key a key value
	 * @return instance of {@link ExperimentPresenter} or null
	 */
	public ExperimentPresenter getExperimentFromActiveList(final String key) {
		
		// iterate the list
		for(ExperimentPresenter experiment : activeExperiments) {
			if(experiment.getSecretReservationKeyValue().equals(key)) {
				return experiment;
			}
		}
		
		// nothing found return null
		return null;
	}
	
	/**
	 * Retrieves an {@link ExperimentPresenter} instance if it is already 
	 * in the inactive list.Else returns null.
	 * @param key a key value
	 * @return instance of {@link ExperimentPresenter} or null
	 */
	public ExperimentPresenter getExperimentFromInactiveList(final String key) {
		
		// iterate the list
		for(ExperimentPresenter experiment : inactiveExperiments) {
			if(experiment.getSecretReservationKeyValue().equals(key)) {
				return experiment;
			}
		}
		
		// nothing found return null
		return null;
	}
	
}
