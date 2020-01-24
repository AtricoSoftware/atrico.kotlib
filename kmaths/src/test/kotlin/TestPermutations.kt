package atrico.kotlib.maths

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertFails

class TestPermutations {
    @TestFactory
    fun testCalculateCombinationsErrors() = errors.map { (elements, count, expected) ->
        DynamicTest.dynamicTest("Combinations of $count from $elements gives error $expected")
        {
            // Act
            val ex = assertFails { calculateCombinations(elements, count) }
            // Assert
            assertThat(ex.message, equalTo(expected))
        }
    }

    @TestFactory
    fun testCalculatePermutationsErrors() = errors.map { (elements, count, expected) ->
        DynamicTest.dynamicTest("Permutations of $count from $elements gives error $expected")
        {
            // Act
            val ex = assertFails { calculatePermutations(elements, count) }
            // Assert
            assertThat(ex.message, equalTo(expected))
        }
    }

    private val errors = listOf(
        // Empty elements
        Triple(emptyList(), 1, "Elements cannot be empty"),
        // Count <= 0
        Triple(listOf(1, 2, 3), 0, "Count must be greater than 0"),
        Triple(listOf(1), -2, "Count must be greater than 0"),
        // Count lager than elements
        Triple(listOf(1), 2, "Count must be less than or equal to elements count"),
        Triple(listOf(1, 2, 3), 4, "Count must be less than or equal to elements count")
    )


    @TestFactory
    fun testCalculateCombinations() = combinations.map { (values, count, expected) ->
        DynamicTest.dynamicTest("Combinations of $count of $values are $expected")
        {
            // Act
            val result = calculateCombinations(values, count).toList()

            // Assert
            assertThat(result, equalTo(expected))
        }
    }

    private data class TestCase<T>(val values: Collection<T>, val count: Int, val expected: List<Iterable<T>>)

    private val combinations = listOf(
        TestCase(listOf(1), 1, listOf(listOf(1))),
        TestCase(listOf(1, 2), 1, listOf(listOf(1), listOf(2))),
        TestCase(listOf("1", "2"), 2, listOf(listOf("1", "2"))),
        TestCase(listOf(1.0, 2.0, 3.0), 1, listOf(listOf(1.0), listOf(2.0), listOf(3.0))),
        TestCase(
            listOf('1', '2', '3'), 2, listOf(
                listOf('1', '2'), listOf('1', '3'), listOf('2', '3')
            )
        ),
        TestCase(
            listOf(1, 2, 3), 3, listOf(listOf(1, 2, 3))
        )
    )

    @TestFactory
    fun testCalculatePermutations() = permutations.map { (values, count, expected) ->
        DynamicTest.dynamicTest("Permutations of $count of $values are $expected")
        {
            // Act
            val result = calculatePermutations(values, count).toList()

            // Assert
            assertThat(result, equalTo(expected))
        }
    }

    private val permutations = listOf(
        TestCase(listOf(1), 1, listOf(listOf(1))),
        TestCase(listOf(1, 2), 1, listOf(listOf(1), listOf(2))),
        TestCase(listOf("1", "2"), 2, listOf(listOf("1", "2"), listOf("2", "1"))),
        TestCase(listOf(1.0, 2.0, 3.0), 1, listOf(listOf(1.0), listOf(2.0), listOf(3.0))),
        TestCase(
            listOf('1', '2', '3'), 2, listOf(
                listOf('1', '2'),
                listOf('1', '3'),
                listOf('2', '1'),
                listOf('2', '3'),
                listOf('3', '1'),
                listOf('3', '2')
            )
        ),
        TestCase(
            listOf(1, 2, 3), 3, listOf(
                listOf(1, 2, 3),
                listOf(1, 3, 2),
                listOf(2, 1, 3),
                listOf(2, 3, 1),
                listOf(3, 1, 2),
                listOf(3, 2, 1)
            )
        )
    )
}


