package org.pokerpg.rom

import org.pokerpg.buffer.Buffer
import org.pokerpg.buffer.PokemonText
import java.io.File
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Represents a Pokémon ROM, containing information about the game, its addresses, and the attached buffer.
 *
 * @property file The file containing the ROM data.
 * @author Alycia <https://github.com/alycii>
 */
class Rom(var file: File) {

    /**
     * Initializes the class by loading the Pokémon text data from the poketable.ini file.
     */
    init {
        PokemonText.loadFromFile(Paths.get("./data/poketable.ini").toFile())
    }


    /**
     * The game code of the ROM.
     */
    lateinit var gameCode: String

    /**
     * The name of the game in the ROM.
     */
    lateinit var gameName: String

    /**
     * The game creator's identification as a string (e.g., "01" is GameFreak's company ID).
     */
    lateinit var gameCreator: String

    /**
     * Indicates whether the ROM has been patched.
     */
    var patched: Boolean = false

    /**
     * Indicates whether the ROM has a real-time clock.
     */
    private var realTimeClock: Boolean = false

    /**
     * The buffer attached to this ROM.
     */
    val buffer: Buffer
        get() = Buffer(this)

    /**
     * The data provided by the ROM.
     */
    lateinit var data: ByteArray

    /**
     * The addresses where specific information about the ROM can be found.
     * Examples: Pokémon names, sprites, maps, etc.
     */
    var addresses = RomAddresses()

    /**
     * Updates flags for the ROM if it is a specific game version.
     */
    fun setFlags() {
        when (gameCode) {
            "BPRE" -> {
                if (buffer.readByte(0x427).toInt() == 28) {
                    patched = true
                }
            }
        }
    }
    companion object {

        /**
         * The logger for the Rom class.
         */
        private val logger: Logger = Logger.getLogger(Rom::class.java.name)

        /**
         * Logs the provided message at the INFO level using the logger.
         *
         * @param message The message to be logged.
         */
        fun log(message: String) {
            logger.log(Level.INFO, message)
        }
    }

}
