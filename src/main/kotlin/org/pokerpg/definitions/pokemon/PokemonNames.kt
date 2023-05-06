package org.pokerpg.definitions.pokemon

import org.pokerpg.rom.Rom

/**
 * @author Alycia <https://github.com/alycii>
 */
object PokemonNames {

    /**
     * Returns the Pokémon name corresponding to the given index.
     *
     * @param index The index of the Pokémon (0-based).
     * @return The name of the Pokémon at the specified index, or `null` if the index is out of range.
     */
    fun Rom.getPokemonName(index: Int): String {
        val offset = when(patched) {
            true -> {
                val adjustedIndex = index + 10 // Add 10 to adjust the index
                (addresses.pokemonNames + (adjustedIndex * 11) + 2)
            }
            false -> {
                (addresses.pokemonNames + (index * 11))
            }
        }
        return buffer.readPokemonString(offset, 11)
    }
}