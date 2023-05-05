package org.pokerpg.buffer

import org.pokerpg.rom.Rom

/**
 * A class representing a buffer used to read data from a ROM file.
 *
 * @property rom The ROM object containing the data to read.
 * @author Alycia <https://github.com/alycii>
 */
class Buffer(private val rom: Rom) {

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
     * Reads a string of PokeText from the specified offset with the specified length.
     *
     * If a length is specified, returns the converted PokeText string.
     *
     * If no length is specified, reads from the specified offset until the end of the text.
     * Returns the converted PokeText string that was read.
     *
     * @param offset The offset to read from.
     * @param length The length of the PokeText string to read.
     * @return The PokeText string that was read.
     */
    fun readPokemonString(offset: Int, length: Int = -1): String? {
        return if (length > -1) {
            // If length is specified, return converted PokeText string
            PokemonText.toAscii(BufferUtils.getBytes(rom.data, offset, length))
        } else {
            // Read from specified offset until end of text
            var b: Byte = 0x0
            var i = 0

            while (b.toInt() != -1) {
                b = rom.data[offset + i]
                i++
            }
            PokemonText.toAscii(BufferUtils.getBytes(rom.data, offset, i))
        }
    }


}

