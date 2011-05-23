package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfidentialReservationData extends PublicReservationData implements Serializable{

	private static final long serialVersionUID = 8009880232496996054L;
	
    protected List<Data> data;

    
	public ConfidentialReservationData(){
	}
	
    public ConfidentialReservationData(final Date from, final List<String> nodeURNs, final Date to, final String userData) {
        super.setFrom(from);
        super.setNodeURNs(nodeURNs);
        super.setTo(to);
        super.setUserData(userData);
    }
	    
    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<Data>();
        }
        return this.data;
    }
    
    @Override
    public String toString() {
    	
    	StringBuilder dataString = new StringBuilder();
    	
    	for(Data dt : data) {
    		dataString.append("{SecretReservationKey=" + dt.getSecretReservationKey()
    				+ ", username=" + dt.getUsername()
    				+ ", urnPrefix" + dt.getUrnPrefix() 
    				+ "}");
    	}
        
    	return "ConfidentialReservationData{"
                + "from=" + this.getFrom()
                + ", nodeURNs=" + this.getNodeURNs()
                + ", to=" + this.getTo()
                + ", userData='" + this.getUserData() 
                + "}" + dataString.toString();
               
    }
}
