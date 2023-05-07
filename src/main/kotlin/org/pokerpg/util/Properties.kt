package org.pokerpg.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.File

/**
 * Holds a map of properties that can be used by library at any point.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Properties {

    val properties = hashMapOf<String, Any?>()

    /**
     * Gets the property associated with the [key]. If it cannot be found, it will
     * return the [default] value instead.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(key: String, default: T): T = properties[key] as? T ?: default

    /**
     * Gets the property associated with the [key]. If it cannot be found, it will
     * return null instead.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? = properties[key] as? T

    /**
     * Loads a YAML (.yml) file and puts all the found keys & values
     * into the [properties] map.
     */
    fun loadYaml(file: File): Properties {
        check(properties.isEmpty())

        val mapper = ObjectMapper(YAMLFactory())
        val values = mapper.readValue(file, HashMap<String, Any>().javaClass)

        values.forEach { (key, value) ->
            if (value is String && value.isEmpty()) {
                properties[key] = null
            } else {
                properties[key] = value
            }
        }
        return this
    }

    /**
     * A mutable map that stores ROM names as keys and their corresponding
     * structure properties as values.
     */
    private val romsProperties = mutableMapOf<String, Map<String, Any?>>()

    /**
     * Gets the property associated with the [key] for the specified [romName]. If it cannot
     * be found, it will return the [default] value instead.
     *
     * @param romName The name of the ROM to get the property from.
     * @param key The key associated with the property.
     * @param default The default value to return if the property is not found.
     * @return The property value or the default value if not found.
     */
    fun <T> getOrDefault(romName: String, key: String, default: T): T {
        val romProperties = romsProperties[romName]
        @Suppress("UNCHECKED_CAST")
        return romProperties?.get(key) as? T ?: default
    }

    /**
     * Loads a YAML file containing ROM addresses and populates the [romsProperties] map.
     *
     * @param file The file containing the ROM addresses in YAML format.
     * @return The current instance of the [Properties] class.
     */
    fun loadRomAddresses(file: File): Properties {
        val mapper = ObjectMapper(YAMLFactory())
        val values = mapper.readValue(file, HashMap<String, List<Map<String, Any>>>().javaClass)

        values["roms"]?.forEach { rom ->
            val romName = rom["game-code"] as? String ?: return@forEach
            @Suppress("UNCHECKED_CAST")
            val structure = rom["structure"] as? Map<String, Any?> ?: return@forEach
            romsProperties[romName] = structure
        }

        return this
    }

}