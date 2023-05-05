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

}

