package eu.wisebed.wiseui.server;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import eu.wisebed.wiseui.server.rpc.SNAAServiceImpl;
import eu.wisebed.wiseui.server.rpc.SessionManagementServiceImpl;
import eu.wisebed.wiseui.server.rpc.TestbedConfigurationServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class WiseUiGuiceServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(Mapper.class).to(DozerBeanMapper.class).in(Singleton.class);

        serve("/wiseui/testbed.rpc").with(TestbedConfigurationServiceImpl.class);
        serve("/wiseui/snaa.rpc").with(SNAAServiceImpl.class);
        serve("/wiseui/sessionmanagement.rpc").with(SessionManagementServiceImpl.class);
    }
}
