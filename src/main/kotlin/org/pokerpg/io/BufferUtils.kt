package org.pokerpg.io

import kotlin.math.absoluteValue

/**
 * A utility object that provides methods for converting and manipulating byte arrays and integers.
 *
 * @author Alycia <https://github.com/alycii>
 */
@Suppress("UNUSED")
object BufferUtils {

    /**
     * Converts a byte array to a 32-bit integer (Long).
     *
     * @param bytes The input byte array.
     * @return The converted 32-bit integer (Long).
     */
    fun toLong(bytes: ByteArray): Long {
        return ((bytes[0].toInt() and 0xFF) +
                ((bytes[1].toInt() and 0xFF) shl 8) +
                ((bytes[2].toInt() and 0xFF) shl 16) +
                ((bytes[3].toInt() and 0xFF) shl 24)).toLong()
    }

    /**
     * Shortens a pointer by truncating the leftmost bits, leaving only the rightmost 25 bits.
     *
     * @param pointer The input pointer value (Long).
     * @return The shortened pointer value (Int).
     */
    fun shortenPointer(pointer: Long): Int {
        return (pointer and 0x1FFFFFF).toInt()
    }

    /**
     * Converts a 32-bit integer (Long) to a byte array.
     *
     * @param i The input 32-bit integer (Long).
     * @return The resulting byte array.
     */
    fun getBytes(i: Long): ByteArray {
        return byteArrayOf(
            ((i and 0xFF000000) shr 24).toByte(),
            ((i and 0x00FF0000) shr 16).toByte(),
            ((i and 0x0000FF00) shr 8).toByte(),
            (i and 0x000000FF).toByte()
        )
    }

    /**
     * Converts a 32-bit integer (Long) to an integer array.
     *
     * @param i The input 32-bit integer (Long).
     * @return The resulting integer array.
     */
    fun getInts(i: Long): IntArray {
        return intArrayOf(
            ((i and 0xFF000000) shr 24).toInt(),
            ((i and 0x00FF0000) shr 16).toInt(),
            ((i and 0x0000FF00) shr 8).toInt(),
            (i and 0x000000FF).toInt()
        )
    }

    /**
     * Reverses the order of a byte array.
     *
     * @param bytes The input byte array.
     * @return The reversed byte array.
     */
    fun reverseBytes(bytes: ByteArray): ByteArray {
        return bytes.reversedArray()
    }

    /**
     * Retrieves a portion of a byte array starting at the specified offset.
     *
     * @param array The input byte array.
     * @param offset The starting index.
     * @param length The number of bytes to retrieve.
     * @return The resulting byte array.
     */
    fun getBytes(array: ByteArray, offset: Int, length: Int): ByteArray {
        return array.copyOfRange(offset, offset + length)
    }

    /**
     * Grabs a portion of a byte array starting at the specified offset and converts it to an integer array.
     *
     * @param array The input byte array.
     * @param offset The starting index.
     * @param length The number of bytes to grab.
     * @return The resulting integer array.
     */
    fun grabBytesAsInts(array: ByteArray, offset: Int, length: Int): IntArray {
        return array.copyOfRange(offset, offset + length).map { it.toInt() and 0xFF }.toIntArray()
    }

    /**
     * Inserts a byte array into another byte array at the specified offset.
     *
     * @param array The target byte array.
     * @param toPut The byte array to insert.
     * @param offset The starting index.
     * @return The modified target byte array.
     */
    fun putBytes(array: ByteArray, toPut: ByteArray, offset: Int): ByteArray {
        toPut.copyInto(array, offset)
        return array
    }

    /**
     * Converts a byte array to an integer array.
     *
     * @param array The input byte array.
     * @return The resulting integer array.
     */
    fun toInts(array: ByteArray): IntArray {
        return array.map { it.toInt() and 0xFF }.toIntArray()
    }

    /**
     * Converts an integer value to a hexadecimal string.
     *
     * @param loc The input integer value.
     * @param spacing Determines whether the resulting string should be zero-padded.
     * @return The hexadecimal string representation of the integer value.
     */
    fun toHexString(loc: Int, spacing: Boolean = false): String {
        return if (spacing) {
            String.format("%02X", loc.absoluteValue)
        } else {
            String.format("%X", loc.absoluteValue)
        }
    }

    /**
     * Converts an integer value to a 6-digit zero-padded hexadecimal string.
     *
     * @param b The input integer value.
     * @param spacing Determines whether the resulting string should be zero-padded.
     * @return The 6-digit zero-padded hexadecimal string representation of the integer value.
     */
    fun toDwordString(b: Int, spacing: Boolean = false): String {
        return if (spacing) {
            String.format("%06X", b.absoluteValue)
        } else {
            String.format("%X", b.absoluteValue)
        }
    }

    /**
     * Converts an integer value to a hexadecimal string, excluding zeros.
     *
     * @param b The input integer value.
     * @return The hexadecimal string representation of the integer value, excluding zeros.
     */
    fun byteToStringNoZero(b: Int): String {
        return if (b != 0) {
            String.format("%X", b.absoluteValue)
        } else {
            ""
        }
    }

    /**
     * Converts an integer array to a byte array.
     *
     * @param data The input integer array.
     * @return The resulting byte array.
     */
    fun toBytes(data: IntArray): ByteArray {
        return data.map { it.toByte() }.toByteArray()
    }

    /**
     * Checks whether all elements in an integer array are zero.
     *
     * @param words The input integer array.
     * @return True if all elements are zero, false otherwise.
     */
    fun zero(vararg words: Int): Boolean {
        return words.all { it == 0 }
    }

    /**
     * Determines if a specific bit is set in a byte.
     *
     * @param value The input byte value.
     * @param index The bit index to check.
     * @return True if the specified bit is set, false otherwise.
     */
    fun isBitSet(value: Byte, index: Int): Boolean {
        return (value.toInt() and (1 shl index)) != 0
    }

    /**
     * Retrieves the value of a specific bit range from an integer value.
     *
     * @param value The input integer value.
     * @param from The starting bit index (counting from the left).
     * @param to The ending bit index (counting from the left).
     * @return The value of the specified bit range.
     */
    fun getBitRange(value: Int, from: Int, to: Int): Int {
        val adjustedFrom: Int = 16 - to
        val adjustedTo: Int = 16 - from

        val bits = adjustedTo - adjustedFrom
        val rightShifted = value shr adjustedFrom
        val mask = (1 shl bits) - 1

        return rightShifted and mask
    }
}
