package eu.wisebed.wiseui.persistence;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for all {@link PersistenceService} methods.
 *
 * @author Soenke Nommensen
 */
public class PersistenceServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceTest.class);

    private static final String BINARY_IMAGE_PATH = "src/test/resources/test_image.ihex";

    private PersistenceService persistenceService;

    @Before
    public void setUp() {
        persistenceService = PersistenceServiceProvider.newPersistenceService();
    }

    @After
    public void tearDown() {
        persistenceService = null;
    }

    @Test(timeout = 10000)
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

    @Test(timeout = 30000)
    public void testLoadAllTestbedConfigurations() {
        final int noOfConfigurations = 5;

        for (int i = 0; i < noOfConfigurations; i++) {
            persistenceService.storeTestbedConfiguration(createTestbedConfiguration());
        }

        assertTrue(persistenceService.loadAllTestbedConfigurations().size() == noOfConfigurations);
    }

    @Test(timeout = 10000)
    public void testRemoveTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration =
                persistenceService.storeTestbedConfiguration(createTestbedConfiguration());

        assertTrue(persistenceService.loadAllTestbedConfigurations().size() == 1);

        persistenceService.removeTestbedConfiguration(testbedConfiguration.getId());

        assertTrue(persistenceService.loadAllTestbedConfigurations().isEmpty());
    }

    @Test(timeout = 10000)
    public void testStoreBinaryImage() {
        BinaryImage binaryImage = createBinaryImage();

        assertNotNull(binaryImage.getContent());

        BinaryImage persistedBinaryImage =
                persistenceService.storeBinaryImage(binaryImage);

        assertNotNull("persistedBinaryImage is null", persistedBinaryImage);
        assertNotNull("persistedBinaryImage's ID is null", persistedBinaryImage.getId());

        LOGGER.info(persistedBinaryImage.toString());
    }


    @Test(timeout = 10000)
    public void testLoadBinaryImage() {
        BinaryImage binaryImage = createBinaryImage();

        // Store binary image
        BinaryImage persistedBinaryImage =
                persistenceService.storeBinaryImage(binaryImage);

        assertNotNull("persistedBinaryImage is null", persistedBinaryImage);
        assertNotNull("persistedBinaryImage's ID is null", persistedBinaryImage.getId());

        // Load binary image
        BinaryImage loadedBinaryImage =
                persistenceService.loadBinaryImage(persistedBinaryImage.getId());

        assertNotNull("loadedBinaryImage is null", loadedBinaryImage);
        assertNotNull("loadedBinaryImage's ID is null", loadedBinaryImage.getId());

        assertEquals(loadedBinaryImage.getId(), persistedBinaryImage.getId());

        LOGGER.info(loadedBinaryImage.toString());
    }

    @Test(timeout = 10000)
    public void testRemoveBinaryImage() {
        BinaryImage binaryImage =
                persistenceService.storeBinaryImage(createBinaryImage());

        assertTrue(persistenceService.loadAllBinaryImages().size() == 1);

        persistenceService.removeBinaryImage(binaryImage.getId());

        assertTrue(persistenceService.loadAllBinaryImages().isEmpty());
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

    private BinaryImage createBinaryImage() {
        BinaryImage binaryImage = new BinaryImage();
        binaryImage.setFileName(BINARY_IMAGE_PATH);
        binaryImage.setContentType("sensor-node-image");
        binaryImage.setContent(loadBinaryImageFromDisk(BINARY_IMAGE_PATH));
        return binaryImage;
    }

    private byte[] loadBinaryImageFromDisk(final String path) {
        byte[] bytes = null;
        FileInputStream in = null;

        try {
            in = new FileInputStream(path);
        } catch (final FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            bytes = IOUtils.toByteArray(in);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return bytes;
    }

}
