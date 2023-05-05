package org.pokerpg.buffer

import java.io.*
import java.nio.charset.StandardCharsets

/**
 * A utility class for converting Poketext to ASCII.
 * @since 30/04/2017
 *
 * TODO: Update this to use different file format
 * Note, Ally: Would've used .yaml but it has a thing against special characters I suppose
 */
object PokemonText {
    private val hexTable = mutableMapOf<String, String>()

    /**
     * Loads the default PokeTable from this JAR's resources
     * @throws IOException If the PokeTable isn't stored in this JAR
     */
    @Throws(IOException::class)
    fun loadFromJar() {
        PokemonText::class.java.getResourceAsStream("poketable.ini")?.use { inStream ->
            loadFromStream(inStream)
        } ?: throw IOException("Cannot find internal PokeTable in PokeData JAR")
    }

    /**
     * Load a HEX table file for character mapping i.e. Pokétext
     * @param tableFile File that contains the character table
     */
    @Throws(IOException::class)
    fun loadFromFile(tableFile: File) {
        FileInputStream(tableFile).use { fileStream ->
            loadFromStream(fileStream)
        }
    }

    private fun loadFromStream(inputStream: InputStream) {
        BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val separated = line!!.split("=")
                val key = separated[0]
                val value = separated.getOrElse(1) { " " }
                hexTable[key] = value
            }
        }
    }

    /**
     * Convert Poketext to ASCII, takes an array of bytes of Poketext.
     * @param pokeText Poketext as a byte array
     * @return The results from the given HEX Table <- must {@link #loadFromFile(File)} first
     */
    fun toAscii(pokeText: ByteArray): String {
        val converted = StringBuilder()
        pokeText.forEach { byte ->
            val value = hexTable[String.format("%02X", byte)]
            converted.append(value)
        }

        val text = converted.toString().trim()
        return replaceLineBreaks(text)
    }

    private fun replaceLineBreaks(text: String): String {
        return text.replace("|br|", "\n")
    }
}
