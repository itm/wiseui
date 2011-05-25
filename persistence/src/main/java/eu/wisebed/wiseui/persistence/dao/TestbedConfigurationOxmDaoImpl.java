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
package eu.wisebed.wiseui.persistence.dao;

import eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo;
import eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;
import static java.lang.String.format;

/**
 * Data access object for {@link eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo} objects.
 *
 * @author Soenke Nommensen
 */
public class TestbedConfigurationOxmDaoImpl implements TestbedConfigurationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationOxmDaoImpl.class);

    private static final String CONFIGURATION_FILE = "src/main/resources/testbed-configurations.xml";

    private TestbedConfigurationBoWrapper wrapper;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private String path;

    public TestbedConfigurationOxmDaoImpl() {
        this(CONFIGURATION_FILE);
    }

    public TestbedConfigurationOxmDaoImpl(final String path) {
        this.path = path;
    }

    @Override
    public void persist(final TestbedConfigurationBo testbedConfigurationBo) {
        LOGGER.info(format("persist( %s )", testbedConfigurationBo.toString()));
        ifNullArgument(testbedConfigurationBo, "testbedConfigurationBo is null");
        // Reload all testbed configurations
        wrapper.setTestbedConfigurations(findAll());
        // Add new testbed configuration
        wrapper.addTestbedConfiguration(testbedConfigurationBo);
        // Persist in XML file
        writeXmlToFile();
    }

    @Override
    public TestbedConfigurationBo update(final TestbedConfigurationBo testbedConfigurationBo) {
        LOGGER.info(format("update( %s )", testbedConfigurationBo.toString()));
        ifNullArgument(testbedConfigurationBo, "testbedConfigurationBo is null");
        // Reload all testbed configurations
        wrapper.setTestbedConfigurations(findAll());
        // Updates testbed configuration in wrapper object
        wrapper.addTestbedConfiguration(testbedConfigurationBo);
        // Check if update worked
        TestbedConfigurationBo result = wrapper.getTestbedConfiguration(testbedConfigurationBo.getId());
        ifNull(result, "Could not update testbed configuration: " + testbedConfigurationBo);
        // Write changes back to XML file
        writeXmlToFile();
        return result;
    }

    @Override
    public void remove(final TestbedConfigurationBo testbedConfigurationBo) {
        LOGGER.info(format("remove( %s )", testbedConfigurationBo.toString()));
        ifNullArgument(testbedConfigurationBo, "testbedConfigurationBo is null");
        // Reload all testbed configurations
        wrapper.setTestbedConfigurations(findAll());
        // Remove testbed configuration if it exists
        wrapper.removeTestbedConfiguration(testbedConfigurationBo);
        // Write changes back to XML file
        writeXmlToFile();
    }

    @Override
    public TestbedConfigurationBo findById(final Integer id) {
        LOGGER.info(format("findById( %s )", id));
        ifNullArgument(id, "id is null");
        // Reload all testbed configurations
        wrapper.setTestbedConfigurations(findAll());
        // Fetch testbed configuration with corresponding id
        TestbedConfigurationBo testbedConfigurationBo = wrapper.getTestbedConfiguration(id);
        ifNull(testbedConfigurationBo, "testbedConfigurationBo is null");
        return testbedConfigurationBo;
    }

    @Override
    public List<TestbedConfigurationBo> findAll() {
        LOGGER.info("findAll()");
        readXmlFromFile();
        return wrapper.getTestbedConfigurations();
    }

    private void readXmlFromFile() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
            StreamSource source = new StreamSource(file);
            wrapper = (TestbedConfigurationBoWrapper) unmarshaller.unmarshal(source);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    private void writeXmlToFile() {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
            marshaller.marshal(wrapper, new StreamResult(os));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public void setWrapper(TestbedConfigurationBoWrapper wrapper) {
        this.wrapper = wrapper;
    }
}
