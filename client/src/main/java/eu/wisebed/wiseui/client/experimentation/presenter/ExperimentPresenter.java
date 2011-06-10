/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ExperimentationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.event.ExperimentMessageArrivedEvent;
import eu.wisebed.wiseui.client.experimentation.event.ReservationTimeStartedEvent;
import eu.wisebed.wiseui.client.experimentation.event.ReservationTimeEndedEvent;
import eu.wisebed.wiseui.client.experimentation.event.FlashBinaryImageEvent;
import eu.wisebed.wiseui.client.experimentation.util.StringTimer;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentOutputView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentWiseMLOutputView;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.Data;
import eu.wisebed.wiseui.shared.dto.Message;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ExperimentPresenter implements ExperimentView.Presenter,
ReservationTimeStartedEvent.Handler,ReservationTimeEndedEvent.Handler,
ExperimentMessageArrivedEvent.Handler,FlashBinaryImageEvent.Handler{


	public enum ExperimentStatus {
		PENDING			("Pending"),
		READY			("Ready"),
		RUNNING			("Running"),
		STOPPED			("Stopped"),
		TIMEDOUT    	("Reservation time out");

		private String text;
		ExperimentStatus(String text) {
			this.text = text;
		}

		public String getStatusText() {
			return text;
		}
	}

	private final WiseUiGinjector injector;
	private ExperimentView view;
	private FlashExperimentImagePresenter flashExperimentImage;
	private ExperimentWiseMLOutputView wiseMloutputView;
	private HashMap<String,ExperimentOutputView> outputMap;
	private Date fromDate;
	private Date toDate;
	private List<Data> userData;
	private List<String> nodeUrns;
	private String experimentTiming;
	private String flashedImageFilename;
	private Timer reservationStartTimer;
	private Timer reservationStopTimer;
	private Timer experimentMessageCollectionTimer;
	private ExperimentStatus status;
	private String sessionManagementUrl;
	private ExperimentationServiceAsync service;
	private EventBusManager eventBus;

	@Inject
	public ExperimentPresenter(final WiseUiGinjector injector,
			final ExperimentationServiceAsync service,
			final ExperimentView view,
			final FlashExperimentImagePresenter flashExperimentImage,
			final ExperimentWiseMLOutputView wiseMloutputView,
			final EventBus eventBus) {

		this.injector = injector;
		this.view = view;
		this.view.setPresenter(this);
		this.flashExperimentImage = flashExperimentImage;
		this.wiseMloutputView = wiseMloutputView;
		this.userData = new ArrayList<Data>();
		this.outputMap = new HashMap<String,ExperimentOutputView>();
		this.eventBus = new EventBusManager(eventBus);
		this.service = service;
		bind();
	} 

	public ExperimentView getView() {
		return view;
	}
	
	public List<Data> getUserData() {
		return userData;
	}

	public void setUserData(List<Data> userData) {
		this.userData = userData;
	}
	
	@SuppressWarnings("deprecation")
	public void setupExperimentPresenter(final ConfidentialReservationData data, 
			final String url) {

		// set attributes
		fromDate = data.getFrom();
		toDate = data.getTo();
		nodeUrns = data.getNodeURNs();
		userData = data.getData();
		
		sessionManagementUrl = url;
		experimentTiming = "-";
		flashedImageFilename = "-";
		status = ExperimentStatus.PENDING;
		
		// initialize output views
		for(String nodeUrn : nodeUrns) {
			outputMap.put(nodeUrn, injector.getExperimentOutputView());
		}

		// initialize view
		view.setSecretReservationKey(userData.get(0).getSecretReservationKey());
		view.setUsername(userData.get(0).getUsername());
		view.setStartDate(fromDate.toLocaleString());
		view.setStopDate(toDate.toLocaleString());
		view.setExperimentTiming(experimentTiming);
		view.setFlashedImageFilename(flashedImageFilename);
		view.setStatus(status.getStatusText());
		view.setNodeUrns(nodeUrns);
		view.deactivateStartExperimentButton();
		view.deactivateFlashExperimentButton();
		view.deactivateStopExperimentButton();
		view.deactivateDownloadWiseMLButton();		
		
		// start the reservation start timer
		startReservationStartTimer();
	}

	public void bind() {
		eventBus.addHandler(ReservationTimeStartedEvent.TYPE, this);
		eventBus.addHandler(ReservationTimeEndedEvent.TYPE, this);
		eventBus.addHandler(ExperimentMessageArrivedEvent.TYPE, this);
		eventBus.addHandler(FlashBinaryImageEvent.TYPE, this);
	}
	
	@Override 
	public void onReservationTimeStarted(ReservationTimeStartedEvent event) {
		if(event.getSource() == this ){

			// start the reservation stop timer
			startReservationStopTimer();

			// update status
			status = ExperimentStatus.READY;

			// update view
			view.setStatus(status.getStatusText());
			view.activateStartExperimentButton();
		}
	}

	@Override
	public void onReservationTimeEnded(ReservationTimeEndedEvent event) {
		if(event.getSource() == this ){

			// if experiment message collection timer is not null stop it
			if(experimentMessageCollectionTimer != null) {
				experimentMessageCollectionTimer.cancel();
			}

			// if this presenter is on the active list 
			if(injector.getExperimentationManager().getExperimentFromActiveList(this) != null) {
				injector.getExperimentationManager().removeExperimentFromActiveList(this);
			}

			// update status
			status = ExperimentStatus.TIMEDOUT;

			// update view
			view.setStatus(status.getStatusText());
			view.setExperimentTiming("-");
			view.deactivateStartExperimentButton();
			view.deactivateFlashExperimentButton();
			view.deactivateStopExperimentButton();
			view.deactivateDownloadWiseMLButton();
		}
	}

	

	
	@Override
	public void onMessageArrival(ExperimentMessageArrivedEvent event) {
		if(event.getSource() == this ){
			if(status == ExperimentStatus.RUNNING) {
				
				// setup callback
				AsyncCallback<Message> callback = new AsyncCallback<Message>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
					}

					@Override
					public void onSuccess(final Message result) {
						if(result == null) return;
						GWT.log(result.toString());
						outputMap.get(result.getSourceNodeId()).addOutput(result.toString());
					}
					
				};
				
				// make the rpc call
				List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
				for(int i=0;i<userData.size();i++) {
					SecretReservationKey key = new SecretReservationKey();
					key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
					key.setUrnPrefix(userData.get(i).getSecretReservationKey());
					secretReservationKeys.add(key);
				}
				service.returnExperimentMessage(secretReservationKeys, callback);
			}
		}
	}
	
	@Override
	public void onFlashBinaryImageEvent(FlashBinaryImageEvent event) {
		if(event.getSource() == flashExperimentImage) {
			flashSelectedImage(event.getImageToBeFlashed());
		}
	}

	@Override
	public void showFlashExperimentImageView() {

		// set title
		String title = "Flash Experiment image";
		flashExperimentImage.getAvailableImages();
		flashExperimentImage.getView().show(title);
	}

	@Override
	public void startExperiment() {

		// this instance 
		final ExperimentPresenter currentExperiment = this;

		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {

				// add the experiment in the active experiment list
				injector.getExperimentationManager().addExperimentToActiveList(currentExperiment);
				
				// update status 
				status = ExperimentStatus.RUNNING;

				// update view
				view.setStatus(status.getStatusText());
				view.deactivateStartExperimentButton();
				view.activateFlashExperimentButton();
				view.activateStopExperimentButton();
				view.activateDownloadWiseMLButton();

				// start collecting messages 
				startExperimentMessageCollectorTimer();
			}

		};

		// make the rpc call
		List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
		for(int i=0;i<userData.size();i++) {
			SecretReservationKey key = new SecretReservationKey();
			key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
			key.setUrnPrefix(userData.get(i).getSecretReservationKey());
			secretReservationKeys.add(key);
		}
		service.startExperimentController(
				sessionManagementUrl,
				secretReservationKeys,
				nodeUrns,callback);
	}


	@Override
	public void stopExperiment() {

		// this instance 
		final ExperimentPresenter currentExperiment = this;

		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {
				// add the experiment in the active experiment list
				injector.getExperimentationManager().removeExperimentFromActiveList(currentExperiment);
			}

		};

		// make the rpc call
		List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
		for(int i=0;i<userData.size();i++) {
			SecretReservationKey key = new SecretReservationKey();
			key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
			key.setUrnPrefix(userData.get(i).getSecretReservationKey());
			secretReservationKeys.add(key);
		}
		service.stopExperimentController(secretReservationKeys,callback);

		// if experiment message collection timer is not null stop it
		if(experimentMessageCollectionTimer != null) {
			experimentMessageCollectionTimer.cancel();
		}

		// update status
		status = ExperimentStatus.STOPPED;

		// update view
		view.setStatus(status.getStatusText());
		view.deactivateStopExperimentButton();
		view.deactivateFlashExperimentButton();
		view.activateStartExperimentButton();
	}
	
	public void flashSelectedImage(final BinaryImage image) {
		
		
		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {
				view.setFlashedImageFilename(image.getFileName());
				flashExperimentImage.getView().hide();
			}
			
		};
		
		// make the rpc call
		List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
		for(int i=0;i<userData.size();i++) {
			SecretReservationKey key = new SecretReservationKey();
			key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
			key.setUrnPrefix(userData.get(i).getSecretReservationKey());
			secretReservationKeys.add(key);
		}
		service.flashExperimentImage(secretReservationKeys,image.getId(),nodeUrns,callback);
	}
	
	@Override
	public void resetExperimentNodes() {
		
		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
				
			}

			@Override
			public void onSuccess(Void result) {
				GWT.log("Nodes are now reset!");
			}
			
		};
		
		// make the rpc call
		List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
		for(int i=0;i<userData.size();i++) {
			SecretReservationKey key = new SecretReservationKey();
			key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
			key.setUrnPrefix(userData.get(i).getSecretReservationKey());
			secretReservationKeys.add(key);
		}
		service.resetExperimentNodes(secretReservationKeys,nodeUrns,callback);
	}
	
	@Override
	public void getWiseMLReport() {
		// setup callback
		AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(final String result) {
				wiseMloutputView.setWiseMLOutput(result);
				wiseMloutputView.show("WiseML report");
			}
			
		};
		
		// make the rpc call
		List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();
		for(int i=0;i<userData.size();i++) {
			SecretReservationKey key = new SecretReservationKey();
			key.setSecretReservationKey(userData.get(i).getSecretReservationKey());
			key.setUrnPrefix(userData.get(i).getSecretReservationKey());
			secretReservationKeys.add(key);
		}
		service.returnExperimentWiseMLReport(secretReservationKeys,callback);
	}

	@Override
	public void showNodeOutput(final String node) {
		outputMap.get(node).show("Output for " + node );
	}

	
	private void startReservationStartTimer() {

		// source is this presenter
		final ExperimentPresenter source = this;

		// reservation start timer counts till the reservation starts
		if(reservationStartTimer == null) {
			reservationStartTimer = new Timer() {

				@Override
				public void run() {
					long diffInMillis = 
						fromDate.getTime() - (new Date()).getTime();
					if(diffInMillis <= 0) {
						this.cancel();
						eventBus.fireEventFromSource(new ReservationTimeStartedEvent(),source);
					}else{
						experimentTiming = "Starting in " + StringTimer.elapsedTimeToString(diffInMillis);
						view.setExperimentTiming(experimentTiming);
					}
				}
			};
		}

		// start reservation start timer
		reservationStartTimer.scheduleRepeating(1000);
	}

	private void startReservationStopTimer() {

		// source is this presenter
		final ExperimentPresenter source = this;

		// reservation stop timer counts till the reservation ends
		if(reservationStopTimer == null) {
			reservationStopTimer = new Timer() {

				@Override
				public void run() {
					long diffInMillis = 
						toDate.getTime() - (new Date()).getTime();
					if(diffInMillis <= 0) {
						this.cancel();
						eventBus.fireEventFromSource(new ReservationTimeEndedEvent(),source);
					}else{
						experimentTiming = "Finishing in " +
						StringTimer.elapsedTimeToString(diffInMillis);
						view.setExperimentTiming(experimentTiming);
					}
				}
			};
		}

		// start reservation stop timer
		reservationStopTimer.scheduleRepeating(1000);
	}

	private void startExperimentMessageCollectorTimer() {

		// source is this presenter
		final ExperimentPresenter source = this;

		//experiment stop timer counts till the reservation ends
		if(experimentMessageCollectionTimer == null) {
			experimentMessageCollectionTimer = new Timer() {

				@Override
				public void run() {
					eventBus.fireEventFromSource(new ExperimentMessageArrivedEvent(), source);
				}
			};
		}

		// start experiment collection timer
		experimentMessageCollectionTimer.scheduleRepeating(500);
	}
}