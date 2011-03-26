package eu.wisebed.wiseui.server.rpc;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@Singleton
public class ExperimentationServiceImpl extends PersistentRemoteService implements ExperimentationService {

	private static final long serialVersionUID = -6301493806193636782L;

	private HibernateUtil gileadHibernateUtil = new HibernateUtil();

	@Inject
	ExperimentationServiceImpl(){
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
	public void bindAndStartExperimentController() throws ReservationException,
			ExperimentationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flashUploadedExperimentImage(int reservationID)
			throws ReservationException, ExperimentationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminateExperiment(int reservationID)
			throws ExperimentationException {
		// TODO Auto-generated method stub
		
	}
}
