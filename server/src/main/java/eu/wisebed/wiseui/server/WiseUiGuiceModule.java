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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.server.controller.WiseUiControllerClient;
import eu.wisebed.wiseui.server.controller.WiseUiListener;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.Arrays;

public class WiseUiGuiceModule extends AbstractModule {

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

    /**
     * Provides a non-singleton instance of an {@link eu.wisebed.wiseui.server.controller.WiseUiListener}.
     */
    @Provides
    public WiseUiListener provideWiseUiListener(final Mapper mapper) {
        return new WiseUiListener(mapper);
    }

    @Provides
    public WiseUiControllerClient provideWiseUiControllerClient(final WiseUiListener listener) {
        return new WiseUiControllerClient(listener);
    }

}
