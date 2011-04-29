package eu.wisebed.wiseui.server.rpc;

import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmptyArgument;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import eu.wisebed.testbed.api.snaa.helpers.SNAAServiceHelper;
import eu.wisebed.testbed.api.snaa.v1.AuthenticationExceptionException;
import eu.wisebed.testbed.api.snaa.v1.AuthenticationTriple;
import eu.wisebed.testbed.api.snaa.v1.SNAA;
import eu.wisebed.testbed.api.snaa.v1.SNAAExceptionException;
import eu.wisebed.wiseui.api.SNAAService;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Singleton
public class SNAAServiceImpl extends RemoteServiceServlet implements SNAAService {

    private static final long serialVersionUID = -478318843648335352L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SNAAServiceImpl.class);
    private static final String AUTHENTICATION_FAILED = "Authentication failed!";

    private final DozerBeanMapper mapper;

    @Inject
    public SNAAServiceImpl(final DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

    private AuthenticationTriple createTriple(final String urn, final String userName, final String password) {
        final AuthenticationTriple triple = new AuthenticationTriple();

        triple.setUsername(userName);
        triple.setUrnPrefix(urn);
        triple.setPassword(password);

        return triple;
    }

    @Override
    public SecretAuthenticationKey authenticate(final String endpointUrl,
                                                final String urn,
                                                final String userName,
                                                final String password) throws AuthenticationException {
        ifNullOrEmptyArgument(endpointUrl, "SNAA Endpoint URL not set!");
        ifNullOrEmptyArgument(urn, "URN not set!");
        ifNullOrEmptyArgument(userName, "User name not set!");
        ifNullOrEmptyArgument(password, "Password not set!");

        final SNAA snaaService = SNAAServiceHelper.getSNAAService(endpointUrl);
        final AuthenticationTriple triple = createTriple(urn, userName, password);
        eu.wisebed.testbed.api.snaa.v1.SecretAuthenticationKey key;

        try {
            key = snaaService.authenticate(Arrays.asList(triple)).get(0);
        } catch (final AuthenticationExceptionException e) {
            LOGGER.error(AUTHENTICATION_FAILED, e);
            throw new AuthenticationException(AUTHENTICATION_FAILED, e);
        } catch (final SNAAExceptionException e) {
            LOGGER.error(AUTHENTICATION_FAILED, e);
            throw new AuthenticationException(AUTHENTICATION_FAILED, e);
        }

        return mapper.map(key, SecretAuthenticationKey.class);
    }
}
