package org.pokerpg.definitions.impl

import org.pokerpg.definitions.Definition
import org.pokerpg.definitions.DefinitionType
import org.pokerpg.rom.Rom

/**
 * @author Alycia <https://github.com/alycii>
 */
object PokemonNames : Definition<String>(DefinitionType.PokemonNames) {

    /**
     * Returns the Pokémon name corresponding to the given index.
     *
     * @param index The index of the Pokémon (0-based).
     * @return The name of the Pokémon at the specified index, or `null` if the index is out of range.
     */
    private fun Rom.getPokemonName(index: Int): String {
        // Name length is fixed at 11 characters
        val nameLength = 11

        // Calculate adjusted index if the ROM is patched
        val adjustedIndex = if (patched) index + 10 else index

        // Calculate the offset for the Pokémon name in the ROM
        val nameOffset = addresses.pokemonNames + (adjustedIndex * nameLength)

        // If the ROM is patched, add 2 to the offset
        val finalOffset = if (patched) nameOffset + 2 else nameOffset

        // Read and return the Pokémon name from the ROM buffer
        return buffer.readPokemonString(finalOffset, nameLength)
    }

    override fun getDefinition(rom: Rom, index: Int): String {
        return rom.getPokemonName(index)
    }

}