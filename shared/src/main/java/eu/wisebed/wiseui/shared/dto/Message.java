package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;
import java.util.Date;
import static eu.wisebed.wiseui.shared.util.StringUtils.toHexString;


public class Message implements Serializable {

	private static final long serialVersionUID = 7058795043185920386L;
	protected String sourceNodeId;
    protected Date timestamp;
    protected byte[] binaryData;
    private String secretRecretReservationKey;

    public String getSecretRecretReservationKey() {
		return secretRecretReservationKey;
	}

	public void setSecretRecretReservationKey(String secretRecretReservationKey) {
		this.secretRecretReservationKey = secretRecretReservationKey;
	}
	
	@SuppressWarnings("deprecation")
	public String toString() {
		
		String level = getBinaryData()[1] == 0x00 ? "DEBUG" : "FATAL";
		
		return "[source][" + sourceNodeId +"]" +
			   "[timestamp][" + timestamp.toGMTString() +"]" +
			   "[level][" + level + "]" +
			   "[data][" + toHexString(binaryData) +"]";
	}

	/**
     * Gets the value of the sourceNodeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceNodeId() {
        return sourceNodeId;
    }

    /**
     * Sets the value of the sourceNodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceNodeId(String value) {
        this.sourceNodeId = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(final Date value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the binaryData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaryData() {
        return binaryData;
    }

    /**
     * Sets the value of the binaryData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaryData(byte[] value) {
        this.binaryData = ((byte[]) value);
    }
}

