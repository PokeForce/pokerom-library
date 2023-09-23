package org.pokerpg.btx

import java.awt.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Palette {
    var offset: Int = 0
    var color0: Int = 0
    var pal: Array<Color>? = null

    companion object {
        fun toBGR555Array(bytes: ByteArray): Array<Color> {
            val palette = Array(bytes.size / 2) { Color(0, 0, 0) }
            for (i in bytes.indices step 2) {
                palette[i / 2] = toBGR555(bytes[i], bytes[i + 1])
            }
            return palette
        }

        fun fromBGR555(p: Array<Color>): ByteArray {
            val b = ByteBuffer.allocate(p.size * 2).order(ByteOrder.LITTLE_ENDIAN)
            for (color in p) {
                var num = 0
                num += color.red / 8
                num += (color.green / 8) shl 5
                b.putShort((num + (color.blue / 8 shl 10)).toShort())
            }
            return b.array()
        }

        fun toBGR555(byte1: Byte, byte2: Byte): Color {
            val b = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
            b.put(byte1)
            b.put(byte2)
            val num = b.getShort(0).toInt()
            return Color((num and 0x1F) * 8, (num and 0x3E0 shr 5) * 8, (num and 0x7C00 shr 10) * 8)
        }
    }

    fun getPalRGBs(): Array<Color> {
        return pal ?: emptyArray()
    }
}
