package org.pokerom.rom.graphics

import org.pokerom.rom.Rom

/**
 * A utility class for handling images and palettes.
 */
object ImageUtils {
    private val paletteCache = HashMap<Int, Palette>()

    /**
     * Gets a palette from the ROM at the specified pointer.
     *
     * @param rom The ROM.
     * @param pointer The pointer to the palette in the ROM.
     * @return The palette or null if not found.
     */
    fun getPalette(rom: Rom, pointer: Int): Palette {
        return getPalette(rom, pointer, ImageType.C16)
    }

    /**
     * Gets a palette from the ROM at the specified pointer, with the specified image type and cache option.
     *
     * @param rom The ROM.
     * @param position The position to the palette in the ROM.
     * @param type The image type.
     * @param cache Whether to cache the palette.
     * @return The palette.
     */
    private fun getPalette(rom: Rom, position: Int, type: ImageType, cache: Boolean = true): Palette {
        if (cache && paletteCache.containsKey(position)) {
            return paletteCache.getOrDefault(position, 0) as Palette
        }
        val data = rom.buffer.decompress(position) ?: return Palette()
        val palette = Palette(type, data)
        if (cache) {
            paletteCache[position] = palette
        }
        return palette
    }

    /**
     * Gets an image from the ROM at the specified pointer, with the specified palette, width, and height.
     *
     * @param rom The ROM.
     * @param position The position to the image in the ROM.
     * @param palette The palette.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The ROM image.
     */
    fun getImage(rom: Rom, position: Int, palette: Palette, width: Int, height: Int): RomImage {
        val data = rom.buffer.decompress(position)
        if (data == null) {
            val imageData = rom.buffer.readBytes(position, width * height)
            return loadRawSprite(imageData, palette, width, height)
        }
        return RomImage(palette, data, width, height)
    }

    /**
     * Loads a raw sprite from the given byte array, with the specified palette, width, and height.
     *
     * @param bits The byte array containing the sprite data.
     * @param palette The palette.
     * @param width The width of the sprite.
     * @param height The height of the sprite.
     * @return The ROM image.
     */
    fun loadRawSprite(bits: ByteArray, palette: Palette, width: Int, height: Int): RomImage {
        val colors = ByteArray(bits.size * 8)

        for (i in bits.indices) {
            val position = i * 8
            val data = bits[i].toInt()

            for (j in 0..7) {
                colors[position + j] = ((data shr j) and 1).toByte()
            }
        }

        return RomImage(palette, colors, width, height)
    }
}
