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
package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.Arrays;
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
import eu.wisebed.wiseui.client.experimentation.event.SuccessfulImageUploadEvent;
import eu.wisebed.wiseui.client.experimentation.util.StringTimer;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentOutputView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentWiseMLOutputView;
import eu.wisebed.wiseui.client.experimentation.view.FlashExperimentImageView;
import eu.wisebed.wiseui.client.experimentation.view.ImageUploadWidget;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ExperimentPresenter implements ExperimentView.Presenter, 
FlashExperimentImageView.Presenter,ImageUploadWidget.Presenter,
ReservationTimeStartedEvent.Handler,ReservationTimeEndedEvent.Handler,
ExperimentMessageArrivedEvent.Handler, SuccessfulImageUploadEvent.Handler{

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
	private FlashExperimentImageView imageView; // TODO provide presenters for those three
	private ImageUploadWidget imageUploadWidget; //
	private ExperimentWiseMLOutputView wiseMloutputView; //
	private HashMap<String,ExperimentOutputView> outputMap;
	private ExperimentationServiceAsync service;
	private EventBusManager eventBus;
	private String secretReservationKeyValue;
	private String username;
	private String urnPrefix;
	private String flashedImageFilename;
	private Date fromDate;
	private Date toDate;
	private List<String> nodeUrns;
	private String experimentTiming;
	private Timer reservationStartTimer;
	private Timer reservationStopTimer;
	private Timer experimentMessageCollectionTimer;
	private ExperimentStatus status;
	private String sessionManagementUrl;
	private BinaryImage selectedImage;

	@Inject
	public ExperimentPresenter(final WiseUiGinjector injector,
			final ExperimentationServiceAsync service,
			final ExperimentView view,
			final FlashExperimentImageView flashImageView,
			//final ExperimentOutputView outputView,
			final ExperimentWiseMLOutputView wiseMloutputView,
			final EventBus eventBus) {

		this.injector = injector;
		this.view = view;
		this.view.setPresenter(this);
		this.imageView = flashImageView;
		this.imageView.setPresenter(this);
		this.imageUploadWidget = imageView.getImageUploadWidget();
		this.imageUploadWidget.setPresenter(this);
		this.wiseMloutputView = wiseMloutputView;
		this.outputMap = new HashMap<String,ExperimentOutputView>();
		this.service = service;
		this.eventBus = new EventBusManager(eventBus);
		this.selectedImage = null;
		bind();
	} 


	public EventBusManager getEventBus() {
		return eventBus;
	}


	public void setEventBus(EventBusManager eventBus) {
		this.eventBus = eventBus;
	}


	public ExperimentView getView() {
		return view;
	}

	public String getSecretReservationKeyValue() {
		return secretReservationKeyValue;
	}


	public void setSecretReservationKeyValue(String secretReservationKeyValue) {
		this.secretReservationKeyValue = secretReservationKeyValue;
	}
	
	public void setSelectedImage(final BinaryImage selectedImage) {
		this.selectedImage = selectedImage;
	}


	public BinaryImage getSelectedImage() {
		return selectedImage;
	}

	public void setupExperimentPresenter(final ConfidentialReservationData data, 
			final String sessionManagementUrl) {
		setExperimentData(data,sessionManagementUrl);
		initView();
		startReservationStartTimer();
	}

	public void bind() {
		eventBus.addHandler(ReservationTimeStartedEvent.TYPE, this);
		eventBus.addHandler(ReservationTimeEndedEvent.TYPE, this);
		eventBus.addHandler(ExperimentMessageArrivedEvent.TYPE, this);
		eventBus.addHandler(SuccessfulImageUploadEvent.TYPE, this);
	}

	@Override
	public void showFlashExperimentImageView() {

		// set title
		String title = "Flash Experiment image";
		getAvailableImages();
		imageView.show(title);
	}

	@Override
	public void getAvailableImages() {


		// setup callback
		AsyncCallback<List<BinaryImage>> callback = new AsyncCallback<List<BinaryImage>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(List<BinaryImage> results) {
				imageView.setAvailableImages(results);
			}

		};

		// make the rpc
		service.returnUploadedExperimentImages(callback);
	}

	@Override
	public void submit() {
		flashSelectedImage();
	}

	@Override
	public void startExperiment() {

		// this instance 
		final ExperimentPresenter currentExperiment = this;

		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {

				// add the experiment in the active experiment list
				injector.getExperimentationManager().addExperimentToActiveList(currentExperiment);
				GWT.log("Experiment controller on server published");
			}

		};

		// make the rpc call
		SecretReservationKey key = new SecretReservationKey();
		key.setSecretReservationKey(secretReservationKeyValue);
		key.setUrnPrefix(urnPrefix);
		List<SecretReservationKey> secretReservationKeys = Arrays.asList(key);
		service.startExperimentController(
				sessionManagementUrl,
				secretReservationKeys,
				nodeUrns,callback);

		// start message collection timer
		startExperimentMessageCollectorTimer();

		// update status 
		status = ExperimentStatus.RUNNING;

		//update view
		view.setStatus(status.getStatusText());
		view.deactivateStartExperimentButton();
		view.activateFlashExperimentButton();
		view.activateStopExperimentButton();
		view.activateDownloadWiseMLButton();
	}


	@Override
	public void stopExperiment() {

		// this instance 
		final ExperimentPresenter currentExperiment = this;

		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {
				// add the experiment in the active experiment list
				injector.getExperimentationManager().removeExperimentFromActiveList(currentExperiment);
				GWT.log("Experiment controller on server is now terminated");
			}

		};

		// make the rpc call
		SecretReservationKey key = new SecretReservationKey();
		key.setSecretReservationKey(secretReservationKeyValue);
		key.setUrnPrefix(urnPrefix);
		List<SecretReservationKey> secretReservationKeys = Arrays.asList(key);
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
	
	public void flashSelectedImage() {
		
		
		// setup callback
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {
				view.setFlashedImageFilename(selectedImage.getFileName());
				imageView.hide();
				startExperimentMessageCollectorTimer();
			}
			
		};
		
		// make the rpc call
		SecretReservationKey key = new SecretReservationKey();
		key.setSecretReservationKey(secretReservationKeyValue);
		key.setUrnPrefix(urnPrefix);
		List<SecretReservationKey> secretReservationKeys = Arrays.asList(key);
		service.flashExperimentImage(secretReservationKeys,selectedImage.getId(),nodeUrns,callback);
	}
	
	@Override
	public void getWiseMLReport() {
		// setup callback
		AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(final String result) {
				GWT.log(result);
				wiseMloutputView.setWiseMLOutput(result);
				wiseMloutputView.show("WiseML report");
			}
			
		};
		
		// make the rpc call
		SecretReservationKey key = new SecretReservationKey();
		key.setSecretReservationKey(secretReservationKeyValue);
		key.setUrnPrefix(urnPrefix);
		List<SecretReservationKey> secretReservationKeys = Arrays.asList(key);
		service.returnExperimentWiseMLReport(secretReservationKeys,callback);
	}

	@Override
	public void showNodeOutput(final String node) {
		outputMap.get(node).show("Output for " + node );
		GWT.log("Showing out for node [" + node +"]");
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
			view.deactivateStopExperimentButton();
			view.deactivateFlashExperimentButton();
		}
	}

	@Override
	public void onExperimentMessageArrival(ExperimentMessageArrivedEvent event) {
		if(event.getSource() == this ){
			if(status == ExperimentStatus.RUNNING) {
				
				// setup callback
				AsyncCallback<ExperimentMessage> callback = new AsyncCallback<ExperimentMessage>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.getMessage());
						MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
					}

					@Override
					public void onSuccess(final ExperimentMessage result) {
						if(result == null) return;
						switch(result.getExperimentMessageType()) {
							case MESSAGE:
								GWT.log("Source : " + result.getSourceNodeID());
								GWT.log("Level : " + result.getLevel());
								GWT.log("Data : " + result.getData());
								outputMap.get(result.getSourceNodeID()).addOutput(result.toString());
								break;
							case NOTIFICATION:
								GWT.log("Notification : " + result.getNotificationText());
								for(String node : outputMap.keySet()) {
									outputMap.get(node).addOutput(result.toString());
								}
								break;
							case STATUS:
								GWT.log("RequestStatus ID : " + result.getRequestStatusID());
								GWT.log("RequestStatus Msg : " + result.getRequestStatusMsg());
								GWT.log("RequestStatus NodeID : " + result.getNodeID());
								GWT.log("RequestStatus value : " + result.getValue());
								outputMap.get(result.getNodeID()).addOutput(result.toString());
								break;
						}
					}
					
				};
				
				// make the rpc call
				SecretReservationKey key = new SecretReservationKey();
				key.setSecretReservationKey(secretReservationKeyValue);
				key.setUrnPrefix(urnPrefix);
				List<SecretReservationKey> secretReservationKeys = Arrays.asList(key);
				service.returnExperimentMessage(secretReservationKeys, callback);
			}
		}
	}

	@Override
	public void onSuccesfullImageUploadEvent(SuccessfulImageUploadEvent event) {
		if(event.getSource() == this) {
			GWT.log("Successfull image upload");
			getAvailableImages();
		}
	}
	
	private void setExperimentData(final ConfidentialReservationData data,
			final String url) {

		fromDate = data.getFrom();
		toDate = data.getTo();
		nodeUrns = data.getNodeURNs();
		secretReservationKeyValue = data.getData().get(0).getSecretReservationKey();
		username = data.getData().get(0).getUsername();
		urnPrefix = data.getData().get(0).getUrnPrefix();
		sessionManagementUrl = url;
		experimentTiming = "-";
		flashedImageFilename = "-";
		status = ExperimentStatus.PENDING;
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		try{
			Checks.ifNull(secretReservationKeyValue, "Experiment key is null");
			Checks.ifNull(fromDate, "Experiment start date is null");
			Checks.ifNull(toDate, "Experiment start date is null");
		}catch(RuntimeException cause){
			MessageBox.error("Error",cause.getMessage(),cause,null);
		}
		
		// initialize outputviews
		for(String nodeUrn : nodeUrns) {
			outputMap.put(nodeUrn, injector.getExperimentOutputView());
		}

		// initialize view
		view.setSecretReservationKey(secretReservationKeyValue);
		view.setUsername(username);
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