package atrico.kotlib.maths

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TestFactors {
    @TestFactory
    fun testCalculateFactors() = testcases.map { (value, expected) ->
        DynamicTest.dynamicTest("Factors of $value are $expected")
        {
            // Act
            val result = calculateFactors(value)

            // Assert
            assertThat(result, equalTo(expected))
        }
    }

    private val testcases = listOf(
        Pair(1, listOf(1)),
        Pair(2, listOf(1, 2)),
        Pair(3, listOf(1, 3)),
        Pair(4, listOf(1, 2, 4)),
        Pair(5, listOf(1, 5)),
        Pair(6, listOf(1, 2, 3, 6)),
        Pair(7, listOf(1, 7)),
        Pair(8, listOf(1, 2, 4, 8)),
        Pair(9, listOf(1, 3, 9)),
        Pair(10, listOf(1, 2, 5, 10)),
        Pair(11, listOf(1, 11)),
        Pair(12, listOf(1, 2, 3, 4, 6, 12)),
        Pair(13, listOf(1, 13)),
        Pair(14, listOf(1, 2, 7, 14)),
        Pair(15, listOf(1, 3, 5, 15)),
        Pair(16, listOf(1, 2, 4, 8, 16)),
        Pair(17, listOf(1, 17)),
        Pair(18, listOf(1, 2, 3, 6, 9, 18)),
        Pair(19, listOf(1, 19)),
        Pair(20, listOf(1, 2, 4, 5, 10, 20))
    )
}


