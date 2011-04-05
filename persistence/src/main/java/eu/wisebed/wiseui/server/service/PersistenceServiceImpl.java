package eu.wisebed.wiseui.server.service;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.server.dao.BinaryImageDao;
import eu.wisebed.wiseui.server.dao.TestbedConfigurationDao;
import eu.wisebed.wiseui.server.domain.BinaryImageBo;
import eu.wisebed.wiseui.server.domain.TestbedConfigurationBo;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.common.Preconditions;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides transactional methods to load and store WiseUi-relevant objects in a database.
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
        Preconditions.notNullArgument(dto, "Argument 'dto' is null!");

        LOGGER.info("storeTestbedConfiguration( " + dto + " )");

        TestbedConfigurationBo bo;
        if (dto.getId() == null) {
            // Create and persist new persistent object
            bo = dozerBeanMapper.map(dto, TestbedConfigurationBo.class);
            testbedConfigurationDao.persist(bo);
        } else {
            // Update existing persistent object
            bo = testbedConfigurationDao.findById(dto.getId());
            if (bo != null) {
                testbedConfigurationDao.update(bo);
            } else {
                LOGGER.error("TestbedConfiguration with id #" + dto.getId()
                        + " does not exist!");
            }
        }

        Preconditions.notNull(bo, "Error creating TestbedConfigurationBo! (bo is null)");
        return dozerBeanMapper.map(bo, TestbedConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TestbedConfiguration loadTestbedConfiguration(final Integer id) {
        Preconditions.notNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("loadTestbedConfiguration( " + id + " )");

        final TestbedConfigurationBo bo = testbedConfigurationDao.findById(id);
        Preconditions.notNull(bo, "TestbedConfiguration with id '"
                + id
                + "' does not exist!");
        return dozerBeanMapper.map(bo, TestbedConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BinaryImage storeBinaryImage(final BinaryImage dto) {
        Preconditions.notNullArgument(dto, "Argument 'dto' is null!");

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
                binaryImageDao.update(bo);
            } else {
                LOGGER.error("BinaryImage with id #" + dto.getId()
                        + " does not exist!");
            }
        }

        Preconditions.notNull(bo, "Error creating BinaryImageBo! (bo is null)");
        return dozerBeanMapper.map(bo, BinaryImage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BinaryImage loadBinaryImage(Integer id) {
        Preconditions.notNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("loadBinaryImage( " + id + " )");

        final BinaryImageBo bo = binaryImageDao.findById(id);
        Preconditions.notNull(bo, "BinaryImage with id '"
                + id
                + "' does not exist!");
        return dozerBeanMapper.map(bo, BinaryImage.class);
    }


    /**
     * Used by Spring inject {@link DozerBeanMapper}.
     * @param dozerBeanMapper The property be injected.
     */
    public void setDozerBeanMapper(final DozerBeanMapper dozerBeanMapper) {
       this.dozerBeanMapper = dozerBeanMapper;
    }

    /**
     * Used by Spring inject {@link TestbedConfigurationDao}.
     * @param testbedConfigurationDao The property be injected.
     */
    public void setTestbedConfigurationDao(final TestbedConfigurationDao testbedConfigurationDao) {
        this.testbedConfigurationDao = testbedConfigurationDao;
    }

    /**
     * Used by Spring inject {@link BinaryImageDao}.
     * @param binaryImageDao The property be injected.
     */
    public void setBinaryImageDao(BinaryImageDao binaryImageDao) {
        this.binaryImageDao = binaryImageDao;
    }
}

