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

public class Capability implements Serializable {

    private static final long serialVersionUID = -5394430386778141537L;

    private String name;
    private Dtypes datatype;
    private Units unit;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Dtypes getDatatype() {
        return datatype;
    }

    public void setDatatype(final Dtypes datatype) {
        this.datatype = datatype;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(final Units unit) {
        this.unit = unit;
    }
}
