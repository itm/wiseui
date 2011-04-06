package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ConfigurationException;

import java.util.List;


@RemoteServiceRelativePath("testbed.rpc")
public interface TestbedConfigurationService extends RemoteService {

    List<TestbedConfiguration> getConfigurations() throws ConfigurationException;
    List<TestbedConfiguration> getTestbedLoggedIn(final List<String> urnPrefix);
}
