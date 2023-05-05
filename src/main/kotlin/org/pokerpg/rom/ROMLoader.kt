package org.pokerpg.rom

import java.io.IOException

object ROMLoader {

    /**
     * Loads a ROM from the roms attached file, reads the metadata, and sets the ROM object's properties.
     *
     * @receiver The target ROM object.
     * @throws IOException If an error occurs while reading the file.
     */
    @Throws(IOException::class)
    fun Rom.load() = apply {
        loadRomBytes()
        gameCode = buffer.readString(0xAC, 4)
        gameName = buffer.readString(0xA0, 12).trim()
        gameCreator = buffer.readString(0xB0, 2)
        setFlags()
    }


    /**
     * Loads the ROM data from the specified file into the given ROM object.
     *
     * @receiver The target ROM object, which should have a 'file' property containing the ROM file.
     * @throws IOException If an error occurs while reading the file.
     */
    @Throws(IOException::class)
    private fun Rom.loadRomBytes() = apply {
        file.inputStream().use { inputStream ->
            val length = file.length()
            val bytes = ByteArray(length.toInt())

            var offset = 0
            var n: Int

            while (offset < bytes.size) {
                n = inputStream.read(bytes, offset, bytes.size - offset)
                if (n < 0) break
                offset += n
            }

            data = bytes
        }
    }
}