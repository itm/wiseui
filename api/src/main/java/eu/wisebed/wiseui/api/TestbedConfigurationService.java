package eu.wisebed.wiseui.api;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;


@RemoteServiceRelativePath("testbed.rpc")
public interface TestbedConfigurationService extends RemoteService {

    List<TestbedConfiguration> getConfigurations();
    TestbedConfiguration storeConfiguration(TestbedConfiguration testbedConfiguration);
    void removeConfiguration(Integer id);
    List<TestbedConfiguration> getTestbedLoggedIn(final List<String> urnPrefix);
}
