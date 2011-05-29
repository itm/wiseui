package eu.wisebed.wiseui.server;

import java.util.LinkedList;
import java.util.Queue;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import eu.wisebed.wiseui.shared.dto.ExperimentMessage;

public class WiseUiGuiceModule extends AbstractModule{

	@Override
	protected void configure() {		
	}
	
	@Provides
	/**
	 * Provides a queue of {@link ExperimentMessages}.
	 */
	public Queue<ExperimentMessage> provideExperimentMessageQueue() {
		return new LinkedList<ExperimentMessage>();
	}

}
