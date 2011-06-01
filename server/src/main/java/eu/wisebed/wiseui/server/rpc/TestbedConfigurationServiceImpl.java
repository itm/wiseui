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
package eu.wisebed.wiseui.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class TestbedConfigurationServiceImpl extends RemoteServiceServlet implements TestbedConfigurationService {

    private static final long serialVersionUID = 5174874924600302509L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationServiceImpl.class);

    private final PersistenceService persistenceService;
    
    @Inject
    public TestbedConfigurationServiceImpl(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<TestbedConfiguration> getConfigurations() {
        LOGGER.debug("getConfigurations");
        return persistenceService.loadAllTestbedConfigurations();
    }

    @Override
    public void storeConfiguration(final TestbedConfiguration testbedConfiguration) {
        LOGGER.debug("storeConfiguration( " + testbedConfiguration + " )");
        persistenceService.storeTestbedConfiguration(testbedConfiguration);
    }

    @Override
    public void removeConfiguration(final Integer id) {
        LOGGER.debug("removeConfiguration( " + id + " )");
        persistenceService.removeTestbedConfiguration(id);
    }
}
