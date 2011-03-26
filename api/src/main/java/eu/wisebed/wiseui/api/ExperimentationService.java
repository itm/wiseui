package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@RemoteServiceRelativePath("experimentation.rpc")
public interface ExperimentationService extends RemoteService {
	void bindAndStartExperimentController() 
		throws ReservationException,ExperimentationException;
	void flashUploadedExperimentImage(final int reservationID) 
		throws ReservationException,ExperimentationException;
//ExperimentDetails getNextUndeliveredMessage(final int reservationID) 
//	throws ExpException;
	void terminateExperiment(final int reservationID) 
		throws ExperimentationException;

}
