package eu.wisebed.wiseui.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class TestbedConfigurationServiceImpl extends RemoteServiceServlet implements TestbedConfigurationService {

    private static final long serialVersionUID = 5174874924600302509L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationServiceImpl.class);

    private final PersistenceService persistenceService;

    @Inject
    public TestbedConfigurationServiceImpl(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<TestbedConfiguration> getConfigurations() {
        LOGGER.debug("getConfigurations");
        return persistenceService.loadAllTestbedConfigurations();
    }

    @Override
    public void storeConfiguration(final TestbedConfiguration testbedConfiguration) {
        LOGGER.debug("storeConfiguration( " + testbedConfiguration + " )");
        persistenceService.storeTestbedConfiguration(testbedConfiguration);
    }

    @Override
    public void removeConfiguration(final Integer id) {
        LOGGER.debug("removeConfiguration( " + id + " )");
        persistenceService.removeTestbedConfiguration(id);
    }
}
