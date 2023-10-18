package org.pokerom.rom.graphics

import org.pokerom.io.BufferUtils
import org.pokerom.rom.Rom
import java.awt.Color

/**
 * Represents a color palette for ROM images.
 */
@Suppress("UNUSED")
class Palette {
    private val colors: Array<Color?>
    private val reds: ByteArray
    private val greens: ByteArray
    private val blues: ByteArray

    /**
     * Constructs a palette from the given image type and color data.
     *
     * @param type The image type.
     * @param data The color data.
     */
    constructor(type: ImageType, data: IntArray) {
        val size = type.size

        colors = arrayOfNulls(size)
        reds = ByteArray(size)
        greens = ByteArray(size)
        blues = ByteArray(size)

        for (i in data.indices step 2) {
            if (i >= type.imageSize) break
            val color = data[i] + (data[i + 1] shl 8)

            val red = (color and 0x1F) shl 3
            val green = (color and 0x3E0) shr 2
            val blue = (color and 0x7C00) shr 7

            val index = i / 2

            reds[index] = red.toByte()
            greens[index] = green.toByte()
            blues[index] = blue.toByte()
            colors[index] = Color(red, green, blue)
        }
    }

    /**
     * Constructs a palette from the given image type and byte array of color data.
     *
     * @param type The image type.
     * @param data The byte array of color data.
     */
    constructor(type: ImageType, data: ByteArray) : this(type, BufferUtils.toInts(data))

    /**
     * Constructs a palette from the given image type, ROM, and position.
     *
     * @param type The image type.
     * @param rom The ROM.
     * @param position The position in the ROM.
     */
    constructor(type: ImageType, rom: Rom, position: Int) : this(type, rom.buffer.readBytes(position, type.imageSize))

    /**
     * Constructs a palette from a list of colors.
     *
     * @param colors The list of colors.
     */
    constructor(vararg colors: Color) {
        @Suppress("UNCHECKED_CAST")
        this.colors = colors as Array<Color?>

        reds = ByteArray(colors.size)
        greens = ByteArray(colors.size)
        blues = ByteArray(colors.size)

        for (i in colors.indices) {
            val color = colors[i]

            reds[i] = color.red.toByte()
            greens[i] = color.green.toByte()
            blues[i] = color.blue.toByte()
        }
    }

    /**
     * Retrieves the color at the specified index in the palette.
     *
     * @param index The index of the color.
     * @return The color, or black if the index is out of bounds.
     */
    fun getColor(index: Int): Color {
        return colors.getOrElse(index) { Color.BLACK }!!
    }

    /**
     * Retrieves the RGB integer value of the color at the specified index in the palette.
     *
     * @param index The index of the color.
     * @return The RGB integer value, or black if the index is out of bounds.
     */
    fun getRgbInt(index: Int): Int {
        return colors.getOrElse(index) { Color.BLACK }!!.rgb
    }

    /**
     *
     * Gets the image type based on the number of colors in the palette.
     *
     * @return The image type.
     */
    val type: ImageType
        get() = when (colors.size) {
            16 -> ImageType.C16
            else -> ImageType.C256
        }
}