import org.pokerpg.buffer.PokemonText
import org.pokerpg.rom.Rom
import org.pokerpg.rom.RomLoader.load
import org.pokerpg.rom.RomLoader.loadAddresses
import java.nio.file.Paths
import java.util.logging.Level

/**
 * Main entry point for the application.
 *
 * @param args command-line arguments
 */
fun main(args: Array<String>) {

    // Direct path to ROM
    val rom = Rom(file = Paths.get(args[0]).toFile())

    // Load PokeText character mappings from file
    PokemonText.loadFromFile(Paths.get("./data/poketable.ini").toFile())

    // Load ROM addresses from file
    rom.loadAddresses()

    // Load ROM data
    rom.load()

    // Log information about the loaded ROM
    rom.logger.log(Level.INFO, "Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    // Read the name of the 151st Pokémon from ROM data (Mew)
    val offset = (rom.addresses.pokemonNames + (151 * 11))
    rom.logger.log(Level.INFO, "${rom.addresses.pokemonNames} - ${rom.buffer.readPokemonString(offset)}")
}


