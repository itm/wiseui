package eu.wisebed.wiseui.shared.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all information for one testbed configuration entry.
 *
 * @author Soenke Nommensen
 */
public class TestbedConfiguration implements Dto {

    private static final long serialVersionUID = 1721396665761010739L;
    private Integer id;
    private String name;
    private String testbedUrl;
    private String snaaEndpointUrl;
    private String rsEndpointUrl;
    private String sessionmanagementEndpointUrl;
    private List<String> urnPrefixList;
    private boolean isFederated;
    private int testbedId;

    public TestbedConfiguration() {
    }

    public TestbedConfiguration(final String name, final String testbedUrl,
                                final String snaaEndpointUrl, final String rsEndpointUrl,
                                final String sessionmanagementEndpointUrl, final boolean isFederated) {
        this.name = name;
        this.testbedUrl = testbedUrl;
        this.snaaEndpointUrl = snaaEndpointUrl;
        this.rsEndpointUrl = rsEndpointUrl;
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
        this.urnPrefixList = new ArrayList<String>();
        this.isFederated = isFederated;
    }

    public Integer getId() {
        return id;
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

    public String getSessionmanagementEndpointUrl() {
        return sessionmanagementEndpointUrl;
    }

    public List<String> getUrnPrefixList() {
        return urnPrefixList;
    }

    public boolean isFederated() {
        return isFederated;
    }

    public int getTestbedId() {
        return testbedId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTestbedUrl(String testbedUrl) {
        this.testbedUrl = testbedUrl;
    }

    public void setSnaaEndpointUrl(String snaaEndpointUrl) {
        this.snaaEndpointUrl = snaaEndpointUrl;
    }

    public void setRsEndpointUrl(String rsEndpointUrl) {
        this.rsEndpointUrl = rsEndpointUrl;
    }

    public void setSessionmanagementEndpointUrl(String sessionmanagementEndpointUrl) {
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
    }

    public void setUrnPrefixList(List<String> urnPrefixList) {
        this.urnPrefixList = urnPrefixList;
    }

    public void setFederated(boolean isFederated) {
        this.isFederated = isFederated;
    }

    public void setTestbedId(int testbedId) {
        this.testbedId = testbedId;
    }

    @Override
    public String toString() {
        return "TestbedConfiguration{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", testbedUrl='" + testbedUrl + '\''
                + ", snaaEndpointUrl='" + snaaEndpointUrl + '\''
                + ", rsEndpointUrl='" + rsEndpointUrl + '\''
                + ", sessionmanagementEndpointUrl='" + sessionmanagementEndpointUrl + '\''
                + ", urnPrefixList=" + urnPrefixList
                + ", isFederated=" + isFederated
                + ", testbedId=" + testbedId
                + '}';
    }
}
