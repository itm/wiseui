package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfidentialReservationData extends PublicReservationData implements Serializable {

    private static final long serialVersionUID = 8009880232496996054L;

    protected List<Data> data = new ArrayList<Data>();

    public ConfidentialReservationData() {
    }

    public ConfidentialReservationData(final Date from,
                                       final List<String> nodeURNs,
                                       final Date to,
                                       final String userData) {
        super.setFrom(from);
        super.setNodeURNs(nodeURNs);
        super.setTo(to);
        super.setUserData(userData);
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    @Override
    public String toString() {

        StringBuilder dataString = new StringBuilder();

        for (Data dt : data) {
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
