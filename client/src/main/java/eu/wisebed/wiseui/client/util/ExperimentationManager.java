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
package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter;

@Singleton
public class ExperimentationManager {

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
	public void addExperimentToActiveList(final ExperimentPresenter experiment) {
		String key = experiment.getSecretReservationKeyValue();
		GWT.log("Adding experiment with key = "+key+" to the active list");
		activeExperiments.add(experiment);
	}
		
	/**
	 * Removes an {@link ExperimentPresenter} from the active experiment list.
	 * @param experiment
	 */
	public void removeExperimentFromActiveList(final ExperimentPresenter experiment) {
		String key = experiment.getSecretReservationKeyValue();
		GWT.log("Removing experiment with key = "+key+" to the active list");
		activeExperiments.remove(experiment);
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
				GWT.log("Experiment with key = " + key +" belongs in the active list");
				return experiment;
			}
		}
		
		// nothing found return null
		GWT.log("Experiment with key = " + key + " does not belong in the active list");
		return null;
	}
	
	/**
	 * Retrieves an {@link ExperimentPresenter} instance if it is already 
	 * in the active list.Else returns null.
	 * @param an {@link ExperimentPresenter} instance.
	 * @return instance of {@link ExperimentPresenter} or null.
	 */
	public ExperimentPresenter getExperimentFromActiveList(final ExperimentPresenter experiment) {
		String key = experiment.getSecretReservationKeyValue();
		if(activeExperiments.contains(experiment)) {
			GWT.log("Experiment with key = " + key +" belongs in the active list");
			return experiment;
		}
		
		// nothing found return null
		GWT.log("Experiment with key = " + key + " does not belong in the active list");
		return null;
	}
}
