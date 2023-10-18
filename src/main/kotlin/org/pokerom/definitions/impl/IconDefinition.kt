import org.pokerom.definitions.Definition
import org.pokerom.definitions.DefinitionType
import org.pokerom.rom.Rom
import org.pokerom.rom.graphics.ImageType
import org.pokerom.rom.graphics.Palette
import org.pokerom.rom.graphics.RomImage
import java.awt.image.RenderedImage

class IconDefinition(vararg address: Int) : Definition<Icon>(type = DefinitionType.Icons, addresses = address) {
    private fun Rom.getIcon(index: Int): Icon {
        val iconAddress = addresses[0]
        val paletteAddress = addresses[1]
        val paletteTableAddress = addresses[2]

        val iconPal = buffer.readByte(paletteTableAddress + index)
        println(iconPal)
        val iconPalOffset = paletteAddress + (iconPal * 32)
        val palette = Palette(ImageType.C16, this, iconPalOffset)

        val pointer = iconAddress + (index * 4)
        buffer.position(pointer)

        val offset = buffer.readInt()
        val sprite = RomImage(palette, buffer.readBytes(offset, 0xFFF), 32, 64).toBufferedImage()
        return Icon(sprite)
    }

    override fun getDefinition(rom: Rom, index: Int): Icon {
        return rom.getIcon(index)
    }

}

data class Icon(val sprite: RenderedImage)

