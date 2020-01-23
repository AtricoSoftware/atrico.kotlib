package atrico.kotlib.maths.matrix

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestToString : TestMatrixBase() {

    @Test
    fun testToStringEqual() {
        // Arrange
        val matrix = Matrix.Builder(3, 2)
            .withValue(0, 0, 100.0)
            .withValue(1, 0, 200.0)
            .withValue(2, 0, 300.0)
            .withValue(0, 1, 101.0)
            .withValue(1, 1, 201.0)
            .withValue(2, 1, 301.0)
            .build()
        display("matrix", matrix)
        val expected = listOf("|100.000 101.000|", "|200.000 201.000|", "|300.000 301.000|")

        // Act
        var result = matrix.toMultilineString()

        // Assert
        assertThat("display", result.toList(), equalTo(expected))
    }

    @Test
    fun testToStringPadded() {
        // Arrange
        val matrix = Matrix.Builder(3, 2)
            .withValue(0, 0, 1.0)
            .withValue(1, 0, 20.0)
            .withValue(2, 0, 300.0)
            .withValue(0, 1, 100.0)
            .withValue(1, 1, 20.0)
            .withValue(2, 1, 3.0)
            .build()
        display("matrix", matrix)
        val expected = listOf("|  1.000 100.000|", "| 20.000  20.000|", "|300.000   3.000|")

        // Act
        var result = matrix.toMultilineString()

        // Assert
        assertThat("display", result.toList(), equalTo(expected))
    }

    @Test
    fun testToStringPaddedColumns() {
        // Arrange
        val matrix = Matrix.Builder(3, 2)
            .withValue(0, 0, 1.0)
            .withValue(1, 0, 20.0)
            .withValue(2, 0, 300.0)
            .withValue(0, 1, 10.0)
            .withValue(1, 1, 2.0)
            .withValue(2, 1, 3.0)
            .build()
        display("matrix", matrix)
        val expected = listOf("|  1.000 10.000|", "| 20.000  2.000|", "|300.000  3.000|")

        // Act
        var result = matrix.toMultilineString()

        // Assert
        assertThat("display", result.toList(), equalTo(expected))
    }

    @Test
    fun testToStringPaddedNegative() {
        // Arrange
        val matrix = Matrix.Builder(3, 2)
            .withValue(0, 0, 1.0)
            .withValue(1, 0, 20.0)
            .withValue(2, 0, 300.0)
            .withValue(0, 1, -100.0)
            .withValue(1, 1, -20.0)
            .withValue(2, 1, -3.0)
            .build()
        display("matrix", matrix)
        val expected = listOf("|  1.000 -100.000|", "| 20.000  -20.000|", "|300.000   -3.000|")

        // Act
        var result = matrix.toMultilineString()

        // Assert
        assertThat("display", result.toList(), equalTo(expected))
    }
}



