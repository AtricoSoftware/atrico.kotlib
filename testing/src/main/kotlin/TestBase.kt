package atrico.kotlib.testing

import kotlin.math.abs
import kotlin.random.Random

/**
 * Base class for all unit tests
 */
abstract class TestBase {
    protected val random = Random.Default

    /**
     * Test cases for all [Boolean] values
     */
    protected val booleanTestCases = listOf(true, false)

    // region Random values

    /**
     * Generate a random string
     */
    protected fun randomString(length: Int = 5): String =
        random.nextBytes(length).map { byteToChar(it) }.joinToString("")

    private fun byteToChar(byte: Byte) = when (val windowed = abs(byte.toInt()) % 62) {
        in 0..25 -> (windowed + 'A'.toInt()).toChar()
        in 26..51 -> (windowed - 26 + 'a'.toInt()).toChar()
        in 52..61 -> (windowed - 52 + '0'.toInt()).toChar()
        else -> {
            // Cannot reach here
            '*'
        }
    }

    /**
     * Get unique random values
     */
    protected fun <T> uniqueValues(count: Int, generator: () -> T): List<T> {
        val alreadyDone = mutableSetOf<T>()
        return (1..count).map {
            var value: T
            do {
                value = generator();
            } while (alreadyDone.contains(value))
            alreadyDone.add(value)
            value
        }
    }
    // endregion Random values
}
