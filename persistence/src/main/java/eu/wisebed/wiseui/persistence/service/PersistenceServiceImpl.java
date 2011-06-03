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
package eu.wisebed.wiseui.persistence.service;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.persistence.dao.BinaryImageDao;
import eu.wisebed.wiseui.persistence.dao.TestbedConfigurationDao;
import eu.wisebed.wiseui.persistence.domain.BinaryImageBo;
import eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;

/**
 * This service provides transactional methods to load and store WiseUI-relevant objects in a database.
 *
 * @author Soenke Nommensen
 */
@Service("persistenceService")
public class PersistenceServiceImpl implements PersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceImpl.class);

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private TestbedConfigurationDao testbedConfigurationDao;

    @Autowired
    private BinaryImageDao binaryImageDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TestbedConfiguration storeTestbedConfiguration(final TestbedConfiguration dto) {
        ifNullArgument(dto, "Argument 'dto' is null!");

        LOGGER.info("storeTestbedConfiguration( " + dto + " )");

        TestbedConfigurationBo bo = dozerBeanMapper.map(dto, TestbedConfigurationBo.class);
        ifNull(bo, "Error creating TestbedConfigurationBo! (bo is null)");

        testbedConfigurationDao.persist(bo);

        return dozerBeanMapper.map(bo, TestbedConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TestbedConfiguration loadTestbedConfiguration(final Integer id) {
        ifNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("loadTestbedConfiguration( " + id + " )");

        final TestbedConfigurationBo bo = testbedConfigurationDao.findById(id);
        ifNull(bo, "TestbedConfiguration with id '"
                + id
                + "' does not exist!");

        return dozerBeanMapper.map(bo, TestbedConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeTestbedConfiguration(final Integer id) {
        ifNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("removeTestbedConfiguration( " + id + " )");

        final TestbedConfigurationBo bo = testbedConfigurationDao.findById(id);
        ifNull(bo, "TestbedConfiguration with id '"
                + id
                + "' does not exist!");

        testbedConfigurationDao.remove(bo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TestbedConfiguration> loadAllTestbedConfigurations() {
        LOGGER.info("loadAllTestbedConfigurations()");

        final List<TestbedConfiguration> testbedConfigurations = new ArrayList<TestbedConfiguration>();

        for (TestbedConfigurationBo bo : testbedConfigurationDao.findAll()) {
            testbedConfigurations.add(dozerBeanMapper.map(bo, TestbedConfiguration.class));
        }

        return testbedConfigurations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BinaryImage storeBinaryImage(final BinaryImage dto) {
        ifNullArgument(dto, "Argument 'dto' is null!");

        LOGGER.info("storeBinaryImage( " + dto + " )");

        BinaryImageBo bo;
        if (dto.getId() == null) {
            // Create and persist new persistent object
            bo = dozerBeanMapper.map(dto, BinaryImageBo.class);
            binaryImageDao.persist(bo);
        } else {
            // Update existing persistent object
            bo = binaryImageDao.findById(dto.getId());
            if (bo != null) {
                bo = dozerBeanMapper.map(dto, BinaryImageBo.class);
                binaryImageDao.update(bo);
            } else {
                LOGGER.error("BinaryImage with id #" + dto.getId()
                        + " does not exist!");
            }
        }

        ifNull(bo, "Error creating BinaryImageBo! (bo is null)");
        return dozerBeanMapper.map(bo, BinaryImage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BinaryImage loadBinaryImage(final Integer id) {
        ifNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("loadBinaryImage( " + id + " )");

        final BinaryImageBo bo = binaryImageDao.findById(id);
        ifNull(bo, "BinaryImage with id '"
                + id
                + "' does not exist!");

        return dozerBeanMapper.map(bo, BinaryImage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeBinaryImage(final Integer id) {
        ifNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("removeBinaryImage( " + id + " )");

        final BinaryImageBo bo = binaryImageDao.findById(id);
        ifNull(bo, "BinaryImage with id '"
                + id
                + "' does not exist!");

        binaryImageDao.remove(bo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BinaryImage> loadAllBinaryImages() {
        LOGGER.info("loadAllBinaryImages()");

        final List<BinaryImage> binaryImages = new ArrayList<BinaryImage>();

        for (BinaryImageBo bo : binaryImageDao.findAll()) {
            binaryImages.add(dozerBeanMapper.map(bo, BinaryImage.class));
        }

        return binaryImages;
    }

}

