package org.pokerpg.rom.graphics

import java.awt.Graphics
import java.awt.Point
import java.awt.image.BufferedImage

/**
 * Represents a ROM Image
 *
 * @property palette The palette used in the image.
 * @property data The image data.
 * @property size The size of the image.
 */
data class RomImage(
    val palette: Palette,
    val data: ByteArray,
    val size: Point
) {

    constructor(palette: Palette, data: ByteArray, width: Int, height: Int) : this(
        palette,
        data,
        Point(width, height)
    )

    /**
     * Converts the [RomImage] to a [BufferedImage].
     *
     * @param transparency Whether to use transparency.
     * @return The created [BufferedImage].
     */
    fun toBufferedImage(transparency: Boolean = true): BufferedImage {
        val image = BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB)
        val type = palette.type
        val graphics: Graphics? = if (type.graphics) image.createGraphics() else null

        for (i in 0 until data.size * type.resize) {
            val x = i % 8
            val y = (i / 8) % 8
            val blockX = (i / 64) % (image.width / 8)
            val blockY = i / (64 * (image.width / 8))

            val pal = data[i / type.resize]
            val paletteIndex = type.getPalette(i, pal.toInt())
            type.setPixel(
                image = image,
                graphics = graphics,
                x = blockX * 8 + x,
                y = blockY * 8 + y,
                palette = palette,
                pal = paletteIndex,
                transparency = transparency
            )
        }

        return image
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RomImage

        if (palette != other.palette) return false
        if (!data.contentEquals(other.data)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = palette.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}
