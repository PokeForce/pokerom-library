package org.pokerpg.definitions.impl

import org.pokerpg.definitions.Definition
import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom

/**
 * @author Alycia <https://github.com/alycii>
 */
class PkmnNameDefinition(vararg address: Int) : Definition<String>(type = DefinitionType.PokemonNames, addresses = address) {

    /**
     * Returns the Pokémon name corresponding to the given index.
     *
     * @param index The index of the Pokémon (0-based).
     * @return The name of the Pokémon at the specified index, or `null` if the index is out of range.
     */
    private fun Rom.getPokemonName(index: Int): String {
        // Name length is fixed at 11 characters
        val nameLength = 11

        // Calculate the position for the Pokémon name in the ROM
        val namePosition = addresses.first() + (index * nameLength)

        // Read and return the Pokémon name from the ROM buffer
        return buffer.readPokemonString(namePosition, nameLength)
    }

    override fun getDefinition(rom: Rom, index: Int): String {
        return rom.getPokemonName(index)
    }

}