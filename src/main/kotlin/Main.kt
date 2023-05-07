import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom
import org.pokerpg.rom.RomLoader.load
import org.pokerpg.util.PIKACHU
import java.nio.file.Paths

/**
 * Main entry point for the application.
 *
 * @author Alycia <https://github.com/alycii>
 */
fun main() {

    // Direct path to ROM
    val rom = Rom(file = Paths.get("./rom/test.gba").toFile()).load()

    // Log information about the loaded ROM
    Rom.log("Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    /** Try loading a Pokemons name from the `pokemon-names` ROM address. **/
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU).lowercase()}!")
}


