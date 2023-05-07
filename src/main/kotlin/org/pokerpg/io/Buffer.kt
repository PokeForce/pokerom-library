package org.pokerpg.io

import org.pokerpg.io.HexToString.toPokemonString
import org.pokerpg.rom.Rom

/**
 * A class representing a buffer used to read data from a ROM file.
 *
 * @property rom The ROM object containing the data to read.
 * @author Alycia <https://github.com/alycii>
 */
class Buffer(private val rom: Rom) {

    /**
     * Reads a single byte from the buffer at the specified offset.
     *
     * @param offset The offset of the byte in the ROM data.
     * @return The byte read from the buffer.
     */
    fun readByte(offset: Int): Byte {
        return readBytes(offset)[0]
    }

    /**
     * Reads a specified number of bytes from the buffer, starting at the specified offset.
     *
     * @param offset The offset of the bytes in the ROM data.
     * @param size The number of bytes to read.
     * @return The ByteArray read from the buffer.
     */
    private fun readBytes(offset: Int, size: Int = 1): ByteArray {
        return BufferUtils.getBytes(rom.data, offset, size)
    }

    /**
     * Reads a string from the buffer, starting at the specified offset and with the specified length.
     *
     * @param offset The offset of the string in the ROM data.
     * @param length The length of the string to read.
     * @return The string read from the buffer.
     */
    fun readString(offset: Int, length: Int): String {
        return String(BufferUtils.getBytes(rom.data, offset, length))
    }

    /**
     * Reads and converts a Pokétext string from the specified ROM offset.
     *
     * @param offset The ROM offset to start reading from.
     * @param length The length of the Pokétext string to read, or -1 to read until the end of the text.
     * @return The converted string, or null if the byte array is empty or the offset is invalid.
     * @throws IndexOutOfBoundsException If the provided offset is out of bounds for the ROM data.
     */
    fun readPokemonString(offset: Int, length: Int = -1): String {
        require(offset >= 0) { "Offset must be non-negative" }

        return if (length > -1) {
            val byteArray = BufferUtils.getBytes(rom.data, offset, length)
            byteArray.toPokemonString()
        } else {
            var i = 0
            var b: Byte

            do {
                b = rom.data.getOrNull(offset + i) ?: break
                i++
            } while (b.toInt() != 0x50)

            val byteArray = BufferUtils.getBytes(rom.data, offset, i)
            byteArray.toPokemonString()
        }
    }

}
