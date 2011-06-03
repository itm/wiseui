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
package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ExperimentViewImpl.class)
public interface ExperimentView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	    
	String getSecretReservationKey();
	
	void setSecretReservationKey(String key);
	
	String getStartDate();
	
	void setStartDate(String date);
	
	String getStopDate();
	
	void setStopDate(String date);
	
	void setExperimentTiming(String timing);
	
	String getExperimentTiming();
	
	void setStatus(String value);
	
	String getStatus();
	
	void setNodeUrns(List<String> nodeUrns);
		
	void setUsername(String username);
	
	String getUsername();
	
	void activateStartExperimentButton(); 
	
	void activateFlashExperimentButton();
	
	void activateStopExperimentButton();
	
	void deactivateStartExperimentButton();
	
	void deactivateFlashExperimentButton();
	
	void deactivateStopExperimentButton();
	
	public interface Presenter {
		void flashExperimentImage();
		void startExperiment();
		void stopExperiment();
		void showNodeOutput(String node);
	}
}
