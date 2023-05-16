import org.pokerpg.definitions.DefinitionType
import org.pokerpg.definitions.impl.Item
import org.pokerpg.definitions.impl.MapHeader
import org.pokerpg.rom.Rom
import org.pokerpg.rom.RomLoader.getAddress
import org.pokerpg.rom.RomLoader.load
import org.pokerpg.util.PIKACHU
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
    val rom = Rom(file = Paths.get("./rom/test_patched.gba").toFile()).load()

    /** Log the ROMs game information **/
    Rom.log("Loaded ${rom.gameCode} ${rom.gameName} by ${rom.gameCreator}")

    /** Sample loading of a Pokémon name. **/
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU)?.lowercase()}!")

    /** Sample loading of an item **/
    val item = rom.definition<Item>(type = DefinitionType.Items, index = 1) ?: Item("null")
    Rom.log("[item = ${item.name}, price = ${item.price}, description = ${item.description}]")
    saveImage(item.sprite, item.name.replace(" ", "_").lowercase())

    /** Load the Map Banks **/
    val mapHeader = rom.definition<MapHeader>(type = DefinitionType.MapHeader, index = 3, subIndex = 0) ?: return
    val position = rom.getAddress("map-label") + (mapHeader.label - 0x58) * 4
    val namePosition = rom.buffer.readInt(position)
    val name = rom.buffer.readPokemonString(namePosition, -1)
    Rom.log("Welcome to $name")
}

@Throws(IOException::class)
private fun saveImage(image: RenderedImage, filename: String) {
    val file = File("./data/test/", "$filename.png")
    ImageIO.write(image, "png", file)
}


