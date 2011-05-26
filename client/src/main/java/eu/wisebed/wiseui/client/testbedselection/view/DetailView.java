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
package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.dto.Capability;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;


/**
 * Interface for a view that shows all details of a testbed.
 * 
 * @author Malte Legenhausen
 */
@ImplementedBy(DetailViewImpl.class)
public interface DetailView extends IsWidget {

    void setPresenter(Presenter presenter);
    
    void setTreeViewModel(TreeViewModel model);
    
    HasText getDescriptionHasText();
    
    HasText getNodeIdHasText();
    
    HasText getNodeTypeHasText();
    
    HasText getNodePositionHasText();
    
    HasText getNodeGatewayHasText();
    
    HasText getNodeProgramDetailsHasText();
    
    HasText getNodeDescriptionHasText();
    
    void showMessage(String message);
    
    HasData<Capability> getCapababilitesList();
    
    HasLoadingIndicator getLoadingIndicator();

    /**
     * The presenter for a DetailView.
     * 
     * @author Malte Legenhausen
     */
    public interface Presenter {

    }	
}