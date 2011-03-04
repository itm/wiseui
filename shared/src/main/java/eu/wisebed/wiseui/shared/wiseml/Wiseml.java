package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;

public class Wiseml implements Serializable {

    private static final long serialVersionUID = 5604418503671696867L;
    private String version;
    private Setup setup;

    public Wiseml() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public Setup getSetup() {
        return setup;
    }

    public void setSetup(final Setup setup) {
        this.setup = setup;
    }
}
