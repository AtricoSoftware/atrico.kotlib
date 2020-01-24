package atrico.kotlib.maths

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TestFactorials {
    @TestFactory
    fun testFactorials() = testcases.map { (value, expected) ->
        DynamicTest.dynamicTest("Factors of $value are $expected")
        {
            // Act
            val result = calculateFactorial(value)

            // Assert
            assertThat(result, equalTo(expected))
        }
    }

    private val testcases = listOf(
        Pair(0, 1),
        Pair(1, 1),
        Pair(2, 2),
        Pair(3, 6),
        Pair(4, 24),
        Pair(5, 120),
        Pair(6, 720),
        Pair(7, 5040),
        Pair(8, 40320),
        Pair(9, 362880),
        Pair(10, 3628800)
    )
}


