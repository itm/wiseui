package eu.wisebed.wiseui.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author Soenke Nommensen
 */
public class TestbedConfiguration extends LightEntity implements Serializable {

	private static final long serialVersionUID = 1721396665761010739L;
	private String name;
	private String testbedUrl;
	private String snaaEndpointUrl;
	private String rsEndpointUrl;
	private String sessionmanagementEndpointUrl;
	private List<String> urnPrefixList = new ArrayList<String>();
	private boolean isFederated;
	private Integer testbedID;

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

	public Integer getTestbedID() {
		return testbedID;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setTestbedUrl(final String testbedUrl) {
		this.testbedUrl = testbedUrl;
	}
	public void setSnaaEndpointUrl(final String snaaEndpointUrl) {
		this.snaaEndpointUrl = snaaEndpointUrl;
	}

	public void setRsEndpointUrl(final String rsEndpointUrl) {
		this.rsEndpointUrl = rsEndpointUrl;
	}

	public void setSessionmanagementEndpointUrl(final String sessionmanagementEndpointUrl) {
		this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
	}

	public void setUrnPrefixList(final List<String> urnPrefixList) {
		this.urnPrefixList = urnPrefixList;
	}

	public void setFederated(final boolean isFederated) {
		this.isFederated = isFederated;
	}

	public void setTestbedID(final Integer testbedID) {
		this.testbedID = testbedID;
	}
}
