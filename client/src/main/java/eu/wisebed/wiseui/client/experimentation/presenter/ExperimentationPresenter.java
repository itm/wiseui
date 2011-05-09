package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;


public class ExperimentationPresenter implements Presenter{

	private final WiseUiGinjector injector;
	private ExperimentationView view;
	private ReservationServiceAsync service;


	@Inject
	public ExperimentationPresenter(final WiseUiGinjector injector,
			final ExperimentationView view,final EventBus eventBus,
			final ReservationServiceAsync service){
		this.injector = injector;
		
		this.view = view;
		view.setPresenter(this);

		this.service = service;
	}

	public void setView(final ExperimentationView view) {
		this.view = view;
	}

	public ExperimentationView getView() {
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void getUserReservations() {
		
		// Let's see ...
		// 
		// Let the user being authenticated in N testbeds and has already made M reservations
		// 
		// 1. Retrieve testbed credentials of user that includes both N s.a.k and M s.r.k
		// 	  (Retrieved from local storage)
		// 2. With those credentials retrieve users reservations
		// 	  (Look up on the remote services)
		// 3. Bring them back and print them on the views
		
		final List<ExperimentView> experimentViews = 
			new ArrayList<ExperimentView>();
		
//		// TODO this multikey multilogin issue should be rediscussed in my opinion as far as experimentation is concerned
//		@SuppressWarnings("unchecked")
//		Collection<SecretAuthenticationKey> keys =  injector.getAuthenticationManager().getKeyHash().values();
//		SecretAuthenticationKey key = keys.iterator().next();
//		if(key == null){
//			GWT.log("ExperimentPresenter.getUserReservations : key is null");
//			return;
//		}
//		// dont like it one bit...

		
		// setup rpc callback
		AsyncCallback<List<ReservationDetails>> callback = 
			new AsyncCallback<List<ReservationDetails>>(){

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ExperimentationException) {
					GWT.log(caught.getMessage());
				}				
			}

			@Override
			public void onSuccess(List<ReservationDetails> result) {

				if(result == null) {
					MessageBox.info("No reservations found", "No reservations found", null);
					return;
				}

				for(ReservationDetails reservation : result) {
					final ExperimentPresenter experiment = injector.getExperimentPresenter();

                    // TODO FIXME
//					experiment.initialize(reservation.getReservationid(),
//							reservation.getStartTime(), reservation.getStopTime(),
//							reservation.getSensors(),
//							reservation.getImageFileName(),
//							reservation.getUrnPrefix(),new Callback() { // TODO GINject callback to experiment presenter
//
//						@Override
//						public void onButtonClicked(Button button) {
//							switch(button){
//							case START:
//								experiment.setAsRunningExperiment();
//								break;
//							case STOP:
//								experiment.setAsTerminatedExperiment();
//								break;
//							case SHOWHIDE:
//								experiment.getView().showHideNodeOutput();
//								break;
//							case CANCEL:
//								experiment.setAsCancelledExperiment();
//								break;
//							}
//						}
//					});
					experimentViews.add(experiment.getView());
					
					// fist clear the experiment container and then add the new panels
					view.resetExperimentContainer();
					view.initView(experimentViews);
				}
			}
		};

		// make the rpc
//		service.getUserReservations(key, callback);
	}
}
