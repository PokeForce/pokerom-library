package org.pokerpg.definitions.impl

import org.pokerpg.definitions.Definition
import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom

/**
 * A class representing a map header definition, containing various map-related properties.
 * Inherits from the Definition class and provides a concrete implementation for retrieving map header definitions.
 *
 * @param address A vararg parameter representing the addresses in the ROM where the map header data can be found.
 *
 * @author Alycia <https://github.com/alycii>
 */
class MapHeaderDefinition(vararg address: Int) : Definition<MapHeader>(type = DefinitionType.MapHeader, addresses = address) {

    /**
     * Retrieves the map header definition value from the ROM at the specified index and subIndex.
     *
     * @param rom The Rom instance that holds the data to be used for the definition retrieval.
     * @param index The index in the ROM where the data for the definition can be found.
     * @param subIndex The subIndex in the ROM data for the definition, if applicable.
     * @return The map header definition value at the specified index and subIndex.
     */
    override fun getDefinition(rom: Rom, index: Int, subIndex: Int): MapHeader {
        val position = rom.definition<MapGroup>(type = DefinitionType.MapGroup, index = index, subIndex = subIndex)?.mapHeaderAddress
        rom.buffer.position(position!!)

        val mapPosition = rom.buffer.readInt()
        val spritesPosition = rom.buffer.readInt()
        val scriptPosition = rom.buffer.readInt()
        val connectPosition = rom.buffer.readInt()
        val music = rom.buffer.readShort()
        val map = rom.buffer.readShort()
        val label = rom.buffer.readShort(rom.buffer.position)
        rom.buffer.position += 1

        val flash = rom.buffer.readByte()
        val weather = rom.buffer.readByte()
        val type = rom.buffer.readByte()
        rom.buffer.position += 2

        val labelToggle = rom.buffer.readByte()
        val battleFieldModel = rom.buffer.readByte()
        rom.buffer.position += 1

        return MapHeader(mapPosition, spritesPosition, scriptPosition, connectPosition, music, map, label, flash, weather, type, labelToggle, battleFieldModel)
    }
}

/**
 * A data class representing a map header containing various properties related to a map.
 *
 * @property mapPosition The position of the map data in the ROM.
 * @property spritesPosition The position of the sprites data in the ROM.
 * @property scriptPosition The position of the script data in the ROM.
 * @property connectPosition The position of the connection data in the ROM.
 * @property music The music ID associated with the map.
 * @property map The map ID.
 * @property label The label ID of the map.
 * @property flash The flash level of the map.
 * @property weather The weather ID of the map.
 * @property type The type of the map.
 * @property labelToggle The label toggle value for the map.
 * @property battleFieldModel The battle field model ID for the map.
 */
data class MapHeader(
    val mapPosition: Int,
    val spritesPosition: Int,
    val scriptPosition: Int,
    val connectPosition: Int,
    val music: Int,
    val map: Int,
    val label: Int,
    val flash: Byte,
    val weather: Byte,
    val type: Byte,
    val labelToggle: Byte,
    val battleFieldModel: Byte
)
