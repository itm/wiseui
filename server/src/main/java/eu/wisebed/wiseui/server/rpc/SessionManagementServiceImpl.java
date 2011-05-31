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

import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;

import org.dozer.Mapper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.itm.uniluebeck.tr.wiseml.WiseMLHelper;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.api.SessionManagementService;
import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.shared.dto.Wiseml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SessionManagementServiceImpl extends RemoteServiceServlet implements SessionManagementService {

    private static final long serialVersionUID = 784455164992864141L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManagementServiceImpl.class);
    private final Mapper mapper;

    @Inject
    public SessionManagementServiceImpl(final Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Wiseml getWiseml(final String url) throws WisemlException {
        ifNullOrEmptyArgument(url, "Session Management URL not set!");
        try {
            final eu.wisebed.ns.wiseml._1.Wiseml wiseml = WiseMLHelper.deserialize(getWisemlAsXml(url));
            return mapper.map(wiseml, Wiseml.class);
        } catch (final Exception e) {
            LOGGER.error("Unable to load Wiseml from " + url, e);
            throw new WisemlException("Unable to load Wiseml from " + url, e);
        }
    }

    @Override
    public String getWisemlAsXml(final String url) throws WisemlException {
        ifNullOrEmptyArgument(url, "Session Management URL not set!");
        try {
            final SessionManagement sessionManagement = WSNServiceHelper.getSessionManagementService(url);
            return sessionManagement.getNetwork();
        } catch (final Exception e) {
            LOGGER.error("Unable to load Wiseml from " + url, e);
            throw new WisemlException("Unable to load Wiseml from " + url, e);
        }
    }
}
