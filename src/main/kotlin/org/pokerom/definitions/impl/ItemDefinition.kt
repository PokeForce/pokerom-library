package org.pokerom.definitions.impl

import org.pokerom.definitions.Definition
import org.pokerom.definitions.DefinitionType
import org.pokerom.rom.Rom
import org.pokerom.rom.graphics.ImageUtils
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.util.*

/**
 * A class representing a definition of a Pokémon item.
 *
 * @property addresses The position of the item definition in the ROM data.
 * @constructor Creates a new instance of the [ItemDefinition] class.
 */
class ItemDefinition(vararg address: Int) : Definition<Item>(type = DefinitionType.Items, addresses = address) {

    /**
     * Returns the item corresponding to the given index.
     *
     * @param index The index of the item (0-based).
     * @return The item at the specified index, or `null` if the index is out of range.
     */
    private fun Rom.getItem(index: Int): Item {
        val itemAddress = addresses[0] + (index * ITEM_SIZE)
        val spriteAddress = addresses[1] + (index * SPRITE_SIZE)

        buffer.position(itemAddress)

        // Read the item properties
        val name = buffer.getPokemonString(ITEM_NAME_SIZE)
        if(name.contains("?????")) {
            return Item(name = "null")
        }

        val idx = buffer.readShort()
        val price = buffer.readShort()
        val holdEffect = buffer.readByte()
        val parameter = buffer.readByte()
        val description = buffer.readPokemonString(buffer.readInt())
        val mysteryValue = buffer.readShort()
        val pocketSlot = buffer.readByte()
        val type = buffer.readByte()
        val usagePosition = buffer.readInt()
        val battleUsage = buffer.readLong()
        val battleUsagePosition = buffer.readInt()
        val extraParameter = buffer.readLong()

        /** Load Sprite Data **/
        buffer.position(spriteAddress)
        val imagePosition = buffer.readInt()
        val palette = ImageUtils.getPalette(this, buffer.readInt())
        val sprite = ImageUtils.getImage(this, imagePosition, palette, 24, 24).toBufferedImage()

        return Item(name, idx, price, holdEffect, parameter, description, mysteryValue, pocketSlot, type, usagePosition, battleUsage, battleUsagePosition, extraParameter, sprite, index)
    }

    override fun getDefinition(rom: Rom, index: Int): Item {
        return rom.getItem(index)
    }

    companion object {
        private const val ITEM_SIZE = 44
        private const val SPRITE_SIZE = 8
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
 * @property description The description of the item.
 * @property mysteryValue The mystery value of the item.
 * @property pocketSlot The pocket slot of the item.
 * @property type The type of the item.
 * @property usagePosition The position to the usage of the item.
 * @property battleUsage The battle usage of the item.
 * @property battleUsagePosition The position to the battle usage of the item.
 * @property extraParameter The extra parameter of the item.
 * @property id The ID of the item.
 */
data class Item(
    val name: String,
    val index: Int = -1,
    val price: Int = -1,
    val holdEffect: Byte = -1,
    val parameter: Byte = -1,
    val description: String = "",
    val mysteryValue: Int = -1,
    val pocketSlot: Byte = -1,
    val type: Byte = -1,
    val usagePosition: Int = -1,
    val battleUsage: Long = 0L,
    val battleUsagePosition: Int = -1,
    val extraParameter: Long = 0L,
    val sprite: RenderedImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
    val id: Int = -1,
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