package eu.wisebed.wiseui.persistence.dao;

import eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo} objects.
 *
 * @author Soenke Nommensen
 */
@Repository
public class TestbedConfigurationDaoImpl extends AbstractDaoImpl<TestbedConfigurationBo>
        implements TestbedConfigurationDao {

    public TestbedConfigurationDaoImpl() {
        super(TestbedConfigurationBo.class);
    }
}
