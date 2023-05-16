package org.pokerpg.rom

import org.pokerpg.definitions.impl.ItemDefinition
import org.pokerpg.definitions.impl.MapGroupDefinition
import org.pokerpg.definitions.impl.MapHeaderDefinition
import org.pokerpg.definitions.impl.PkmnNameDefinition
import org.pokerpg.io.Buffer
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
            PkmnNameDefinition(
                address = intArrayOf(romAddresses.getOrDefault(romName = gameCode, key = "pokemon-names", default = 0))
            ),
            ItemDefinition(
                address = intArrayOf(
                    romAddresses.getOrDefault(romName = gameCode, key = "items", default = 0),
                    romAddresses.getOrDefault(romName = gameCode, key = "item-sprites", default = 0),
                )
            ),
            MapGroupDefinition(
                address = IntArray(4) {
                    romAddresses.getOrDefault(romName = gameCode, key = "bank-$it", default = 0)
                }
            ),
            MapHeaderDefinition()
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
        buffer = Buffer(this)
        gameCode = buffer.readString(0xAC, 4)
        gameName = buffer.readString(0xA0, 12).trim()
        gameCreator = buffer.readString(0xB0, 2)
        setFlags()
        loadDefinitions()
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

            var position = 0
            var n: Int

            while (position < bytes.size) {
                n = inputStream.read(bytes, position, bytes.size - position)
                if (n < 0) break
                position += n
            }

            data = bytes
        }
    }

    /**
     * Retrieves the address of a specific ROM data location by its name.
     *
     * @param addressName The name of the address location in the ROM data (e.g., "map-label", "bank-0").
     * @return The address as an Int, or the default value (0) if the address name is not found.
     *
     * @throws IOException If there is an issue reading the "rom-addresses.yml" file.
     */
    fun Rom.getAddress(addressName: String) : Int {
        val romAddresses = Properties()
        romAddresses.loadRomAddresses(Paths.get("./data/rom-addresses.yml").toFile())
        return romAddresses.getOrDefault(romName = gameCode, key = addressName, default = 0)
    }


}