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
