/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.shared.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all information for one testbed configuration entry.
 *
 * @author Soenke Nommensen
 */
public class TestbedConfiguration implements Dto {

    private static final long serialVersionUID = 1721396665761010739L;

    private Integer id;
    private String name;
    private String testbedUrl;
    private String snaaEndpointUrl;
    private String rsEndpointUrl;
    private String sessionmanagementEndpointUrl;
    private List<String> urnPrefixList = new ArrayList<String>();
    private boolean isFederated;

    public TestbedConfiguration() {
    }

    public TestbedConfiguration(final String name,
                                final String testbedUrl,
                                final String snaaEndpointUrl,
                                final String rsEndpointUrl,
                                final String sessionmanagementEndpointUrl,
                                final boolean isFederated) {
        this.name = name;
        this.testbedUrl = testbedUrl;
        this.snaaEndpointUrl = snaaEndpointUrl;
        this.rsEndpointUrl = rsEndpointUrl;
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
        this.isFederated = isFederated;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTestbedUrl() {
        return testbedUrl;
    }

    public String getSnaaEndpointUrl() {
        return snaaEndpointUrl;
    }

    public String getRsEndpointUrl() {
        return rsEndpointUrl;
    }

    public String getSessionmanagementEndpointUrl() {
        return sessionmanagementEndpointUrl;
    }

    public List<String> getUrnPrefixList() {
        return urnPrefixList;
    }

    public boolean isFederated() {
        return isFederated;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setTestbedUrl(final String testbedUrl) {
        this.testbedUrl = testbedUrl;
    }

    public void setSnaaEndpointUrl(final String snaaEndpointUrl) {
        this.snaaEndpointUrl = snaaEndpointUrl;
    }

    public void setRsEndpointUrl(final String rsEndpointUrl) {
        this.rsEndpointUrl = rsEndpointUrl;
    }

    public void setSessionmanagementEndpointUrl(final String sessionmanagementEndpointUrl) {
        this.sessionmanagementEndpointUrl = sessionmanagementEndpointUrl;
    }

    public void setUrnPrefixList(final List<String> urnPrefixList) {
        this.urnPrefixList = urnPrefixList;
    }

    public void setFederated(final boolean isFederated) {
        this.isFederated = isFederated;
    }

    @Override
    public String toString() {
        return "TestbedConfiguration{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", testbedUrl='" + testbedUrl + '\''
                + ", snaaEndpointUrl='" + snaaEndpointUrl + '\''
                + ", rsEndpointUrl='" + rsEndpointUrl + '\''
                + ", sessionmanagementEndpointUrl='" + sessionmanagementEndpointUrl + '\''
//                + ", urnPrefixList=" + urnPrefixList
                + ", isFederated=" + isFederated
                + '}';
    }

//    @Override
//    public boolean equals(final Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        TestbedConfiguration that = (TestbedConfiguration) o;
//
//        if (isFederated != that.isFederated) return false;
//        if (testbedId != that.testbedId) return false;
//        if (id != null ? !id.equals(that.id) : that.id != null) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (rsEndpointUrl != null ? !rsEndpointUrl.equals(that.rsEndpointUrl) : that.rsEndpointUrl != null)
//            return false;
//        if (sessionmanagementEndpointUrl != null ? !sessionmanagementEndpointUrl.equals(that.sessionmanagementEndpointUrl) : that.sessionmanagementEndpointUrl != null)
//            return false;
//        if (snaaEndpointUrl != null ? !snaaEndpointUrl.equals(that.snaaEndpointUrl) : that.snaaEndpointUrl != null)
//            return false;
//        if (testbedUrl != null ? !testbedUrl.equals(that.testbedUrl) : that.testbedUrl != null) return false;
//        if (urnPrefixList != null ? !urnPrefixList.equals(that.urnPrefixList) : that.urnPrefixList != null)
//            return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id != null ? id.hashCode() : 0;
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (testbedUrl != null ? testbedUrl.hashCode() : 0);
//        result = 31 * result + (snaaEndpointUrl != null ? snaaEndpointUrl.hashCode() : 0);
//        result = 31 * result + (rsEndpointUrl != null ? rsEndpointUrl.hashCode() : 0);
//        result = 31 * result + (sessionmanagementEndpointUrl != null ? sessionmanagementEndpointUrl.hashCode() : 0);
//        result = 31 * result + (urnPrefixList != null ? urnPrefixList.hashCode() : 0);
//        result = 31 * result + (isFederated ? 1 : 0);
//        result = 31 * result + testbedId;
//        return result;
//    }
}
