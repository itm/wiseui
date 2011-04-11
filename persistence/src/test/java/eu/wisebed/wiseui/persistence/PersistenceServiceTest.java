package eu.wisebed.wiseui.persistence;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Soenke Nommensen
 */
public class PersistenceServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceTest.class);

    private PersistenceService persistenceService;

    @Before
    public void setUp() {
        persistenceService = PersistenceServiceProvider.newPersistenceService();
    }

    @After
    public void tearDown() {
        persistenceService = null;
    }

    @Test(timeout = 5000)
    public void testStoreTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration = createTestbedConfiguration();

        TestbedConfiguration persistedTestbedConfiguration =
                persistenceService.storeTestbedConfiguration(testbedConfiguration);

        assertNotNull("persistedTestbedConfiguration is null", persistedTestbedConfiguration);
        assertNotNull("persistedTestbedConfiguration's ID is null", persistedTestbedConfiguration.getId());

        LOGGER.info(persistedTestbedConfiguration.toString());
    }

    @Test(timeout = 10000)
    public void testLoadTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration = createTestbedConfiguration();

        // Store testbed configuration
        TestbedConfiguration persistedTestbedConfiguration =
                persistenceService.storeTestbedConfiguration(testbedConfiguration);

        assertNotNull("persistedTestbedConfiguration is null", persistedTestbedConfiguration);
        assertNotNull("persistedTestbedConfiguration's ID is null", persistedTestbedConfiguration.getId());

        // Load testbed configuration
        TestbedConfiguration loadedTestbedConfiguration =
                persistenceService.loadTestbedConfiguration(persistedTestbedConfiguration.getId());

        assertNotNull("loadedTestbedConfiguration is null", loadedTestbedConfiguration);
        assertNotNull("loadedTestbedConfiguration's ID is null", loadedTestbedConfiguration.getId());

        assertEquals(loadedTestbedConfiguration.getId(), persistedTestbedConfiguration.getId());
        assertEquals(loadedTestbedConfiguration, persistedTestbedConfiguration);

        LOGGER.info(loadedTestbedConfiguration.toString());
    }

    private TestbedConfiguration createTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration = new TestbedConfiguration();
        testbedConfiguration.setFederated(false);
        testbedConfiguration.setName("Test Configuration");
        testbedConfiguration.setRsEndpointUrl("/RS");
        testbedConfiguration.setSessionmanagementEndpointUrl("/SessionManagement");
        testbedConfiguration.setSnaaEndpointUrl("/SNAA");
        testbedConfiguration.setTestbedUrl("http://www.testbed.de");
        testbedConfiguration.setUrnPrefixList(new ArrayList<String>());
        return testbedConfiguration;
    }
}
