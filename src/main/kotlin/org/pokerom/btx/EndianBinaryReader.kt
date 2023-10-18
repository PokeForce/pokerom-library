package org.pokerom.btx

import java.io.ByteArrayInputStream

/**
 * @author Alycia <https://github.com/alycii>
 */
class EndianBinaryReader(data: ByteArray) : ByteArrayInputStream(data) {

    val size: Int = data.size

    fun seek(pos: Int) {
        reset()
        skip(pos.toLong())
    }

    fun readBytes(num: Int): ByteArray {
        val tmp = ByteArray(num)
        read(tmp, 0, num)
        return tmp
    }

    fun readString(num: Int): String {
        val tmp = ByteArray(num)
        read(tmp, 0, num)
        return String(tmp)
    }

    fun readInt32(): Int {
        val low = (read() and 0xFF shl 0) + (read() and 0xFF shl 8) + (read() and 0xFF shl 16)
        val high = read() and 0xFF
        return (high shl 24) + (0xFFFFFFFFL and low.toLong()).toInt()
    }

    fun readInt16(): Int {
        return (read() and 0xFF shl 0) + (read() and 0xFF shl 8)
    }

    fun getPosition(): Int {
        return pos
    }
}