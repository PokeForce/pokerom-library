import org.pokerpg.definitions.DefinitionType
import org.pokerpg.definitions.impl.Item
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

    /** Load the rom from the rom folder **/
    val rom = Rom(file = Paths.get("./rom/test.gba").toFile()).load()

    /** Log the ROMs game information **/
    Rom.log("Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    /** Sample loading of a Pokemons name. **/
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU).lowercase()}!")

    /** Sample loading of an item **/
    val item = rom.definition<Item>(type = DefinitionType.Items, index = 4)
    Rom.log(item.toString())
}


