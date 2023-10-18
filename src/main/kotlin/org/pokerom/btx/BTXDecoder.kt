package org.pokerom.btx

import narc.FimgEntry
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException

/**
 * @author Alycia <https://github.com/alycii>
 */
class BTXDecoder {


    private var size = 0
    private var texSize = 0
    private var texDataSize = 0
    private var texDataOffset = 0
    private var compTexDataSize = 0
    private var compTexDataOffset = 0
    private var compTexInfoDataOffset = 0
    private var palDataSize = 0
    private var palInfoOffset = 0
    private var palDataOffset = 0
    private var numObjects_3d = 0
    private var size_3d = 0
    private var infoDataSize_3d = 0
    private var numObjects_pal = 0
    private var size_pal = 0
    private var infoDataSize_pal = 0

    private var unknownBlock1: ByteArray = byteArrayOf()
    private var unknownBlock2: ByteArray = byteArrayOf()

    private var pal_names: Array<String> = arrayOf()
    private var tex_names: Array<String> = arrayOf()

    private val textures: MutableList<Texture> = ArrayList()
    private val palettes: MutableList<Palette> = ArrayList()

    private val bpp = intArrayOf(0, 8, 2, 4, 8, 2, 8, 16)
    var img: Array<BufferedImage?> = arrayOfNulls(numObjects_3d)


    constructor(fs: FimgEntry) {
        try {
            val inp = EndianBinaryReader(fs.entryData)
            if (inp.readInt32() != 811095106) {
                inp.close()
                throw IOException()
            }
            inp.skip(4)
            size = inp.readInt32()
            inp.skip(12)
            texSize = inp.readInt32()
            inp.skip(4)
            texDataSize = inp.readInt16() shl 3
            inp.skip(6)
            texDataOffset = inp.readInt32()
            inp.skip(4)
            compTexDataSize = inp.readInt16() shl 3
            inp.skip(6)
            compTexDataOffset = inp.readInt32()
            compTexInfoDataOffset = inp.readInt32()
            inp.skip(4)
            palDataSize = inp.readInt32() shl 3
            palInfoOffset = inp.readInt32()
            palDataOffset = inp.readInt32()
            inp.skip(1)
            numObjects_3d = inp.read()
            img = arrayOfNulls(numObjects_3d)
            size_3d = inp.readInt16()
            unknownBlock1 = inp.readBytes(10 + 4 * numObjects_3d)
            infoDataSize_3d = inp.readInt16()

            for (i in 0 until numObjects_3d) {
                val tmp = Texture()
                tmp.offset = inp.readInt16() shl 3
                tmp.parameter = inp.readInt16()
                tmp.width2 = inp.read()
                tmp.unknown1 = inp.read()
                tmp.height2 = inp.read()
                tmp.unknown2 = inp.read()
                tmp.coord_transf = tmp.parameter and 0xE
                tmp.color0 = tmp.parameter shr 13 and 1
                tmp.format = tmp.parameter shr 10 and 7
                tmp.height = 8 shl (tmp.parameter shr 7 and 7)
                tmp.width = 8 shl (tmp.parameter shr 4 and 7)
                tmp.flipY = tmp.parameter shr 3 and 1
                tmp.flipX = tmp.parameter shr 2 and 1
                tmp.repeatY = tmp.parameter shr 1 and 1
                tmp.repeatX = tmp.parameter and 1
                tmp.depth = bpp[tmp.format]

                if (tmp.width == 0) {
                    when (tmp.unknown1 and 3) {
                        2 -> tmp.width = 512
                        else -> tmp.width = 256
                    }
                }

                if (tmp.height == 0) {
                    when (tmp.height2 shr 3 and 3) {
                        2 -> tmp.height = 512
                        else -> tmp.height = 256
                    }
                }

                val imgsize = tmp.width * tmp.height * tmp.depth / 8
                val curpos = inp.getPosition()
                if (tmp.format != 5) {
                    inp.seek(tmp.offset + 20 + texDataOffset)
                } else {
                    inp.seek(20 + compTexDataOffset + tmp.offset)
                }
                tmp.image = inp.readBytes(imgsize)
                inp.seek(curpos)

                if (tmp.format == 5) {
                    val curpos2 = inp.getPosition()
                    inp.seek(20 + compTexInfoDataOffset + tmp.offset / 2)
                    tmp.spData = inp.readBytes(imgsize / 2)
                    inp.seek(curpos2)
                }
                textures.add(tmp)  // Assuming textures is a MutableList
            }

            tex_names = Array(numObjects_3d) { "" }
            for (i in 0 until numObjects_3d) {
                tex_names[i] = inp.readString(16).replace("\u0000", "")
            }

            inp.skip(1)
            numObjects_pal = inp.read()
            size_pal = inp.readInt16()
            unknownBlock2 = inp.readBytes(10 + 4 * numObjects_pal)
            infoDataSize_pal = inp.readInt16()

            for (i in 0 until numObjects_pal) {
                val tmp = Palette()
                tmp.offset = inp.readInt16() shl 3
                tmp.color0 = inp.readInt16()
                val curpos = inp.getPosition()
                inp.seek(20 + palDataOffset + tmp.offset)
                tmp.pal = Palette.toBGR555Array(inp.readBytes(palDataSize - tmp.offset))
                palettes.add(tmp)
                inp.seek(curpos)
            }

            pal_names = Array(numObjects_pal) { "" }
            for (i in 0 until numObjects_pal) {
                pal_names[i] = inp.readString(16).replace("\u0000", "")
            }

            for (i in 0 until numObjects_3d) {
                img[i] = getImage(i, 0)
            }
            inp.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getImage(index: Int, palnum: Int): BufferedImage {
        val width = textures[index].width
        val height = textures[index].height
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        val pixelnum = width * height
        val texture = textures[index]
        val palette = palettes[palnum]
        val imageData = texture.image

        when (texture.format) {
            1 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j].toInt() and 0x1F
                    var alpha = imageData[j].toInt() shr 5
                    alpha = (alpha * 4 + alpha / 2) * 8
                    val x = j % width
                    val y = j / width
                    val color = palette.getPalRGBs()[index2]
                    image.setRGB(x, y, Color(color.red, color.green, color.blue, alpha).rgb)
                }
            }
            2 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j / 4].toInt() shr (j % 4 shl 1) and 3
                    if (index2 != 0 || texture.color0 != 1) continue
                    val x = j % width
                    val y = j / width
                    val color = palette.getPalRGBs()[index2]
                    image.setRGB(x, y, Color(color.red, color.green, color.blue).rgb)
                }
            }
            3 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j / 2].toInt() shr (j % 2 shl 2) and 0xF
                    val x = j % width
                    val y = j / width
                    val color = palette.getPalRGBs()[index2]
                    image.setRGB(x, y, Color(color.red, color.green, color.blue).rgb)
                }
            }
            4 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j].toInt()
                    val x = j % width
                    val y = j / width
                    val color = palette.getPalRGBs()[index2]
                    image.setRGB(x, y, Color(color.red, color.green, color.blue).rgb)
                }
            }
            6 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j].toInt() and 7
                    var alpha = imageData[j].toInt() shr 3
                    alpha = alpha.times(8)
                    val x = j % width
                    val y = j / width
                    val color = palette.getPalRGBs()[index2]
                    image.setRGB(x, y, Color(color.red, color.green, color.blue, alpha).rgb)
                }
            }
            7 -> {
                for (j in 0 until pixelnum) {
                    val index2 = imageData[j * 2] + (imageData[j * 2 + 1].toInt() shl 8)
                    val alpha = if (index2 and 0x8000 != 0) 255 else 0
                    val red = (index2 shr 0 and 0x1F) shl 3
                    val green = (index2 shr 5 and 0x1F) shl 3
                    val blue = (index2 shr 10 and 0x1F) shl 3
                    val x = j % width
                    val y = j / width
                    image.setRGB(x, y, Color(red, green, blue, alpha).rgb)
                }
            }
        }

        return image
    }
}