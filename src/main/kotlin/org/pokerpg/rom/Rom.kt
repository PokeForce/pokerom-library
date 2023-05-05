package org.pokerpg.rom

import org.pokerpg.buffer.Buffer
import org.pokerpg.game.Game
import java.io.File
import java.util.logging.Logger

/**
 * @author Alycia <https://github.com/alycii>
 */
class Rom(var file: File) {

    /**
     * Gets the Game code from the ROM
     */
    lateinit var gameCode: String

    /**
     * Gets the Game version from the ROM
     */
    lateinit var game: Game

    /**
     * Gets the Games name from the ROM
     */
    lateinit var gameName: String

    /**
     * Gets the Games creator identification as a string
     * Example: 01 is GameFreak's Company ID
     */
    lateinit var gameCreator: String

    /**
     * If the ROM has had a patch attached to it
     */
    private var patched: Boolean = false

    /**
     * If the ROM has had RTC attached to it
     */
    private var rtc: Boolean = false

    /**
     * The Buffer attached to this ROM
     */
    val buffer: Buffer
        get() = Buffer(this)

    /**
     * The data the rom has provided
     */
    lateinit var data: ByteArray

    /**
     * Update flags for the ROM if it is a specific
     * game version
     */
    fun setFlags() {
        when(gameCode) {
            "BPRE" -> {
                patched = true
            }
            "BPEE" -> {
                rtc = true
            }
        }
    }

    /**
     * The logger
     */
    val logger: Logger = Logger.getLogger(Rom::class.java.name)

}