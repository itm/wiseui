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

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Instance of the {@link PersistenceService} to be tested.
     */
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
    @Ignore
    public void testStoreTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration = createTestbedConfiguration("store-testbed");

        TestbedConfiguration persistedTestbedConfiguration =
                persistenceService.storeTestbedConfiguration(testbedConfiguration);

        assertNotNull("persistedTestbedConfiguration is null", persistedTestbedConfiguration);
        assertNotNull("persistedTestbedConfiguration's ID is null", persistedTestbedConfiguration.getId());

        LOGGER.info(persistedTestbedConfiguration.toString());
    }

    @Test(timeout = 10000)
    @Ignore
    public void testLoadTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration = createTestbedConfiguration("load-testbed");

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

        //assertEquals(loadedTestbedConfiguration, persistedTestbedConfiguration);

        LOGGER.info(loadedTestbedConfiguration.toString());
    }

    @Test(timeout = 30000)
    @Ignore
    public void testLoadAllTestbedConfigurations() {
        final int noOfConfigurations = 5;

        for (int i = 0; i < noOfConfigurations; i++) {
            persistenceService.storeTestbedConfiguration(createTestbedConfiguration("load-all-testbed"));
        }

        assertTrue(persistenceService.loadAllTestbedConfigurations().size() >= noOfConfigurations);
    }

    @Test(timeout = 10000)
    @Ignore
    public void testRemoveTestbedConfiguration() {
        TestbedConfiguration testbedConfiguration =
                persistenceService.storeTestbedConfiguration(createTestbedConfiguration("remove-testbed"));

        assertTrue(persistenceService.loadAllTestbedConfigurations().size() >= 1);
        int size = persistenceService.loadAllTestbedConfigurations().size();

        persistenceService.removeTestbedConfiguration(testbedConfiguration.getId());

        assertTrue(persistenceService.loadAllTestbedConfigurations().size() == (size - 1));
    }

    @Test(timeout = 10000)
    @Ignore
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
    @Ignore
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

        assertEquals(loadedBinaryImage, persistedBinaryImage);

        LOGGER.info(loadedBinaryImage.toString());
    }

    @Test(timeout = 30000)
    @Ignore
    public void testLoadAllBinaryImages() {
        final int noOfConfigurations = 5;

        for (int i = 0; i < noOfConfigurations; i++) {
            persistenceService.storeBinaryImage(createBinaryImage());
        }

        assertTrue(persistenceService.loadAllBinaryImages().size() == noOfConfigurations);
    }

    @Test(timeout = 10000)
    @Ignore
    public void testRemoveBinaryImage() {
        BinaryImage binaryImage =
                persistenceService.storeBinaryImage(createBinaryImage());

        assertTrue(persistenceService.loadAllBinaryImages().size() == 1);

        persistenceService.removeBinaryImage(binaryImage.getId());

        assertTrue(persistenceService.loadAllBinaryImages().isEmpty());
    }

    private TestbedConfiguration createTestbedConfiguration(final String name) {
        TestbedConfiguration testbedConfiguration = new TestbedConfiguration();
        testbedConfiguration.setFederated(true);
        testbedConfiguration.setName(name);
        testbedConfiguration.setRsEndpointUrl(String.format("http://%s.eu/rs", name));
        testbedConfiguration.setSnaaEndpointUrl(String.format("http://%s.eu/snaa", name));
        testbedConfiguration.setSessionmanagementEndpointUrl(String.format("http://%s.eu/session", name));
        testbedConfiguration.setTestbedUrl(String.format("http://%s.eu", name));
        List<String> urns = new ArrayList<String>(1);
        urns.add(String.format("urn:%s1", name));
        urns.add(String.format("urn:%s2", name));
        testbedConfiguration.setUrnPrefixList(urns);
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
