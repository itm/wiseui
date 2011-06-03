/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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

import java.io.Serializable;
import java.util.List;

/**
 * Holds all information about one node in the context of a {@link TestbedConfiguration}.
 *
 * @author Malte Legenhausen
 */
public class Node implements Serializable {

    private static final long serialVersionUID = -7074948156263912998L;
    private String id;
    private Coordinate position;
    private Boolean gateway;
    private String programDetails;
    private String nodeType;
    private String description;
    private List<Capability> capability;

    public Node() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(final Coordinate position) {
        this.position = position;
    }

    public Boolean isGateway() {
        return gateway;
    }

    public void setGateway(final Boolean gateway) {
        this.gateway = gateway;
    }

    public String getProgramDetails() {
        return programDetails;
    }

    public void setProgramDetails(final String programDetails) {
        this.programDetails = programDetails;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Capability> getCapability() {
        return capability;
    }

    public void setCapability(final List<Capability> capability) {
        this.capability = capability;
    }
}
