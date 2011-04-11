package eu.wisebed.wiseui.server;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.persistence.PersistenceServiceProvider;
import eu.wisebed.wiseui.server.rpc.ExperimentationServiceImpl;
import eu.wisebed.wiseui.server.rpc.ImageUploadServiceImpl;
import eu.wisebed.wiseui.server.rpc.ReservationServiceImpl;
import eu.wisebed.wiseui.server.rpc.SNAAServiceImpl;
import eu.wisebed.wiseui.server.rpc.SessionManagementServiceImpl;
import eu.wisebed.wiseui.server.rpc.TestbedConfigurationServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.Arrays;

public class WiseUiGuiceServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/wiseui/testbed.rpc").with(TestbedConfigurationServiceImpl.class);
        serve("/wiseui/snaa.rpc").with(SNAAServiceImpl.class);
        serve("/wiseui/sessionmanagement.rpc").with(SessionManagementServiceImpl.class);
        serve("/wiseui/reservation.rpc").with(ReservationServiceImpl.class);
        serve("/wiseui/experimentation.rpc").with(ExperimentationServiceImpl.class);
        serve("*gupld").with(ImageUploadServiceImpl.class);
    }

    @Singleton
    @Provides
    /**
     * Provides an configured instance of the {@link DozerBeanMapper}.
     */
    public Mapper provideMapper() {
        return new DozerBeanMapper(Arrays.asList("server-bean-mappings.xml"));
    }

    @Singleton
    @Provides
    /**
     *  Provides an configured instance of the {@link PersistenceService}.
     */
    public PersistenceService providePersistenceService() {
        return PersistenceServiceProvider.newPersistenceService();
    }
}
