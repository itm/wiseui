package eu.wisebed.wiseui.server.persist;

import java.util.List;

import org.hibernate.Session;

import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class TestbedConfigurationPersist {

	/**
	 * Persist all existing testbed infrastructure
	 * @param testbeds
	 */
	public static void saveTestbedInfrastructure(List<TestbedConfiguration> testbeds){
		final Session session = WiseUiHibernateUtil.getSessionFactory().
		getCurrentSession();	
		session.beginTransaction();
		for (int i=0; i<testbeds.size(); i++){
			TestbedConfiguration testbed = new TestbedConfiguration();
			testbed.setName((testbeds.get(i).getName()));
			testbed.setRsEndpointUrl((testbeds.get(i).getRsEndpointUrl()));
			testbed.setSessionmanagementEndpointUrl(testbeds.get(i).getSessionmanagementEndpointUrl());
			testbed.setSnaaEndpointUrl(testbeds.get(i).getSnaaEndpointUrl());
			testbed.setTestbedUrl(testbeds.get(i).getTestbedUrl());
			testbed.setUrnPrefixList(testbeds.get(i).getUrnPrefixList());
			testbed.setTestbedID(i+1);
			session.saveOrUpdate(testbed);
		}
		session.getTransaction().commit();
	}
}
