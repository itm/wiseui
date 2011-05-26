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
package eu.wisebed.wiseui.client.testbedlist;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.testbedlist.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedlist.presenter.TestbedEditPresenter;
import eu.wisebed.wiseui.client.testbedlist.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.client.util.AuthenticationManager;

public interface TestbedListGinjector extends Ginjector {

	TestbedListActivity getTestbedListActivity();
	
	TestbedListView getTestbedListView();
	
    LoginDialogView getLoginDialogView();
    
    LoginDialogPresenter getLoginDialogPresenter();
    
	TestbedEditPresenter getTestbedEditPresenter();
	
	TestbedEditView getTestbedEditView();

    AuthenticationManager getAuthenticationManager();
}
