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
package eu.wisebed.wiseui.api;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.BinaryImage;

import java.util.List;

/**
 * @author Soenke Nommensen
 */
public interface PersistenceService {

    /**
     * Stores a {@link TestbedConfiguration} in the database.
     *
     * @param testbedConfiguration The {@link TestbedConfiguration} to be stored
     * @return The persisted instance (id should be not null) of the given {@link TestbedConfiguration}
     */
    TestbedConfiguration storeTestbedConfiguration(TestbedConfiguration testbedConfiguration);

    /**
     * Loads a {@link TestbedConfiguration} from the database.
     *
     * @param id The ID of the {@link TestbedConfiguration} to be loaded
     * @return The loaded instance of {@link TestbedConfiguration} for the given ID.
     */
    TestbedConfiguration loadTestbedConfiguration(Integer id);

    /**
     * Removes the specified {@link TestbedConfiguration} from the database.
     *
     * @param id The ID of the {@link TestbedConfiguration} to be removed
     */
    void removeTestbedConfiguration(Integer id);

    /**
     * Loads all available {@link TestbedConfiguration}s from the database.
     * Returns an empty list, if no {@link TestbedConfiguration}s could be found.
     *
     * @return A list with all {@link TestbedConfiguration}s loaded from the database
     */
    List<TestbedConfiguration> loadAllTestbedConfigurations();

    /**
     * Stores a {@link BinaryImage} in the database.
     *
     * @param binaryImage The {@link BinaryImage} to be stored
     * @return The persisted instance (id should be not null) of the given {@link BinaryImage}
     */
    BinaryImage storeBinaryImage(BinaryImage binaryImage);

    /**
     * Loads a {@link BinaryImage} from the database.
     *
     * @param id The ID of the {@link BinaryImage} to be loaded
     * @return The loaded instance of {@link BinaryImage} for the given ID.
     */
    BinaryImage loadBinaryImage(Integer id);


    /**
     * Removes the specified {@link BinaryImage} from the database.
     *
     * @param id The ID of the {@link BinaryImage} to be removed
     */
    void removeBinaryImage(Integer id);

    /**
     * Loads all available {@link BinaryImage}s from the database.
     * Returns an empty list, if no {@link BinaryImage}s could be found.
     *
     * @return A list with all {@link BinaryImage}s loaded from the database
     */
    List<BinaryImage> loadAllBinaryImages();

}


