package org.pokerpg.definitions

import org.pokerpg.rom.Rom

/**
 * An abstract base class for definitions that represent different types of data
 * found in the ROM, such as Pokémon names, moves, etc.
 *
 * @param T The type of the definition value (e.g., String, ByteArray).
 * @param type The DefinitionType associated with this definition.
 *
 * @author Alycia <https://github.com/alycii>
 */
abstract class Definition<T>(val type: DefinitionType, protected vararg val addresses: Int) {

    /**
     * Retrieves the definition value from the ROM at the specified index.
     *
     * @param rom The Rom instance that holds the data to be used for the definition retrieval.
     * @param index The index in the ROM where the data for the definition can be found.
     * @return The definition value of type T at the specified index.
     */
    abstract fun getDefinition(rom: Rom, index: Int): T
}

