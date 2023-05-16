package org.pokerpg.definitions.impl

import org.pokerpg.definitions.Definition
import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom

/**
 * A class representing a map "group" or folder that contains map data.
 * Inherits from the Definition class and provides a concrete implementation for retrieving map group definitions.
 *
 * @param address A vararg parameter representing the addresses in the ROM where the map group data can be found.
 *
 * @author Alycia <https://github.com/alycii>
 */
class MapGroupDefinition(vararg address: Int) : Definition<MapGroup>(type = DefinitionType.MapGroup, addresses = address) {

    /**
     * Retrieves the map group definition value from the ROM at the specified index and subIndex.
     *
     * @param rom The Rom instance that holds the data to be used for the definition retrieval.
     * @param index The index in the ROM where the data for the definition can be found.
     * @param subIndex The subIndex in the ROM data for the definition, if applicable.
     * @return The map group definition value at the specified index and subIndex.
     */
    override fun getDefinition(rom: Rom, index: Int, subIndex: Int): MapGroup {
        val position = addresses[index] + (subIndex * 4)
        return MapGroup(rom.buffer.readInt(position = position))
    }
}

/**
 * A data class representing a map group containing a single property, the map header address.
 *
 * @property mapHeaderAddress The address of the map header in the ROM.
 */
data class MapGroup(val mapHeaderAddress: Int)
