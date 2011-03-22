package eu.wisebed.wiseui.domain;

import eu.wisebed.wiseui.shared.UrnPrefix;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Urn prefix business object (BO) for Hibernate persistence. Part of {@link TestbedConfigurationBo}.
 *
 * @author Soenke Nommensen
 */
@Entity
public class UrnPrefixBo implements Bo {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "testbed_conf_fk")
    private TestbedConfigurationBo testbedConfigurationBo;

    public UrnPrefixBo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TestbedConfigurationBo getTestbedConfigurationBo() {
        return testbedConfigurationBo;
    }

    public void setTestbedConfigurationBo(TestbedConfigurationBo testbedConfigurationBo) {
        this.testbedConfigurationBo = testbedConfigurationBo;
    }

    @Override
    public String toString() {
        return "UrnPrefixBo{"
                + "id=" + id
                + '}';
    }
}
