package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

public class Data implements Serializable {

    private static final long serialVersionUID = -448041000886017545L;

    private String urnPrefix;
    private String username;
    private String secretReservationKey;

    public String getUrnPrefix() {
        return urnPrefix;
    }

    public void setUrnPrefix(String value) {
        this.urnPrefix = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public String getSecretReservationKey() {
        return secretReservationKey;
    }

    public void setSecretReservationKey(String value) {
        this.secretReservationKey = value;
    }

    @Override
    public String toString() {
        return "Data{" +
                "urnPrefix='" + urnPrefix + '\'' +
                ", username='" + username + '\'' +
                ", secretReservationKey='" + secretReservationKey + '\'' +
                '}';
    }
}
