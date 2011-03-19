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
	private String sessionmanagementEndpointUrl;
	private List<String> urnPrefixList;
	private boolean isFederated;
	private int testbedID;

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
        
	public int getTestbedID() {
		return testbedID;
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

	public void setTestbedID(int testbedID) {
		this.testbedID = testbedID;
	}


}
