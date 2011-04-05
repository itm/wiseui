package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@RemoteServiceRelativePath("experimentation.rpc")
public interface ExperimentationService extends RemoteService {
	void bindAndStartExperimentController(final int reservationID) 
		throws ReservationException,ExperimentationException;
	void flashExperimentImage(final int reservationID) 
		throws ReservationException,ExperimentationException;
	ExperimentMessage getNextUndeliveredMessage(final int reservationID)
		throws ExperimentationException;
	void terminateExperiment(final int reservationID) 
		throws ExperimentationException;

}
