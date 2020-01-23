package atrico.kotlib.maths.matrix

import atrico.kotlib.number.div
import atrico.kotlib.number.minus
import atrico.kotlib.number.plus
import atrico.kotlib.number.times
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

class TestArithmetic : TestMatrixBase() {

    @Test
    fun testAddMismatch() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(rows + 1, columns + 1).matrix
        display("lhs", lhs)
        display("rhs", rhs)

        // Act & Assert
        val ex = assertFailsWith<SizeMismatchException>("exception thrown") {
            lhs + rhs
        }
        println(ex.message)

        // Assert
        assertThat("message", ex.message!!, containsSubstring("plus"))
        assertThat("message", ex.message!!, containsSubstring("rows must match (${lhs.rows} != ${rhs.rows})"))
        assertThat("message", ex.message!!, containsSubstring("columns must match (${lhs.columns} != ${rhs.columns})"))
    }

    @Test
    fun testAdd() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(rows, columns).matrix
        val expected = Matrix.Pos.all(rows, columns).associateWith { lhs.get(it) + rhs.get(it) }
        display("lhs", lhs)
        display("rhs", rhs)

        // Act
        val result = lhs + rhs

        // Assert
        assertMatrix("plus", result, rows, columns, expected)
    }

    @Test
    fun testSubtractMismatch() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(rows + 1, columns + 1).matrix
        display("lhs", lhs)
        display("rhs", rhs)

        // Act & Assert
        val ex = assertFailsWith<SizeMismatchException>("exception thrown") {
            lhs - rhs
        }
        println(ex.message)

        // Assert
        assertThat("message", ex.message!!, containsSubstring("minus"))
        assertThat("message", ex.message!!, containsSubstring("rows must match (${lhs.rows} != ${rhs.rows})"))
        assertThat("message", ex.message!!, containsSubstring("columns must match (${lhs.columns} != ${rhs.columns})"))
    }

    @Test
    fun testSubtract() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(rows, columns).matrix
        val expected = Matrix.Pos.all(rows, columns).associateWith { lhs.get(it) - rhs.get(it) }
        display("lhs", lhs)
        display("rhs", rhs)

        // Act
        val result = lhs - rhs

        // Assert
        assertMatrix("minus", result, rows, columns, expected)
    }

    @Test
    fun testMultiplyByScalar() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = Random.nextDouble()
        val expected = Matrix.Pos.all(rows, columns).associateWith { lhs.get(it) * rhs }
        display("lhs", lhs)
        println(rhs)

        // Act
        val result = lhs * rhs

        // Assert
        assertMatrix("times", result, rows, columns, expected)
    }

    @Test
    fun testDivideByScalar() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = Random.nextDouble()
        val expected = Matrix.Pos.all(rows, columns).associateWith { lhs.get(it) / rhs }
        display("lhs", lhs)
        println(rhs)

        // Act
        val result = lhs / rhs

        // Assert
        assertMatrix("div", result, rows, columns, expected)
    }

    @Test
    fun testMultiplyMismatch() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(columns + 1, rows + 1).matrix
        display("lhs", lhs)
        display("rhs", rhs)

        // Act & Assert
        val ex = assertFailsWith<SizeMismatchException>("exception thrown") {
            lhs * rhs
        }
        println(ex.message)

        // Assert
        assertThat("message", ex.message!!, containsSubstring("times"))
        assertThat("message", ex.message!!, containsSubstring("columns must match rows(${lhs.columns} != ${rhs.rows})"))
    }

    @Test
    fun testMultiply() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val rhs = randomMatrix(columns, rows).matrix
        val expected = Matrix.Pos.all(rows, columns).associateWith { pos ->
            (0 until columns).fold(0 as Number) { total, idx ->
                total + (lhs.get(pos.row, idx) * rhs.get(
                    idx,
                    pos.column
                ))
            }
        }
        display("lhs", lhs)
        display("rhs", rhs)

        // Act
        val result = lhs * rhs

        // Assert
        assertMatrix("times", result, rows, columns, expected)
    }

    @Test
    fun testMultiplyByIdentity() {
        // Arrange
        val size = Random.nextInt(2, 10)
        val (_, _, values, lhs) = randomMatrix(size, size)
        val rhs = Matrix.identity(size)
        display("lhs", lhs)
        display("rhs", rhs)

        // Act
        val result = lhs * rhs

        // Assert
        assertMatrix("times", result, size, size, values)
    }

}
