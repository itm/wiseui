package eu.wisebed.wiseui.domain;

import eu.wisebed.wiseui.shared.UrnPrefix;
import eu.wisebed.wiseui.shared.dto.Dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 *
 * @author Soenke Nommensen
 */
@Entity
public class UrnPrefixBo implements Bo {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "testbed_configuration_fk")
    private TestbedConfigurationBo testbedConfigurationBo;

    public UrnPrefixBo() {
    }

    public UrnPrefixBo(UrnPrefix dto) {
        // TODO
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UrnPrefixBo{"
                + "id=" + id
                + '}';
    }

    @Override
    public UrnPrefix toDto() {
        UrnPrefix dto = new UrnPrefix();
        dto.setId(this.id);
        return dto;
    }
}
