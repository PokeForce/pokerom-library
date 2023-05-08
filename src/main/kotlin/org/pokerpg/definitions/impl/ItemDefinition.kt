package org.pokerpg.definitions.impl

import org.pokerpg.definitions.Definition
import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom

/**
 * A class representing a definition of a Pokémon item.
 *
 * @property address The offset of the item definition in the ROM data.
 * @constructor Creates a new instance of the [ItemDefinition] class.
 */
class ItemDefinition(address: Int) : Definition<Item>(type = DefinitionType.Items, address = address) {

    /**
     * Returns the item corresponding to the given index.
     *
     * @param index The index of the item (0-based).
     * @return The item at the specified index, or `null` if the index is out of range.
     */
    private fun Rom.getItem(index: Int): Item {
        val itemAddress = address + (index * ITEM_SIZE)
        buffer.position(itemAddress)

        // Read the item properties
        val name = buffer.getPokemonString(ITEM_NAME_SIZE)

        return Item(
            name = name,
            index = buffer.readShort(),
            price = buffer.readShort(),
            holdEffect = buffer.readByte(),
            parameter = buffer.readByte(),
            description = buffer.readPokemonString(buffer.readInt().toInt()),
            mysteryValue = buffer.readShort(),
            pocketSlot = buffer.readByte(),
            type = buffer.readByte(),
            usageOffset = buffer.readInt(),
            battleUsage = buffer.readLong(),
            battleUsageOffset = buffer.readInt(),
            extraParameter = buffer.readLong(),
            id = index
        )
    }

    override fun getDefinition(rom: Rom, index: Int): Item {
        return rom.getItem(index)
    }

    companion object {
        private const val ITEM_SIZE = 44
        private const val ITEM_NAME_SIZE = 14
    }
}

/**
 * A data class representing a Pokémon item.
 *
 * @property name The name of the item.
 * @property index The index of the item.
 * @property price The price of the item.
 * @property holdEffect The hold effect of the item.
 * @property parameter The parameter of the item.
 * @property descriptionOffset The offset to the description of the item.
 * @property mysteryValue The mystery value of the item.
 * @property pocketSlot The pocket slot of the item.
 * @property type The type of the item.
 * @property usageOffset The offset to the usage of the item.
 * @property battleUsage The battle usage of the item.
 * @property battleUsageOffset The offset to the battle usage of the item.
 * @property extraParameter The extra parameter of the item.
 * @property id The ID of the item.
 */
data class Item(
    val name: String,
    val index: Int,
    val price: Int,
    val holdEffect: Byte,
    val parameter: Byte,
    val description: String,
    val mysteryValue: Int,
    val pocketSlot: Byte,
    val type: Byte,
    val usageOffset: Long,
    val battleUsage: Long,
    val battleUsageOffset: Long,
    val extraParameter: Long,
    val id: Int,
)

/**
 * An enumeration representing the different pockets and their corresponding slots.
 *
 * @property elementSlot The slot number in a non-Gem game.
 * @property gemSlot The slot number in a Gem game.
 * @constructor Creates a new instance of the [PocketSlot] enumeration.
 */
@Suppress("UNUSED")
enum class PocketSlot(val elementSlot: Int, val gemSlot: Int) {
    BAG_ITEMS(elementSlot = 1, gemSlot = 1),
    KEY_ITEMS(elementSlot = 2, gemSlot = 0),
    POKE_BALLS(elementSlot = 3, gemSlot = 2),
    BERRIES(elementSlot = 4, gemSlot = 4),
    TECHNICAL_MACHINES(elementSlot = 5, gemSlot = 3)
}