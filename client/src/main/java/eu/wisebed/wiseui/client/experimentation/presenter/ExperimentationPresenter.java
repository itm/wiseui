package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter.Button;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter.Callback;
import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;


public class ExperimentationPresenter implements Presenter{

	private final WiseUiGinjector injector;
	private ExperimentationView view;
	private EventBus eventBus;
	private ReservationServiceAsync service;


	@Inject
	public ExperimentationPresenter(final WiseUiGinjector injector,	// TODO should we really inject the inject0r in order to get other stuff like the authentication manager
			final ExperimentationView view,final EventBus eventBus){
		this.injector = injector;
		this.view = view;
		this.eventBus = eventBus;
		view.setPresenter(this);

		// init service
		service = GWT.create(ReservationService.class); // TODO GINject service instead of GWT.create
	}

	public void setView(ExperimentationView view) {
		this.view = view;
	}

	public ExperimentationView getView() {
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void getUserReservations() {


		// TODO this multikey multilogin issue should be rediscussed in my opinion as far as experimentation is concerned
		@SuppressWarnings("unchecked")
		Collection<SecretAuthenticationKey> keys =  injector.getAuthenticationManager().getKeyHash().values();
		SecretAuthenticationKey key = keys.iterator().next();
		if(key == null){
			GWT.log("ExperimentPresenter.getUserReservations : key is null");
			return;
		}
		// dont like it one bit...

		final List<ExperimentView> experimentViews = 
			new ArrayList<ExperimentView>();

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

					experiment.initialize(reservation.getReservationid(),
							reservation.getStartTime(), reservation.getStopTime(),
							reservation.getSensors(),
							reservation.getImageFileName(), 
							reservation.getUrnPrefix(),new Callback() { // TODO GINject callback to experiment presenter

						@Override
						public void onButtonClicked(Button button) {
							switch(button){
							case START:
								experiment.setAsRunningExperiment();
								break;
							case STOP:
								experiment.setAsTerminatedExperiment();
								break;
							case SHOWHIDE:
								experiment.getView().showHideNodeOutput();
								break;
							case CANCEL:
								experiment.setAsCancelledExperiment();
								break;
							}
						} 
					});
					experimentViews.add(experiment.getView());
					view.initView(experimentViews);
				}
			}
		};

		// make the rpc
		service.getUserReservations(key,callback);
	}
}
