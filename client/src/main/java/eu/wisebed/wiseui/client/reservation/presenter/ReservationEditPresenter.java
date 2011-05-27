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
package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.i18n.ReservationMessages;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView.Presenter;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

/**
 * @author Soenke Nommensen
 */
public class ReservationEditPresenter implements Presenter, EditReservationEvent.Handler, ConfigurationSelectedHandler,
	PlaceChangeEvent.Handler, ReservationSuccessEvent.Handler, ReservationFailedEvent.Handler{

    private static final String DEFAULT_NEW_TITLE = "New Reservation";

    private WiseUiGinjector injector;
    
    private final EventBusManager eventBus;

    private final ReservationEditView view;

    private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();
    
    private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

    private TestbedConfiguration configuration;
    
    private ReservationServiceAsync service;

    private String title = DEFAULT_NEW_TITLE;
    
    private List<String> nodes = new ArrayList<String>();
    
    private ReservationMessages messages;

    @Inject
    public ReservationEditPresenter(final WiseUiGinjector injector,
    								final EventBus eventBus,
                                    final ReservationEditView view,
                                    final ReservationServiceAsync service,
                                    final ReservationMessages messages) {
    	this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.service = service;
        this.messages = messages;
        
        urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
        view.setUrnPrefixSelectionModel(selectionModel);
        view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
        bind();
    }

    private void bind() {
        eventBus.addHandler(EditReservationEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationFailedEvent.TYPE, this);
        
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
            }
        });
    }

    /**
     * Sends reservation details to server and books a new reservation.
     */
    @Override
    public void submit() {
    	if (getNodesSelected() == null || getNodesSelected().isEmpty()){
			// TODO: Add a 'suggestion' type message box
			final String suggestion = "Please select at least one node to submit a reservation.";
			MessageBox.info("No nodes selected", suggestion, null);
			return;
		}
    	if (urnPrefixProvider.getList().isEmpty()){
        	final String suggestion = "Please add at least one URN prefix";
			MessageBox.info("No urnprefix added", suggestion, null);
			return; 
        }
        final String rsEndpointUrl = configuration.getRsEndpointUrl();
        final String urnPrefix = urnPrefixProvider.getList().get(0);
        final SecretAuthenticationKey secretAuthKey = injector.getAuthenticationManager().getMap().get(urnPrefix);
        final ReservationDetails data = new ReservationDetails();
        data.setStartTime(view.getStartDateBox().getValue());
        data.setStopTime(view.getEndDateBox().getValue());
        data.setNodes(getNodesSelected());
        data.setUrnPrefix(urnPrefix);
        
        final AsyncCallback<ConfidentialReservationData> callback = new AsyncCallback<ConfidentialReservationData>() {
            public void onFailure(Throwable caught) {
                eventBus.fireEvent(new ReservationFailedEvent(caught));
            }

            public void onSuccess(ConfidentialReservationData result) {
            	GWT.log(result.toString());
                eventBus.fireEvent(new ReservationSuccessEvent());
            }
        };
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				service.makeReservation(secretAuthKey, rsEndpointUrl, data, callback);
                view.hide();
			}
		});
    }

    @Override
    public void cancel() {
        MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                if (Button.OK.equals(button)) {
                    view.hide();
                }
            }
        });
    }

    @Override
    public void add() {
    	final String urn = view.getUrnPrefixHasText().getText();
    	urnPrefixProvider.getList().add(urn);
    	urnPrefixProvider.refresh();
    }

    @Override
    public void remove() {
    	final String urn = selectionModel.getSelectedObject();
    	urnPrefixProvider.getList().remove(urn);
    	urnPrefixProvider.refresh();
    	view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event){
    	configuration = event.getConfiguration();
    }

    private void setNodesSelected(final Set<Node> nodesSelected){
    	nodes.clear();
    	for(Node node: nodesSelected){
    		nodes.add(node.getId());
    	}
    }
    
    private List<String> getNodesSelected(){
    	return this.nodes;
    }
   
    public void onPlaceChange(final PlaceChangeEvent event){
    	eventBus.removeAll();
    }
    
    @Override
    public void onEditReservation(final EditReservationEvent event) {
    	if (null == configuration){
    		final String suggestion = "Please select at least one testbed to make a new reservation";
			MessageBox.info("No testbed selected", suggestion, null);
			return;
    	}
        final String title = Objects.firstNonNull(configuration.getName(), DEFAULT_NEW_TITLE);
    	view.show(title);
    	view.getStartDateBox().setValue(event.getAppointment().getStart());
    	view.getUrnPrefixHasText().setText(configuration.getUrnPrefixList().get(0));
    	view.getWhoTextBox().setText(injector.getAuthenticationManager().getSecretAuthenticationKeys().get(0).getUsername());
        if (event.getNodes()!=null)
        	setNodesSelected(event.getNodes());
    }
    
    public void onReservationSuccess() {
        MessageBox.success(messages.reservationSuccessTitle(), messages.reservationSuccess(), null);
    }
    
    @Override
    public void onReservationFailed(final ReservationFailedEvent event){
    	final String errorMsg_title;
    	final String errorMsg;
    	final Throwable caught = event.getThrowable();
    	// TODO: Get exception's message for more descriptive error handling
        if (caught instanceof AuthenticationException) {
        	errorMsg_title = messages.authenticationKeysExpiredTitle();
        	errorMsg = messages.authenticationKeysExpired();
            GWT.log(errorMsg);
        } else if (caught instanceof ReservationException) {
        	errorMsg_title = messages.faultyReservationParametersTitle();
        	errorMsg = messages.faultyReservationParameters();
            GWT.log(errorMsg);
        } else {
        	errorMsg_title = messages.rsServiceErrorTitle();
        	errorMsg = messages.rsServiceError();        	
            GWT.log(errorMsg);
        }        
		MessageBox.error(errorMsg_title, errorMsg, caught, null);
    }
}
