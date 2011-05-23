package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = -448041000886017545L;

    private String urnPrefix;
    private String username;
    private String secretReservationKey;
    
    /**
     * Gets the value of the urnPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrnPrefix() {
        return urnPrefix;
    }

    /**
     * Sets the value of the urnPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrnPrefix(String value) {
        this.urnPrefix = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the secretReservationKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretReservationKey() {
        return secretReservationKey;
    }

    /**
     * Sets the value of the secretReservationKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretReservationKey(String value) {
        this.secretReservationKey = value;
    }
}
