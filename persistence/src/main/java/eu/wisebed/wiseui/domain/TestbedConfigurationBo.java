package eu.wisebed.wiseui.domain;

import eu.wisebed.wiseui.shared.TestbedConfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
@Entity
public class TestbedConfigurationBo implements Bo {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String testbedUrl;
    private String snaaEndpointUrl;
    private String rsEndpointUrl;
    private String sessionmanagementEndpointUrl;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "testbed_configuration_fk")
    private List<UrnPrefixBo> urnPrefixList;
    private boolean isFederated;
    private int testbedId;

    public TestbedConfigurationBo() {
    }

    public TestbedConfigurationBo(final String name, final String testbedUrl,
                                  final String snaaEndpointUrl, final String rsEndpointUrl,
                                  final String sessionmanagementEndpointUrl, final boolean isFederated) {
        this.name = name;
        this.testbedUrl = testbedUrl;
        this.snaaEndpointUrl = snaaEndpointUrl;
        this.rsEndpointUrl = rsEndpointUrl;
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
        this.urnPrefixList = new ArrayList<UrnPrefixBo>();
        this.isFederated = isFederated;
    }

    public TestbedConfigurationBo(TestbedConfiguration dto) {
        // TODO
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestbedUrl() {
        return testbedUrl;
    }

    public void setTestbedUrl(String testbedUrl) {
        this.testbedUrl = testbedUrl;
    }

    public String getSnaaEndpointUrl() {
        return snaaEndpointUrl;
    }

    public void setSnaaEndpointUrl(String snaaEndpointUrl) {
        this.snaaEndpointUrl = snaaEndpointUrl;
    }

    public String getRsEndpointUrl() {
        return rsEndpointUrl;
    }

    public void setRsEndpointUrl(String rsEndpointUrl) {
        this.rsEndpointUrl = rsEndpointUrl;
    }

    public String getSessionmanagementEndpointUrl() {
        return sessionmanagementEndpointUrl;
    }

    public void setSessionmanagementEndpointUrl(String sessionmanagementEndpointUrl) {
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
    }

    public List<UrnPrefixBo> getUrnPrefixList() {
        return urnPrefixList;
    }

    public void setUrnPrefixList(List<UrnPrefixBo> urnPrefixList) {
        this.urnPrefixList = urnPrefixList;
    }

    public boolean isFederated() {
        return isFederated;
    }

    public void setFederated(boolean federated) {
        isFederated = federated;
    }

    public int getTestbedId() {
        return testbedId;
    }

    public void setTestbedId(int testbedId) {
        this.testbedId = testbedId;
    }

    @Override
    public TestbedConfiguration toDto() {
        TestbedConfiguration dto = new TestbedConfiguration();
        dto.setId(this.id);
        // TODO
        return dto;
    }

    @Override
    public String toString() {
        return "TestbedConfigurationBo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", testbedUrl='" + testbedUrl + '\'' +
                ", snaaEndpointUrl='" + snaaEndpointUrl + '\'' +
                ", rsEndpointUrl='" + rsEndpointUrl + '\'' +
                ", sessionmanagementEndpointUrl='" + sessionmanagementEndpointUrl + '\'' +
                ", urnPrefixList=" + urnPrefixList +
                ", isFederated=" + isFederated +
                ", testbedId=" + testbedId +
                '}';
    }
}
