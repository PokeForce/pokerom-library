package org.pokerpg.definitions

import org.pokerpg.rom.Rom

/**
 * An abstract base class for definitions that represent different types of data
 * found in the ROM, such as Pokémon names, moves, etc.
 *
 * @param T The type of the definition value (e.g., String, ByteArray).
 * @param type The DefinitionType associated with this definition.
 * @param addresses A vararg parameter representing the addresses in the ROM where the data for the definition can be found.
 *
 * @author Alycia <https://github.com/alycii>
 */
abstract class Definition<T>(val type: DefinitionType, protected vararg val addresses: Int) {

    /**
     * Retrieves the definition value from the ROM at the specified index.
     * This function has a default implementation that returns null, making it optional for derived classes to override it.
     *
     * @param rom The Rom instance that holds the data to be used for the definition retrieval.
     * @param index The index in the ROM where the data for the definition can be found.
     * @return The definition value of type T at the specified index or null if the derived class doesn't provide an implementation.
     */
    open fun getDefinition(rom: Rom, index: Int): T? {
        return null
    }

    /**
     * Retrieves the definition value from the ROM at the specified index and subIndex.
     * This function has a default implementation that returns null, making it optional for derived classes to override it.
     *
     * @param rom The Rom instance that holds the data to be used for the definition retrieval.
     * @param index The index in the ROM where the data for the definition can be found.
     * @param subIndex The subIndex in the ROM data for the definition, if applicable.
     * @return The definition value of type T at the specified index and subIndex or null if the derived class doesn't provide an implementation.
     */
    open fun getDefinition(rom: Rom, index: Int, subIndex: Int): T? {
        return null
    }
}
