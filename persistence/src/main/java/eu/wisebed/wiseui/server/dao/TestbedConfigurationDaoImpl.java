package eu.wisebed.wiseui.server.dao;

import eu.wisebed.wiseui.server.domain.TestbedConfigurationBo;
import org.springframework.stereotype.Repository;

/**
 * Data access object for {@link eu.wisebed.wiseui.server.domain.TestbedConfigurationBo} objects.
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
