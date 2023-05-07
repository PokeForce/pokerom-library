import org.pokerpg.definitions.DefinitionType
import org.pokerpg.util.PIKACHU
import org.pokerpg.rom.Rom
import org.pokerpg.rom.RomLoader.load
import org.pokerpg.rom.RomLoader.loadAddresses
import java.nio.file.Paths

/**
 * Main entry point for the application.
 *
 * @param args command-line arguments
 */
fun main(args: Array<String>) {

    // Direct path to ROM
    val rom = Rom(file = Paths.get(args[0]).toFile())

    // Load ROM addresses from file
    rom.loadAddresses()

    // Load ROM data
    rom.load()

    // Log information about the loaded ROM
    Rom.log("Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    /** Try loading a Pokemons name from the `pokemon-names` ROM address. **/
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU).lowercase()}!")
}


