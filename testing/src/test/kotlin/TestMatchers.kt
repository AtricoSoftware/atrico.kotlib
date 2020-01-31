package atrico.kotlib.testing

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TestMatchers : TestBase() {
    @TestFactory
    fun testIsTrue() = booleanTestCases.map { Pair(it, it) }.map { (value, expected) ->
        DynamicTest.dynamicTest("isTrue($value) should equal $expected")
        {
            // Arrange
            val matcher = isTrue

            // Act
            val result = matcher.invoke(value).toString()
            println(result)

            // Assert
            val expectedStr = if (expected) "Match" else "Mismatch[\"was: $value\"]"
            assertThat(result, equalTo(expectedStr))
        }
    }

    @TestFactory
    fun testIsFalse() = booleanTestCases.map { Pair(it, !it) }.map { (value, expected) ->
        DynamicTest.dynamicTest("isFalse($value) should equal $expected")
        {
            // Arrange
            val matcher = isFalse

            // Act
            val result = matcher.invoke(value).toString()
            println(result)

            // Assert
            val expectedStr = if (expected) "Match" else "Mismatch[\"was: $value\"]"
            assertThat(result, equalTo(expectedStr))
        }
    }

    @TestFactory
    fun testIsNotEqual() = equalsTestCases.map { (name, lhs, rhs, equal) ->
        DynamicTest.dynamicTest("$name: $lhs should be ${if (equal) "" else "not "} equal to $rhs")
        {
            // Arrange
            val matcher = notEqualTo(rhs)

            // Act
            val result = matcher.invoke(lhs).toString()
            println(result)

            // Assert
            val expected = if (!equal) "Match" else "Mismatch"
            assertThat(result.startsWith(expected), isTrue)
        }
    }

    @TestFactory
    fun testContains() = collectionTestCases.flatMap { (name, subject, compareTo, messages) ->
        fun generateTest(subject: Iterable<Int>?, compareTo: Iterable<Int>, message: String?) =
            DynamicTest.dynamicTest("$name: $subject Contains $compareTo")
            {
                // Arrange
                val matcher = contains(compareTo)

                // Act
                val result = matcher.invoke(subject).toString()
                println("$subject Contains $compareTo = $result")

                // Assert
                val expectedStr = if (message == null) "Match" else "Mismatch[\"$message\"]"
                assertThat(result, equalTo(expectedStr))
            }
        // If failure, check it fails both ways
        if (messages == null) {
            listOf(
                generateTest(subject, compareTo, null)
            )
        } else {
            if (subject == null) {
                listOf(
                    generateTest(subject, compareTo, messages.forward)
                )
            } else {
                listOf(
                    generateTest(subject, compareTo, messages.forward),
                    generateTest(compareTo, subject, messages.backward)
                )
            }
        }
    }

    @TestFactory
    fun testContainsInAnyOrder() = collectionTestCases.flatMap { (name, subject, compareTo, _, messages) ->
        fun generateTest(subject: Iterable<Int>?, compareTo: Iterable<Int>, message: String?) =
            DynamicTest.dynamicTest("$name: $subject ContainsInAnyOrder $compareTo")
            {
                // Arrange
                val matcher = containsInAnyOrder(compareTo)

                // Act
                val result = matcher.invoke(subject).toString()
                println("$subject ContainsInAnyOrder $compareTo = $result")

                // Assert
                val expectedStr = if (message == null) "Match" else "Mismatch[\"$message\"]"
                assertThat(result, equalTo(expectedStr))
            }
        // If failure, check it fails both ways
        if (messages == null) {
            listOf(
                generateTest(subject, compareTo, null)
            )
        } else {
            if (subject == null) {
                listOf(
                    generateTest(subject, compareTo, messages.forward)
                )
            } else {
                listOf(
                    generateTest(subject, compareTo, messages.forward),
                    generateTest(compareTo, subject, messages.backward)
                )
            }
        }
    }

    data class CollectionTestCase(
        val name: String,
        val subject: Iterable<Int>?,
        val compareTo: Iterable<Int>,
        val contains: ErrorMessages?,
        val containsInAnyOrderError: ErrorMessages?
    )

    class ErrorMessages(val forward: String, val backward: String)

    data class EqualsTestCase(val name: String, val lhs: Any?, val rhs: Any?, val equal: Boolean)

    private val equalsTestCases = listOf(
        EqualsTestCase("Equal", "text", "text", true),
        EqualsTestCase("Equal", 123, 123, true),
        EqualsTestCase("Equal (null)", null, null, true),
        EqualsTestCase("Not Equal", "text", "text2", false),
        EqualsTestCase("Not Equal", 123, 124, false),
        EqualsTestCase("Not Equal (null)", 123, null, false),
        EqualsTestCase("Not Equal (null)", null, 123, false)
    )

    private val collectionTestCases = listOf(
        CollectionTestCase(
            "Null",
            null,
            emptyList(),
            contains = ErrorMessages("is null", ""),
            containsInAnyOrderError = ErrorMessages("is null", "")
        ),
        CollectionTestCase("Empty - Empty", emptyList(), emptyList(), contains = null, containsInAnyOrderError = null),
        CollectionTestCase(
            "Empty - List",
            emptyList(),
            listOf(1, 2, 3),
            contains = ErrorMessages("Missing: ${listOf(1, 2, 3)}", "Extra: ${listOf(1, 2, 3)}"),
            containsInAnyOrderError = ErrorMessages("Missing: ${listOf(1, 2, 3)}", "Extra: ${listOf(1, 2, 3)}")
        ),
        CollectionTestCase(
            "Equal lists",
            listOf(1, 2, 3),
            listOf(1, 2, 3),
            contains = null,
            containsInAnyOrderError = null
        ),
        CollectionTestCase(
            "Extra element",
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            contains = ErrorMessages("Missing: ${listOf(4)}", "Extra: ${listOf(4)}"),
            containsInAnyOrderError = ErrorMessages("Missing: ${listOf(4)}", "Extra: ${listOf(4)}")
        ),
        CollectionTestCase(
            "Wrong order",
            listOf(1, 2, 3),
            listOf(2, 1, 3),
            contains = ErrorMessages("Out of Order: ${listOf(1)}", "Out of Order: ${listOf(2)}"),
            containsInAnyOrderError = null
        ),
        CollectionTestCase(
            "Duplicates Same",
            listOf(1, 2, 3, 3),
            listOf(1, 2, 3, 3),
            contains = null,
            containsInAnyOrderError = null
        ),
        CollectionTestCase(
            "Duplicates Wrong order",
            listOf(1, 2, 3, 3),
            listOf(1, 3, 2, 3),
            contains = ErrorMessages("Out of Order: ${listOf(2)}", "Out of Order: ${listOf(3)}"),
            containsInAnyOrderError = null
        ),
        CollectionTestCase(
            "Duplicates missing",
            listOf(1, 2, 3),
            listOf(1, 2, 3, 3),
            contains = ErrorMessages("Missing: ${listOf(3)}", "Extra: ${listOf(3)}"),
            containsInAnyOrderError = ErrorMessages("Missing: ${listOf(3)}", "Extra: ${listOf(3)}")
        ),
        CollectionTestCase(
            "Wrong duplicates",
            listOf(1, 2, 3, 3),
            listOf(1, 2, 2, 3),
            contains = ErrorMessages(
                "Missing: ${listOf(2)}, Extra: ${listOf(3)}",
                "Missing: ${listOf(3)}, Extra: ${listOf(2)}"
            ),
            containsInAnyOrderError = ErrorMessages(
                "Missing: ${listOf(2)}, Extra: ${listOf(3)}",
                "Missing: ${listOf(3)}, Extra: ${listOf(2)}"
            )
        ),
        CollectionTestCase(
            "One out of order",
            listOf(1, 3, 4, 2),
            listOf(1, 2, 3, 4),
            contains = ErrorMessages("Out of Order: ${listOf(2)}", "Out of Order: ${listOf(2)}"),
            containsInAnyOrderError = null
        ),
        CollectionTestCase(
            "Extra duplicates",
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4, 4),
            contains = ErrorMessages("Missing: ${listOf(4, 4)}", "Extra: ${listOf(4, 4)}"),
            containsInAnyOrderError = ErrorMessages("Missing: ${listOf(4, 4)}", "Extra: ${listOf(4, 4)}")
        ),
        CollectionTestCase(
            "Two out of order",
            listOf(1, 5, 3, 4, 2, 6),
            listOf(1, 2, 3, 4, 5, 6),
            contains = ErrorMessages("Out of Order: ${listOf(5, 2)}", "Out of Order: ${listOf(2, 5)}"),
            containsInAnyOrderError = null
        )
    )
}

