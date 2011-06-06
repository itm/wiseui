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
package eu.wisebed.wiseui.persistence;

import eu.wisebed.wiseui.api.PersistenceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Provides an configured instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}.
 *
 * @author Soenke Nommensen
 */
public class PersistenceServiceProvider {

    /**
     * Provides an configured instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}.
     * The instance will be a Singleton as defined in the Spring configuration (this the default for Beans).
     * @return Configured singleton instance of the {@link eu.wisebed.wiseui.persistence.service.PersistenceServiceImpl}
     */
    public static PersistenceService newPersistenceService() {
        final String springConfig = "persistence-spring-config.xml";
        final ApplicationContext springContext = new ClassPathXmlApplicationContext(springConfig);
        return springContext.getBean(PersistenceService.class);
    }
}
