package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.shared.wiseml.Wiseml;

@RemoteServiceRelativePath("sessionmanagement.rpc")
public interface SessionManagementService extends RemoteService {

    Wiseml getWiseml(String urn) throws WisemlException;
}
