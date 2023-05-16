package org.pokerpg.io

import me.hugmanrique.pokedata.compression.CompressUtils
import me.hugmanrique.pokedata.compression.HexInputStream
import org.pokerpg.io.HexToString.toPokemonString
import org.pokerpg.rom.Rom
import java.io.ByteArrayInputStream
import java.io.InputStream

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
    var position: Int = 0

    /**
     * Reads a single byte from the buffer at the current position.
     *
     * @return The byte read from the buffer.
     */
    fun readByte(): Byte {
        val byte: Byte = rom.data[position]
        position += 1
        return byte
    }

    /**
     * Reads a single byte from the buffer at the specified position.
     *
     * @param position The position of the byte in the ROM data.
     * @return The byte read from the buffer.
     */
    fun readByte(position: Int): Byte {
        return readBytes(position)[0]
    }

    /**
     * Reads a specified number of bytes from the buffer, starting at the specified position.
     *
     * @param position The position of the bytes in the ROM data.
     * @param size The number of bytes to read.
     * @return The ByteArray read from the buffer.
     */
    fun readBytes(position: Int, size: Int = 1): ByteArray {
        return BufferUtils.getBytes(rom.data, position, size)
    }

    /**
     * Reads a short from the buffer at the current position.
     *
     * @return The short read from the buffer.
     */
    fun readShort(): Int {
        val word = readShort(position)
        position += 2
        return word
    }

    /**
     * Reads a short from the buffer at the specified position.
     *
     * @param position The position of the short in the ROM data.
     * @return The short read from the buffer.
     */
    fun readShort(position: Int): Int {
        val words: IntArray = BufferUtils.toInts(readBytes(position, 2))
        return (words[1] shl 8) + words[0]
    }

    /**
     * Reads a Pokémon string from the buffer at the current position with the specified length.
     *
     * @param length The length of the Pokémon string to read.
     * @return The converted string.
     */
    fun getPokemonString(length: Int): String {
        val text = readPokemonString(position, length)
        position += length
        return text
    }

    /**
     * Reads an integer from the buffer at the current position.
     *
     * @param fullPointer If false, the high byte of the integer will be adjusted if necessary.
     * @return The integer read from the buffer as a Long.
     */
    fun readInt(fullPointer: Boolean = false): Int {
        val data: ByteArray = BufferUtils.getBytes(rom.data, position, 4)
        if (!fullPointer && data[3] >= 0x8) {
            data[3] = (data[3] - 0x8).toByte()
        }
        position += 4
        return BufferUtils.toLong(data).toInt()
    }

    /**
     * Reads an integer from the buffer at the specified position.
     *
     * @param fullPointer If false, the high byte of the integer will be adjusted if necessary.
     * @return The integer read from the buffer as a Long.
     */
    fun readInt(position: Int = -1, fullPointer: Boolean = false): Int {
        val data: ByteArray = BufferUtils.getBytes(rom.data, position, 4)
        if (!fullPointer) {
            data[3] = 0
        }
        return BufferUtils.toLong(data).toInt()
    }

    /**
     * Reads a Long from the buffer at the current position.
     *
     * @return The Long read from the buffer.
     */
    fun readLong(): Long {
        val data = readBytes(position, 4)
        return BufferUtils.toLong(data)
    }

    /**
     * Sets the current position of the buffer.
     *
     * @param position The new position.
     */
    fun position(position: Int) {
        if (position > 0x08000000) {
            this.position = position and 0x1FFFFFF
        } else {
            this.position = position
        }
    }

    /**
     * Reads a string from the buffer, starting at the specified position and with the specified length.
     *
     * @param position The position of the string in the ROM data.
     * @param length The length of the string to read.
     * @return The string read from the buffer.
     */
    fun readString(position: Int, length: Int): String {
        return String(BufferUtils.getBytes(rom.data, position, length))
    }

    /**
     * Reads and converts a Pokétext string from the specified ROM position.
     *
     * @param position The ROM position to start reading from.
     * @param length The length of the Pokétext string to read, or -1 to read until the end of the text.
     * @return The converted string, or null if the byte array is empty or the position is invalid.
     * @throws IndexOutOfBoundsException If the provided position is out of bounds for the ROM data.
     */
    fun readPokemonString(position: Int, length: Int = -1): String {
        require(position >= 0) { "position must be non-negative" }

        return if (length > -1) {
            val byteArray = BufferUtils.getBytes(rom.data, position, length)
            byteArray.toPokemonString()
        } else {
            var i = 0
            var b: Byte

            do {
                b = rom.data.getOrNull(position + i) ?: break
                i++
            } while (b.toInt() != -1)

            val byteArray = BufferUtils.getBytes(rom.data, position, i)
            byteArray.toPokemonString()
        }
    }

    fun decompress(position: Int): ByteArray? {
        val stream: InputStream = ByteArrayInputStream(rom.data)
        val hexStream = HexInputStream(stream)
        return try {
            stream.skip(position.toLong())
            CompressUtils.decompress(hexStream).map { it.toByte() }.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}