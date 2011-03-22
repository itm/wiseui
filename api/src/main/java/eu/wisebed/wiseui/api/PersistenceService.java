package eu.wisebed.wiseui.api;

import eu.wisebed.wiseui.shared.TestbedConfiguration;

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
}
