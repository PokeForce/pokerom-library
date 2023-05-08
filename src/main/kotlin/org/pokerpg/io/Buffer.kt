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
     * The current offset position of the Buffer
     */
    private var position: Int = 0

    /**
     * Reads a single byte from the buffer at the current offset.
     *
     * @return The byte read from the buffer.
     */
    fun readByte(): Byte {
        val byte: Byte = rom.data[position]
        position += 1
        return byte
    }

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
     * Reads a short from the buffer at the current offset.
     *
     * @return The short read from the buffer.
     */
    fun readShort(): Int {
        val word = readShort(position)
        position += 2
        return word
    }

    /**
     * Reads a short from the buffer at the specified offset.
     *
     * @param offset The offset of the short in the ROM data.
     * @return The short read from the buffer.
     */
    private fun readShort(offset: Int): Int {
        val words: IntArray = BufferUtils.toInts(readBytes(offset, 2))
        return (words[1] shl 8) + words[0]
    }

    /**
     * Reads a Pokemon string from the buffer at the current offset with the specified length.
     *
     * @param length The length of the Pokemon string to read.
     * @return The converted string.
     */
    fun getPokemonString(length: Int): String {
        val text = readPokemonString(position, length)
        position += length
        return text
    }

    /**
     * Reads an integer from the buffer at the current offset.
     *
     * @param fullPointer If false, the high byte of the integer will be adjusted if necessary.
     * @return The integer read from the buffer as a Long.
     */
    fun readInt(fullPointer: Boolean = false): Long {
        val data: ByteArray = BufferUtils.getBytes(rom.data, position, 4)
        if (!fullPointer && data[3] >= 0x8) {
            data[3] = (data[3] - 0x8).toByte()
        }
        position += 4
        return BufferUtils.toLong(data)
    }

    /**
     * Reads a Long from the buffer at the current offset.
     *
     * @return The Long read from the buffer.
     */
    fun readLong(): Long {
        val data = readBytes(position, 4)
        return BufferUtils.toLong(data)
    }

    /**
     * Sets the current offset of the buffer.
     *
     * @param offset The new offset.
     */
    fun position(offset: Int) {
        if (offset > 0x08000000) {
            this.position = offset and 0x1FFFFFF
        } else {
            this.position = offset
        }
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
            } while (b.toInt() != -1)

            val byteArray = BufferUtils.getBytes(rom.data, offset, i)
            byteArray.toPokemonString()
        }
    }
}