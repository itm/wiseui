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
package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.ImplementedBy;

@ImplementedBy(TestbedEditViewImpl.class)
public interface TestbedEditView extends IsWidget {

	HasText getNameHasText();
	
	HasText getTestbedUrlHasText();
	
	HasText getSnaaEndpointUrlHasText();
	
	HasText getRsEndpointUrlHasText();
	
	HasText getSessionManagementEndpointUrlHasText();
	
	HasText getUrnPrefixHasText();
	
	HasData<String> getUrnPrefixHasData();
	
	HasEnabled getUrnPrefixRemoveHasEnabled();
	
	void setUrnPrefixSelectionModel(SelectionModel<String> selectionModel);
	
	void setFederatedItems(List<String> items);
	
	void setFederatedSelectedIndex(int index);
	
	int getFederatedSelectedIndex();
	
	void setPresenter(Presenter presenter);
	
	void show(String title);
	
    void hide();
    
	boolean validate();
	
	public interface Presenter {
		
		void submit();
		
		void cancel();
		
		void add();
		
		void remove();
	}
}
