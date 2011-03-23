package eu.wisebed.wiseui.server.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class TestbedConfigurationManager {

	/**
	 * Persist all existing testbed infrastructure
	 * @param testbeds
	 */
	public static void saveTestbedInfrastructure(
			final List<TestbedConfiguration> testbeds){
		final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();	
		session.beginTransaction();
		for (int i=0; i<testbeds.size(); i++){
			TestbedConfiguration testbed = new TestbedConfiguration();
			testbed.setName((testbeds.get(i).getName()));
			testbed.setRsEndpointUrl((testbeds.get(i).getRsEndpointUrl()));
			testbed.setSessionmanagementEndpointUrl(
					testbeds.get(i).getSessionmanagementEndpointUrl());
			testbed.setSnaaEndpointUrl(testbeds.get(i).getSnaaEndpointUrl());
			testbed.setTestbedUrl(testbeds.get(i).getTestbedUrl());
			testbed.setUrnPrefixList(testbeds.get(i).getUrnPrefixList());
			testbed.setTestbedID(i+1);
			if(session.get(TestbedConfiguration.class, i+1) == null)
				session.save(testbed);
		}
		session.getTransaction().commit();
	}

	/**
	 * For the first urn prefix exist in the list of urn prefixes given, get the
	 * corresponding testbed object list.
	 * @param urnPrefix, List of urn prefixes
	 * @return beds, the testbeds identified from urnPrefix
	 */
	@SuppressWarnings("unchecked")
	public static List<TestbedConfiguration> fetchTestbedByUrn(
			final List<String> urnPrefix){
		final Session session = WiseUiHibernateUtil.getSessionFactory().
			getCurrentSession();	
		session.beginTransaction();
		List<TestbedConfiguration> beds = new ArrayList<TestbedConfiguration>();
		for (int i=0; i<urnPrefix.size(); i++){
			final String QUERY = "select t from TestbedConfiguration t "
				+ "join t.urnPrefixList ul where ul= :urnprefix";
			final String urn = urnPrefix.get(i);
			Query q = session.createQuery(QUERY).setString("urnprefix", urn);
			List<TestbedConfiguration> tmpBeds = 
				(List<TestbedConfiguration>) q.list();
			for (TestbedConfiguration bed: tmpBeds)
				beds.add(bed);
		}
		if (!beds.isEmpty()){
			for(int i=0, j=0; i<beds.size() && j<urnPrefix.size(); i++){
				List<String> ul = new ArrayList<String>();
				ul.add(urnPrefix.get(j));
				beds.get(i).setUrnPrefixList(ul);
			}
		}
		return beds;
	}

}
