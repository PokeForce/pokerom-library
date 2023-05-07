package org.pokerpg.rom

import org.pokerpg.definitions.impl.PokemonNames
import org.pokerpg.io.HexToString
import org.pokerpg.util.Properties
import java.io.IOException
import java.nio.file.Paths

/**
 * @author Alycia <https://github.com/alycii>
 */
object RomLoader {

    /**
     * Loads addresses from the specified YAML file into the current ROM instance.
     *
     * @return the current ROM instance.
     */
    @Throws(IOException::class)
    fun Rom.loadDefinitions(): Rom {
        val romAddresses = Properties()
        romAddresses.loadRomAddresses(Paths.get("./data/rom-addresses.yml").toFile())
        registerDefinitions(
            PokemonNames(
                address = romAddresses.getOrDefault(romName = gameCode, key = "pokemon-names", default = 0)
            )
        )
        return this
    }


    /**
     * Loads a ROM from the roms attached file, reads the metadata, and sets the ROM object's properties.
     *
     * @receiver The target ROM object.
     * @throws IOException If an error occurs while reading the file.
     */
    @Throws(IOException::class)
    fun Rom.load() = apply {
        HexToString.loadFromYaml(Paths.get("./data/hex-strings.yml").toString())
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