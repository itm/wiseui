package eu.wisebed.wiseui.server.rpc;

import net.sf.gilead.gwt.PersistentRemoteService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.api.ExperimentationService;

@Singleton
public class ExperimentationServiceImpl extends PersistentRemoteService implements ExperimentationService {

	@Inject
	ExperimentationServiceImpl(){}
}
