package eu.wisebed.wiseui.server.rpc;

import com.google.inject.Singleton;
import com.thoughtworks.xstream.XStream;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.server.manager.TestbedConfigurationManager;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ConfigurationException;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

@Singleton
public class TestbedConfigurationServiceImpl extends PersistentRemoteService implements TestbedConfigurationService {

    private static final long serialVersionUID = 5174874924600302509L;
    private static final String CONFIGURATION_FILE_LOCATION = "testbed-configurations.xml";
    private final String path;
    private HibernateUtil gileadHibernateUtil = new HibernateUtil();
    public TestbedConfigurationServiceImpl() {
        this(CONFIGURATION_FILE_LOCATION);
    }

    public TestbedConfigurationServiceImpl(final String path) {
        this.path = path;
        // TODO: Move to a common bean manager, maybe when switching to the 
        //		 persistence module
		gileadHibernateUtil.setSessionFactory(
				WiseUiHibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = 
			new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore sps = new StatelessProxyStore();
		sps.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(sps);
		setBeanManager(persistentBeanManager);
    }

    @Override
    public List<TestbedConfiguration> getConfigurations() throws ConfigurationException {
        try {
            final InputStream is =
                    getClass().getClassLoader().getResourceAsStream(path);
            final XStream xstream = new XStream();
            xstream.alias("configuration", TestbedConfiguration.class);
            xstream.useAttributeFor(TestbedConfiguration.class, "isFederated");
            xstream.addImplicitCollection(TestbedConfiguration.class, "urnPrefixList", "urnPrefix", String.class);

            final List<TestbedConfiguration> result = new ArrayList<TestbedConfiguration>();
            ObjectInputStream in;
            in = xstream.createObjectInputStream(is);
            boolean eof = false;
            while (!eof) {
                try {
                    final TestbedConfiguration bed = (TestbedConfiguration) in.readObject();
                    result.add(bed);
                } catch (final EOFException e) {
                    eof = true;
                }
            }
            in.close();
            // Persist testbed infrastructure exist so far.
            TestbedConfigurationManager.saveTestbedInfrastructure(result);
            return result;
        } catch (final FileNotFoundException e) {
            System.err.println(e.getMessage());
            throw new ConfigurationException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new ConfigurationException(e.getMessage(), e);
        } catch (final ClassNotFoundException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }
    
    /**
     * For the urnprefix sent from the client, identify the testbeds logged in.
     * @param urnPrefix, List of urns for testbeds. Only one element for non
     * 		  federated testbeds.
     * @return testbeds the testbeds identified.
     */
    public List<TestbedConfiguration> getTestbedLoggedIn(
    		final List<String> urnPrefix){
    	final List<TestbedConfiguration> testbeds = 
    		TestbedConfigurationManager.fetchTestbedByUrn(urnPrefix);
    	return testbeds;
    }
}
