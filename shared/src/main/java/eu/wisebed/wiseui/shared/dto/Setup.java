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

import java.io.Serializable;
import java.util.List;

public class Setup implements Serializable {

    private static final long serialVersionUID = -298010549586832532L;
    private Coordinate origin;
    private String coordinateType;
    private String description;
    private List<Node> node;
    private Defaults defaults;

    public Setup() {
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(final Coordinate origin) {
        this.origin = origin;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(final String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(final List<Node> nodes) {
        this.node = nodes;
    }

	public Defaults getDefaults() {
		return defaults;
	}

	public void setDefaults(final Defaults defaults) {
		this.defaults = defaults;
	}
}
