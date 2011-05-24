/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.persistence;

import eu.wisebed.wiseui.persistence.dao.TestbedConfigurationDao;
import eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Xstream Dao methods.
 *
 * @author Soenke Nommensen
 */
public class PersistenceServiceXstreamTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceXstreamTest.class);
    final String springConfig = "persistence-spring-test-config.xml";
    final ApplicationContext springContext = new ClassPathXmlApplicationContext(springConfig);
    private TestbedConfigurationDao dao = springContext.getBean(TestbedConfigurationDao.class);

    @Test(timeout = 10000)
    public void testStoreTestbedConfiguration() {
        LOGGER.debug("testStoreTestbedConfiguration");

        TestbedConfigurationBo bo1 = createTestbedConfigurationBo("alice", 23);
        TestbedConfigurationBo bo2 = createTestbedConfigurationBo("bob", 42);

        LOGGER.debug(bo1.toString());
        LOGGER.debug(bo2.toString());

        dao.persist(bo1);
        dao.persist(bo2);
    }

    @Test(timeout = 10000)
    @Ignore
    public void testLoadTestbedConfiguration() {
        // TODO
    }

    @Test(timeout = 30000)
    public void testLoadAllTestbedConfigurations() {
        LOGGER.debug("testLoadAllTestbedConfigurations");
        List<TestbedConfigurationBo> list = dao.findAll();
        for (TestbedConfigurationBo bo : list) {
            LOGGER.info(bo.toString());
        }
    }

    @Test(timeout = 10000)
    @Ignore
    public void testRemoveTestbedConfiguration() {
        // TODO
    }

    private TestbedConfigurationBo createTestbedConfigurationBo(final String name, final Integer id) {
        TestbedConfigurationBo bo = new TestbedConfigurationBo();
        bo.setFederated(true);
        bo.setName(name);
        bo.setRsEndpointUrl(String.format("http://%s.eu/rs", name));
        bo.setSnaaEndpointUrl(String.format("http://%s.eu/snaa", name));
        bo.setSessionmanagementEndpointUrl(String.format("http://%s.eu/session", name));
        bo.setTestbedUrl(String.format("http://%s.eu", name));
        List<String> urns = new ArrayList<String>(1);
        urns.add(String.format("urn:%s1", name));
        urns.add(String.format("urn:%s2", name));
        bo.setUrnPrefixList(urns);
        return bo;
    }

}
