package eu.wisebed.wiseui.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.xstream.XStream;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TestbedConfigurationServiceImpl extends RemoteServiceServlet implements TestbedConfigurationService {

    private static final long serialVersionUID = 5174874924600302509L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationServiceImpl.class);
    private static final String CONFIGURATION_FILE_LOCATION = "testbed-configurations.xml";
    private final String path;
    private PersistenceService persistenceService;

    public TestbedConfigurationServiceImpl() {
        this(CONFIGURATION_FILE_LOCATION);
    }

    public TestbedConfigurationServiceImpl(final String path) {
        this.path = path;
    }

    @Inject
    public TestbedConfigurationServiceImpl(final PersistenceService persistenceService) {
        this(CONFIGURATION_FILE_LOCATION);
        this.persistenceService = persistenceService;
    }

    @Override
    public List<TestbedConfiguration> getConfigurations() throws ConfigurationException {
        try {
            final InputStream is = getClass().getClassLoader().getResourceAsStream(path);

            final XStream xstream = new XStream();
            xstream.alias("configuration", TestbedConfiguration.class);
            xstream.useAttributeFor(TestbedConfiguration.class, "isFederated");
            xstream.addImplicitCollection(TestbedConfiguration.class, "urnPrefixList", "urnPrefix", String.class);

            final List<TestbedConfiguration> configurations = new ArrayList<TestbedConfiguration>();
            ObjectInputStream in;
            in = xstream.createObjectInputStream(is);
            boolean eof = false;
            while (!eof) {
                try {
                    final TestbedConfiguration testbedConfiguration = (TestbedConfiguration) in.readObject();
                    configurations.add(testbedConfiguration);
                } catch (final EOFException e) {
                    eof = true;
                }
            }
            in.close();
            return configurations;
        } catch (final FileNotFoundException e) {
            LOGGER.error("File not found: " + path, e);
            throw new ConfigurationException(e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.error("IO error while processing: " + path, e);
            throw new ConfigurationException(e.getMessage(), e);
        } catch (final ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationException(e.getMessage(), e);
        }
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
