package org.pokerpg.rom.graphics

import java.awt.Graphics
import java.awt.image.BufferedImage

/**
 * Represents the image type
 *
 * @property size The size of the image.
 * @property resize The resize factor.
 * @property graphics Whether to use graphics for rendering.
 */
enum class ImageType(
    val size: Int,
    val resize: Int,
    val graphics: Boolean
) : ImageRenderer {

    /**
     * Represents a 16-color image type.
     */
    C16(16, 2, false) {
        override fun setPixel(
            image: BufferedImage?,
            graphics: Graphics?,
            x: Int,
            y: Int,
            palette: Palette?,
            pal: Int,
            transparency: Boolean
        ) {
            val color = palette?.getColor(pal)
            val pixel = intArrayOf(
                color!!.red,
                color.green,
                color.blue,
                if (transparency && pal == 0) 0 else 255
            )

            image!!.raster.setPixel(x, y, pixel)
        }

        override fun getPalette(i: Int, original: Int): Int {
            return if (i and 1 == 0) {
                original and 0xF
            } else {
                (original and 0xF0) shr 4
            }
        }
    },

    /**
     * Represents a 256-color image type.
     */
    C256(256, 1, true) {
        override fun setPixel(
            image: BufferedImage?,
            graphics: Graphics?,
            x: Int,
            y: Int,
            palette: Palette?,
            pal: Int,
            transparency: Boolean
        ) {

            graphics!!.color = palette!!.getColor(pal)
            graphics.drawRect(x, y, 1, 1)
        }

        override fun getPalette(i: Int, original: Int): Int {
            return original
        }
    };

    /**
     * The size of the ROM image.
     */
    val imageSize: Int
        get() = size * 2
}

/**
 * @author Hugmanrique
 */
interface ImageRenderer {
    fun setPixel(
        image: BufferedImage?,
        graphics: Graphics?,
        x: Int,
        y: Int,
        palette: Palette?,
        pal: Int,
        transparency: Boolean
    )

    fun getPalette(i: Int, original: Int): Int
}