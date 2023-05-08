import org.pokerpg.definitions.DefinitionType
import org.pokerpg.definitions.impl.Item
import org.pokerpg.rom.Rom
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
    Rom.log("I choose you, ${rom.definition<String>(type = DefinitionType.PokemonNames, index = PIKACHU).lowercase()}!")

    /** Sample loading of an item **/
    for(i in 1..10) {
        val item = rom.definition<Item>(type = DefinitionType.Items, index = i)
        Rom.log("[item = ${item.name}, price = ${item.price}, description = ${item.description}]")
        saveImage(item.sprite, item.name.replace(" ", "_").lowercase())
    }

}

@Throws(IOException::class)
private fun saveImage(image: RenderedImage, filename: String) {
    val file = File("./data/test/", "$filename.png")
    ImageIO.write(image, "png", file)
}


