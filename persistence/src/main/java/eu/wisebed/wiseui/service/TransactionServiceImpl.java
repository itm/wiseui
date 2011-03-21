package eu.wisebed.wiseui.service;

import eu.wisebed.wiseui.api.TransactionService;
import eu.wisebed.wiseui.dao.TestbedConfigurationDao;
import eu.wisebed.wiseui.domain.TestbedConfigurationBo;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Soenke Nommensen
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TestbedConfigurationDao testbedConfigurationDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TestbedConfiguration addTestbedConfiguration(TestbedConfiguration dto) {
        if (dto == null) {
            throw new RuntimeException("dto == null");
        }
        LOGGER.info("addTestbedConfiguration( " + dto + " ) called");
        TestbedConfigurationBo bo;
        if (dto.getId() == null) {
            // Create a new persistent poll object
            bo = new TestbedConfigurationBo(dto);
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

        if (bo == null) {
            throw new RuntimeException("Error creating Bo!");
        }
        return bo.toDto();
    }

    public TestbedConfigurationDao getTestbedConfigurationDao() {
        return testbedConfigurationDao;
    }

    public void setTestbedConfigurationDao(TestbedConfigurationDao testbedConfigurationDao) {
        this.testbedConfigurationDao = testbedConfigurationDao;
    }
}
