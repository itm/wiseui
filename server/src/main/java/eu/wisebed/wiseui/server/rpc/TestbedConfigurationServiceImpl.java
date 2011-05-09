package eu.wisebed.wiseui.server.rpc;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

@Singleton
public class TestbedConfigurationServiceImpl extends RemoteServiceServlet implements TestbedConfigurationService {

    private static final long serialVersionUID = 5174874924600302509L;

    private final PersistenceService persistenceService;

    @Inject
    public TestbedConfigurationServiceImpl(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<TestbedConfiguration> getConfigurations() {
        return persistenceService.loadAllTestbedConfigurations();
    }
    
    @Override
    public TestbedConfiguration storeConfiguration(final TestbedConfiguration testbedConfiguration) {
    	return persistenceService.storeTestbedConfiguration(testbedConfiguration);
    }
    
    @Override
    public void removeConfiguration(final Integer id) {
    	persistenceService.removeTestbedConfiguration(id);
    }

    /**
     * For the urnprefix sent from the client, identify the testbeds logged in.
     *
     * @param urnPrefix, List of urns for testbeds. Only one element for non
     *                   federated testbeds.
     * @return testbeds the testbeds identified.
     */
    public List<TestbedConfiguration> getTestbedLoggedIn(
            final List<String> urnPrefix) {
//    	final List<TestbedConfiguration> testbeds =
//    		TestbedConfigurationManager.fetchTestbedByUrn(urnPrefix);
        // TODO FIXME
        return null;
    }
}
