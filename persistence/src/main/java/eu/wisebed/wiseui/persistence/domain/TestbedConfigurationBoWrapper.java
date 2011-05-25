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
package eu.wisebed.wiseui.persistence.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
public class TestbedConfigurationBoWrapper {

    private List<TestbedConfigurationBo> testbedConfigurations = new ArrayList<TestbedConfigurationBo>();

    private static int ID_GENERATOR = 0;

    public TestbedConfigurationBoWrapper() {
    }

    public List<TestbedConfigurationBo> getTestbedConfigurations() {
        return this.testbedConfigurations;
    }

    public void addTestbedConfiguration(final TestbedConfigurationBo testbedConfigurationBo) {
        if (testbedConfigurationBo.getId() == null) {
            testbedConfigurationBo.setId(ID_GENERATOR++);
        }
        if (containsTestbedConfiguration(testbedConfigurationBo)) {
            removeTestbedConfiguration(testbedConfigurationBo);
        }
        this.testbedConfigurations.add(testbedConfigurationBo);
    }

    public TestbedConfigurationBo getTestbedConfiguration(final Integer id) {
        TestbedConfigurationBo result = new TestbedConfigurationBo();
        for (TestbedConfigurationBo bo : testbedConfigurations) {
            if (id.equals(bo.getId())) {
                result = bo;
                break;
            }
        }
        return result;
    }

    public boolean containsTestbedConfiguration(final Integer id) {
        for (TestbedConfigurationBo bo : testbedConfigurations) {
            if (id.equals(bo.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTestbedConfiguration(final TestbedConfigurationBo bo) {
        return containsTestbedConfiguration(bo.getId());
    }

    public void removeTestbedConfiguration(final TestbedConfigurationBo bo) {
        if (containsTestbedConfiguration(bo.getId())) {
            this.testbedConfigurations.remove(getTestbedConfiguration(bo.getId()));
        }
    }

    public void setTestbedConfigurations(final List<TestbedConfigurationBo> testbedConfigurations) {
        this.testbedConfigurations = testbedConfigurations;
    }
}
