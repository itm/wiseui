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
import eu.wisebed.wiseui.shared.common.Checks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for {@link eu.wisebed.wiseui.persistence.domain.TestbedConfigurationBo} objects.
 *
 * @author Soenke Nommensen
 */
public class TestbedConfigurationDaoImpl implements TestbedConfigurationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestbedConfigurationDaoImpl.class);

    private static final String CONFIGURATION_FILE = "src/main/resources/testbed-configurations.xml";

    private TestbedConfigurationBoWrapper wrapper;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

        private String path;

    public TestbedConfigurationDaoImpl() {
        this(CONFIGURATION_FILE);
    }

    public TestbedConfigurationDaoImpl(final String path) {
        this.path = path;
    }

    @Override
    public void persist(final TestbedConfigurationBo t) {
        Checks.ifNullArgument(t, "testbedConfigurationBo is null");

        wrapper.setTestbedConfigurations(findAll());
        wrapper.addTestbedConfiguration(t);

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
            this.marshaller.marshal(wrapper, new StreamResult(os));
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

    @Override
    public TestbedConfigurationBo update(final TestbedConfigurationBo t) {
        Checks.ifNullArgument(t, "t is null");
        wrapper.setTestbedConfigurations(findAll());
        TestbedConfigurationBo bo = wrapper.getTestbedConfiguration(t.getId());
        Checks.ifNull(bo, "Could not find testbed configuration with id: " + t.getId());
        bo = t;
        return bo;
    }

    @Override
    public void remove(final TestbedConfigurationBo t) {
        // TODO
    }

    @Override
    public TestbedConfigurationBo findById(final Integer id) {
        // TODO
        return null;
    }

    @Override
    public List<TestbedConfigurationBo> findAll() {
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
            StreamSource sr = new StreamSource(is);
            wrapper = (TestbedConfigurationBoWrapper) this.unmarshaller.unmarshal(sr);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return wrapper.getTestbedConfigurations();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public TestbedConfigurationBoWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(TestbedConfigurationBoWrapper wrapper) {
        this.wrapper = wrapper;
    }
}
