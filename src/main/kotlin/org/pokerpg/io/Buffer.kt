package org.pokerpg.io

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
     * @param offset The ROM offset to start reading from.
     * @param length The length of the Pokétext string to read, or -1 to read until the end of the text.
     * @return The converted string, or null if the byte array is empty or the offset is invalid.
     */
    fun readPokemonString(offset: Int, length: Int = -1): String {
        return if (length > -1) {
            PokemonText.toAscii(BufferUtils.getBytes(rom.data, offset, length))
        } else {
            var b: Byte = 0x0
            var i = 0

            while (b.toInt() != 0x50) {
                b = rom.data[offset + i]
                i++
            }
            PokemonText.toAscii(BufferUtils.getBytes(rom.data, offset, i))
        }
    }
}
