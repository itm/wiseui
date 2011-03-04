package eu.wisebed.wiseui.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
public class TestbedConfiguration implements Serializable {

    private static final long serialVersionUID = 1721396665761010739L;
    private String name;
    private String testbedUrl;
    private String snaaEndpointUrl;
    private String rsEndpointUrl;
    private String sessionmanagementEndointUrl;
    private List<String> urnPrefixList;
    private boolean isFederated;

    public TestbedConfiguration() {
    }

    public TestbedConfiguration(final String name, final String testbedUrl,
                                final String snaaEndpointUrl, final String rsEndpointUrl,
                                final String sessionmanagementEndointUrl, final boolean isFederated) {
        this.name = name;
        this.testbedUrl = testbedUrl;
        this.snaaEndpointUrl = snaaEndpointUrl;
        this.rsEndpointUrl = rsEndpointUrl;
        this.sessionmanagementEndointUrl = sessionmanagementEndointUrl;
        this.urnPrefixList = new ArrayList<String>();
        this.isFederated = isFederated;
    }

    public String getName() {
        return name;
    }

    public String getTestbedUrl() {
        return testbedUrl;
    }

    public String getSnaaEndpointUrl() {
        return snaaEndpointUrl;
    }

    public String getRsEndpointUrl() {
        return rsEndpointUrl;
    }

    public String getSessionmanagementEndointUrl() {
        return sessionmanagementEndointUrl;
    }

    public List<String> getUrnPrefixList() {
        return urnPrefixList;
    }

    public boolean isFederated() {
        return isFederated;
    }
}
