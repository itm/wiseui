package eu.wisebed.wiseui.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.itm.uniluebeck.tr.wiseml.WiseMLHelper;
import eu.wisebed.wiseui.api.SessionManagementService;
import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.shared.wiseml.Wiseml;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v211.SessionManagement;
import org.dozer.Mapper;

import java.io.PrintWriter;
import java.io.StringWriter;

@Singleton
public class SessionManagementServiceImpl extends RemoteServiceServlet implements SessionManagementService {

    private static final long serialVersionUID = 784455164992864141L;
    private final Mapper mapper;

    @Inject
    public SessionManagementServiceImpl(final Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Wiseml getWiseml(final String url) throws WisemlException {
        try {
            final SessionManagement sessionManagement = WSNServiceHelper.getSessionManagementService(url);
            final eu.wisebed.ns.wiseml._1.Wiseml wiseml = WiseMLHelper.deserialize(sessionManagement.getNetwork());
            return mapper.map(wiseml, Wiseml.class);
        } catch (Exception e) {
            throw new WisemlException("Unable to load Wiseml from " + url, e);
        }
    }
}
