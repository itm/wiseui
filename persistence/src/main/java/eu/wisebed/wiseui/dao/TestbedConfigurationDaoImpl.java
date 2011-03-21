package eu.wisebed.wiseui.dao;

import eu.wisebed.wiseui.domain.TestbedConfigurationBo;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link eu.wisebed.wiseui.domain.TestbedConfigurationBo} objects.
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
