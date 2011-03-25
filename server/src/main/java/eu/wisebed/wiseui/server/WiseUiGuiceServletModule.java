package eu.wisebed.wiseui.server;

import java.util.Arrays;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import eu.wisebed.wiseui.server.rpc.ImageUploadServiceImpl;
import eu.wisebed.wiseui.server.rpc.ReservationServiceImpl;
import eu.wisebed.wiseui.server.rpc.SNAAServiceImpl;
import eu.wisebed.wiseui.server.rpc.SessionManagementServiceImpl;
import eu.wisebed.wiseui.server.rpc.TestbedConfigurationServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class WiseUiGuiceServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/wiseui/testbed.rpc").with(TestbedConfigurationServiceImpl.class);
        serve("/wiseui/snaa.rpc").with(SNAAServiceImpl.class);
        serve("/wiseui/sessionmanagement.rpc").with(SessionManagementServiceImpl.class);
        serve("/wiseui/reservation.rpc").with(ReservationServiceImpl.class);
        serve("*gupld").with(ImageUploadServiceImpl.class);
    }
    
    @Singleton
    @Provides
    public Mapper provideMapper() {
    	return new DozerBeanMapper(Arrays.asList("dozer-bean-mappings.xml"));
    }
}
