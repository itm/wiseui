package eu.wisebed.wiseui.shared.dto;

import java.util.Date;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
public class PublicReservationData implements Dto {

    private Date from;
    private List<String> nodeURNs;
    private Date to;
    private String userData;

    public PublicReservationData() {
    }

    public PublicReservationData(final Date from, final List<String> nodeURNs, final Date to, final String userData) {
        this.from = from;
        this.nodeURNs = nodeURNs;
        this.to = to;
        this.userData = userData;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(final Date from) {
        this.from = from;
    }

    public List<String> getNodeURNs() {
        return nodeURNs;
    }

    public void setNodeURNs(final List<String> nodeURNs) {
        this.nodeURNs = nodeURNs;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(final String userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "PublicReservationData{"
                + "from=" + from
                + ", nodeURNs=" + nodeURNs
                + ", to=" + to
                + ", userData='" + userData + '\''
                + '}';
    }
}
