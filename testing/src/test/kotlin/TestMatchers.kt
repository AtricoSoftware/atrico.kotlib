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
            val matcher = isTrue()

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
            val matcher = isFalse()

            // Act
            val result = matcher.invoke(value).toString()
            println(result)

            // Assert
            val expectedStr = if (expected) "Match" else "Mismatch[\"was: $value\"]"
            assertThat(result, equalTo(expectedStr))
        }
    }
}