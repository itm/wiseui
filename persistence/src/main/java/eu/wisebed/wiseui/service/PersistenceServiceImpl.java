package eu.wisebed.wiseui.service;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.dao.TestbedConfigurationDao;
import eu.wisebed.wiseui.domain.TestbedConfigurationBo;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
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
@Service
public class PersistenceServiceImpl implements PersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceImpl.class);

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private TestbedConfigurationDao testbedConfigurationDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TestbedConfiguration storeTestbedConfiguration(TestbedConfiguration dto) {
        Preconditions.notNullArgument(dto, "Argument 'dto' is null!");

        LOGGER.info("storeTestbedConfiguration( " + dto + " )");

        TestbedConfigurationBo bo;
        if (dto.getId() == null) {
            // Create a new persistent object
            bo = dozerBeanMapper.map(dto, TestbedConfigurationBo.class);
            testbedConfigurationDao.add(bo);
        } else {
            // Update an existing persistent object
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
    @Transactional
    public TestbedConfiguration loadTestbedConfiguration(Integer id) {
        Preconditions.notNullArgument(id, "Argument 'id' is null!");

        LOGGER.info("loadTestbedConfiguration( " + id + " )");

        TestbedConfigurationBo bo = testbedConfigurationDao.findById(id);
        Preconditions.notNull(bo, "TestbedConfiguration with id #"
                + id
                + " does not exist!");
        return dozerBeanMapper.map(bo, TestbedConfiguration.class);
    }
}

