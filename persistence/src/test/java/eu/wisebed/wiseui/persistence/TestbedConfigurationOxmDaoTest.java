/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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

import static org.junit.Assert.assertNotNull;

/**
 * Tests for the OXM DAO methods.
 *
 * @author Soenke Nommensen
 */
public class TestbedConfigurationOxmDaoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationOxmDaoTest.class);
    final String springConfig = "persistence-spring-test-config.xml";
    final ApplicationContext springContext = new ClassPathXmlApplicationContext(springConfig);
    private TestbedConfigurationDao dao = springContext.getBean(TestbedConfigurationDao.class);

    @Test(timeout = 10000)
    @Ignore
    public void testStoreTestbedConfiguration() {
        LOGGER.debug("testStoreTestbedConfiguration()");

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
        LOGGER.debug("testLoadTestbedConfiguration()");

        TestbedConfigurationBo bo = dao.findById(23);
        assertNotNull(bo);

        LOGGER.debug(bo.toString());
    }

    @Test(timeout = 30000)
    @Ignore
    public void testLoadAllTestbedConfigurations() {
        LOGGER.debug("testLoadAllTestbedConfigurations()");
        List<TestbedConfigurationBo> list = dao.findAll();
        for (TestbedConfigurationBo bo : list) {
            LOGGER.info(bo.toString());
        }
    }

    @Test(timeout = 10000)
    @Ignore
    public void testRemoveTestbedConfiguration() {
        LOGGER.debug("testRemoveTestbedConfiguration()");

        TestbedConfigurationBo bo = dao.findById(42);
        assertNotNull(bo);

        dao.remove(bo);

        List<TestbedConfigurationBo> list = dao.findAll();
        for (TestbedConfigurationBo t : list) {
            LOGGER.info(t.toString());
        }
    }

    private TestbedConfigurationBo createTestbedConfigurationBo(final String name, final Integer id) {
        TestbedConfigurationBo bo = new TestbedConfigurationBo();
        bo.setId(id);
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
