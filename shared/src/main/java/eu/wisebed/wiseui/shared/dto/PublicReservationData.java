/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.shared.dto;

import java.util.Date;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
public class PublicReservationData implements Dto {

	private static final long serialVersionUID = -3054098645192693672L;
	
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
