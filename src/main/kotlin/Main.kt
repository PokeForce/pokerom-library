import org.pokerpg.rom.RomLoader.load
import org.pokerpg.rom.Rom
import java.io.File
import java.util.logging.Level

fun main(args: Array<String>) {
    // Direct path to ROM
    val rom = Rom(file = File(args[0]))
    rom.load()
    rom.logger.log(Level.INFO, "Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")
}

