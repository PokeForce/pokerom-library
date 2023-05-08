package org.pokerpg.rom.graphics

import org.pokerpg.rom.Rom

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
     * @param offset The offset to the palette in the ROM.
     * @param type The image type.
     * @param cache Whether to cache the palette.
     * @return The palette.
     */
    private fun getPalette(rom: Rom, offset: Int, type: ImageType, cache: Boolean = true): Palette {
        if (cache && paletteCache.containsKey(offset)) {
            return paletteCache.getOrDefault(offset, 0) as Palette
        }
        val data = rom.buffer.decompress(offset) ?: return Palette()
        val palette = Palette(type, data)
        if (cache) {
            paletteCache[offset] = palette
        }
        return palette
    }

    /**
     * Gets an image from the ROM at the specified pointer, with the specified palette, width, and height.
     *
     * @param rom The ROM.
     * @param offset The offset to the image in the ROM.
     * @param palette The palette.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The ROM image.
     */
    fun getImage(rom: Rom, offset: Int, palette: Palette, width: Int, height: Int): RomImage {
        val data = rom.buffer.decompress(offset)
        if (data == null) {
            val imageData = rom.buffer.readBytes(offset, width * height)
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
    private fun loadRawSprite(bits: ByteArray, palette: Palette, width: Int, height: Int): RomImage {
        val colors = ByteArray(bits.size * 8)

        for (i in bits.indices) {
            val offset = i * 8
            val data = bits[i].toInt()

            for (j in 0..7) {
                colors[offset + j] = ((data shr j) and 1).toByte()
            }
        }

        return RomImage(palette, colors, width, height)
    }
}
