package atrico.kotlib.testing

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
}
