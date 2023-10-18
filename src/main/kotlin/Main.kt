import org.pokerom.definitions.DefinitionType
import org.pokerom.definitions.impl.Item
import org.pokerom.definitions.impl.MapHeader
import org.pokerom.rom.Rom
import org.pokerom.rom.RomLoader.getAddress
import org.pokerom.rom.RomLoader.load
import org.pokerom.util.PIKACHU
import java.awt.image.RenderedImage
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Main entry point for the application.
 *
 * @author Alycia <https://github.com/alycii>
 */
fun main() {

    /** Load the rom from the rom folder **/
    val rom = Rom(file = Paths.get("./rom/fire_red.gba").toFile()).load()

    /** Log the ROMs game information **/
    Rom.log("Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    /** Sample loading of a Pokémon name. **/
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU)?.lowercase()}!")

    /** Load the Map Banks **/
    val mapHeader = rom.definition<MapHeader>(type = DefinitionType.MapHeader, index = 3, subIndex = 0) ?: return
    val position = rom.getAddress("map-label") + (mapHeader.label - 0x58) * 4
    val namePosition = rom.buffer.readInt(position)
    val name = rom.buffer.readPokemonString(namePosition, -1)
    Rom.log("Welcome to $name")

    val icon = rom.definition<Icon>(type = DefinitionType.Icons, 1)?.sprite
    saveImage(icon!!, "bulbasaur")
}

@Throws(IOException::class)
private fun saveImage(image: RenderedImage, filename: String) {
    val file = File("./data/pkmn/", "$filename.png")
    ImageIO.write(image, "png", file)
}


