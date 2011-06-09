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
package eu.wisebed.wiseui.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import eu.wisebed.wiseml.controller.WiseMLController;
import eu.wisebed.wiseml.model.WiseML;
import eu.wisebed.wiseml.model.scenario.Timestamp;
import eu.wisebed.wiseml.model.setup.Setup;
import eu.wisebed.wiseml.model.trace.Trace;
import eu.wisebed.wiseui.shared.dto.Message;
import eu.wisebed.wiseui.shared.dto.RequestStatus;

public class WiseUiGuiceModule extends AbstractModule{

	@Override
	protected void configure() {		
	}
	
    @Singleton
    @Provides
    /**
     * Provides an configured instance of the {@link DozerBeanMapper}.
     */
    public Mapper provideMapper() {
        return new DozerBeanMapper(Arrays.asList("server-bean-mappings.xml"));
    }
	
	@Provides
	/**
	 * Provides a queue of {@link Message}.
	 */
	public Queue<Message> provideMessageQueue() {
		return new LinkedList<Message>();
	}
	
	@Provides
	/**
	 * Provides a queue of {@link RequestStatus}.
	 */
	public Queue<RequestStatus> provideRequestStatusQueue() {
		return new LinkedList<RequestStatus>();
	}
	
	@Provides
	/**
	 * Provides a queue of {@link String}.
	 */
	public Queue<String> provideNotificationQueue() {
		return new LinkedList<String>();
	}
	
	@Provides
	/**
	 * Provides a WiseML model
	 */
	public WiseML provideWiseML() {
		return new WiseML();
	}
	
	@Provides
	/**
	 * Provides a WiseML Setup
	 */
	public Setup provideSetup() {
		return new Setup();
	}
	
	@Provides
	/**
	 * Provides a Timestamp List
	 */
	public List<Timestamp> provideTimestampList() {
		return new ArrayList<Timestamp>();
	}
	
	@Provides
	/**
	 * Provides a WiseML Trace
	 */
	public Trace provideTrace() {
		return new Trace();
	}
	
	@Provides
	/**
	 * Provides a WiseMLController
	 */
	public WiseMLController provideWiseMLController() {
		return new WiseMLController();
	}
}
