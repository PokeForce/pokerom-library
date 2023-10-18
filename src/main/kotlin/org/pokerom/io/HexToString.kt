package org.pokerom.io

import org.pokerom.util.Properties
import java.io.File
import java.io.IOException

/**
 * A singleton object responsible for handling Pokémon text
 * conversion between hex values and readable text.
 *
 * @author Alycia <https://github.com/alycii>
 */
object HexToString {

    /**
     * A mutable map that stores the hex values as keys and their
     * corresponding text characters as values, loaded from the YAML file.
     */
    private val hexTable = mutableMapOf<String, String>()


    /**
     * Loads a YAML file containing hex values and their corresponding text
     * and populates the hexTable map.
     *
     * @param filePath The path to the YAML file.
     * @throws IOException if an error occurs during file reading.
     */
    @Throws(IOException::class)
    fun loadFromYaml(filePath: String) {
        val properties = Properties()
        properties.loadYaml(File(filePath))

        for ((key, value) in properties.properties) {
            hexTable[key] = value as? String ?: " "
        }
    }

    /**
     * Converts a byte array of Pokémon text to a string.
     *
     * @return The converted string.
     */
    fun ByteArray.toPokemonString(): String {
        val converted = StringBuilder()
        this.forEach { byte ->
            val unsignedByte = byte.toUByte().toInt()
            val value = hexTable[String.format("%02X", unsignedByte)]
            converted.append(value)
        }

        val text = converted.toString().trim()
        return text.replace("|br|", " ")
    }




    /**
     * Replaces the line break placeholders with actual line breaks.
     *
     * @param text The text to be processed.
     * @return The text with line break placeholders replaced by actual line breaks.
     */
    private fun replaceLineBreaks(text: String): String {
        return text.replace("|br|", "\n")
    }
}
