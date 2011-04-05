package eu.wisebed.wiseui.api;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.BinaryImage;

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
}


