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
package eu.wisebed.wiseui.server;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.persistence.PersistenceServiceProvider;
import eu.wisebed.wiseui.server.rpc.CalendarServiceImpl;
import eu.wisebed.wiseui.server.rpc.ExperimentationServiceImpl;
import eu.wisebed.wiseui.server.rpc.ImageUploadServiceImpl;
import eu.wisebed.wiseui.server.rpc.ReservationServiceImpl;
import eu.wisebed.wiseui.server.rpc.SNAAServiceImpl;
import eu.wisebed.wiseui.server.rpc.SessionManagementServiceImpl;
import eu.wisebed.wiseui.server.rpc.TestbedConfigurationServiceImpl;
import eu.wisebed.wiseui.server.controller.ExperimentController;


import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WiseUiGuiceServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/wiseui/testbed.rpc").with(TestbedConfigurationServiceImpl.class);
        serve("/wiseui/snaa.rpc").with(SNAAServiceImpl.class);
        serve("/wiseui/sessionmanagement.rpc").with(SessionManagementServiceImpl.class);
        serve("/wiseui/reservation.rpc").with(ReservationServiceImpl.class);
        serve("/wiseui/experimentation.rpc").with(ExperimentationServiceImpl.class);
        serve("/wiseui/calendar.rpc").with(CalendarServiceImpl.class);
        serve("*gupld").with(ImageUploadServiceImpl.class);
    }
    
    @Singleton
    @Provides
    /**
     * Provides an configured instance of the {@link DozerBeanMapper}.
     */
    public Mapper provideMapper() {
        return new DozerBeanMapper(Arrays.asList("server-bean-mappings.xml"));
    }

    @Singleton
    @Provides
    /**
     *  Provides an configured instance of the {@link PersistenceService}.
     */
    public PersistenceService providePersistenceService() {
        return PersistenceServiceProvider.newPersistenceService();
    }
    
    @Singleton
    @Provides
    /**
     * Provides a list of experiment controllers of the {@link ExperimentController}.
     */
    public List<ExperimentController> provideExperimentControllers() {
    	return new ArrayList<ExperimentController>();
    }
    
    @Singleton
    @Provides
    /**
     * Provides a configured instance of the async job observer
     */
    public AsyncJobObserver provideAsyncJobObserver() {
		return new AsyncJobObserver(1, TimeUnit.MINUTES);
    }
}
