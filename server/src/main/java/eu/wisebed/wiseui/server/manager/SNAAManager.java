package eu.wisebed.wiseui.server.manager;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.AuthenticationDetails;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

public class SNAAManager {

	/**
	 * Save new or update an existing user into the persistent store.
	 * @param <code>user</code> , an <code>AuthenticationDetails</code> object.
	 */
	public final static void saveOrUpdateAuthenticationDetails(
			AuthenticationDetails authenticationDetails, String urn){
		final String username = authenticationDetails.getUsername();
		final Session session = WiseUiHibernateUtil.getSessionFactory()
			.getCurrentSession();
		session.beginTransaction();
		final Criteria criteria = session.createCriteria(
				AuthenticationDetails.class)
				.add(Restrictions.eq("username", username));
		AuthenticationDetails auth = (AuthenticationDetails) criteria.uniqueResult();
		
		if(auth !=null){
			Criteria cr = session.createCriteria(SecretAuthenticationKey.class)
				.add(Restrictions.eq("urnPrefix", urn));
			Iterator<SecretAuthenticationKey> it = 
				authenticationDetails.getSecretAuthenticationKeys().iterator();
			SecretAuthenticationKey key = new SecretAuthenticationKey();
			while(it.hasNext()){
				key.setSecretAuthenticationKey(it.next().getSecretAuthenticationKey());
			}
			SecretAuthenticationKey tmpKey = (SecretAuthenticationKey) cr.uniqueResult();
			if(tmpKey !=null){
				session.get(SecretAuthenticationKey.class, tmpKey.getSecretAuthenticationKeyID());
				tmpKey.setSecretAuthenticationKey(key.getSecretAuthenticationKey());
				session.update(tmpKey);
			}else{
				SecretAuthenticationKey newKey = new SecretAuthenticationKey();
				newKey.setSecretAuthenticationKey(key.getSecretAuthenticationKey());
				newKey.setUrnPrefix(urn);
				newKey.setUsername(username);
				auth.getSecretAuthenticationKeys().add(newKey);
				session.update(auth);
			}
		} else{
			session.save(authenticationDetails);
		}
		session.getTransaction().commit();
	}
	
	/**
	 * Given the secret authentication key fetch the corresponding user entity 
	 * from persistent store.
	 * @param <code>secretAuthenticationKey</code>, a secret authentication key.
	 * @return corresponding user entity. 
	 */
	public final static AuthenticationDetails fetchUserBySecretAuthenticationKey(
			String secretAuthenticationKey){
		final Session session = WiseUiHibernateUtil.getSessionFactory()
			.getCurrentSession();
		session.beginTransaction();
		final String QUERY = "select auth from AuthenticationDetails auth inner join " +
				"auth.secretAuthenticationKeys keys where keys.secretAuthenticationKey='" + secretAuthenticationKey + "'";
		Query q = session.createQuery(QUERY);
		return (AuthenticationDetails) q.uniqueResult();
	}
}
