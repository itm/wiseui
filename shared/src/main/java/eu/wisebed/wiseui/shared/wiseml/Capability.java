package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;

public class Capability implements Serializable {

    private static final long serialVersionUID = -5394430386778141537L;

    private String name;

    private Dtypes datatype;

    private Units unit;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Dtypes getDatatype() {
        return datatype;
    }

    public void setDatatype(final Dtypes datatype) {
        this.datatype = datatype;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(final Units unit) {
        this.unit = unit;
    }
}
