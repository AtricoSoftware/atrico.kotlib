package atrico.kotlib.maths.matrix

import atrico.kotlib.testing.isFalse
import atrico.kotlib.testing.isTrue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

class TestMisc : TestMatrixBase() {

    // region Equality

    @Test
    fun testEqual() {
        // Arrange
        val (rows, columns, values, matrix1) = randomMatrix()
        val matrix2 = Matrix(rows, columns, values)

        // Act
        val resultEq = matrix1 == matrix2
        val resultNeq = matrix1 != matrix2

        // Assert
        assertThat("==", resultEq, isTrue())
        assertThat("!=", resultNeq, isFalse())
    }

    @Test
    fun testNotEqual() {
        // Arrange
        val (rows, columns, values, matrix1) = randomMatrix()
        val matrix2 = Matrix(rows, columns, values.entries.map { it.key to it.value + 1.0 }.toMap())

        // Act
        val resultEq = matrix1 == matrix2
        val resultNeq = matrix1 != matrix2

        // Assert
        assertThat("==", resultEq, isFalse())
        assertThat("!=", resultNeq, isTrue())
    }

    // endregion

    @Test
    fun testTranspose() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        val lhs = randomMatrix(rows, columns).matrix
        val expected = Matrix.Pos.all(columns, rows).associateWith { lhs.get(it.column, it.row) }
        display("lhs", lhs)

        // Act
        val result = lhs.transpose

        // Assert
        assertMatrix("transpose", result, columns, rows, expected)
    }

    // region Determinant

    @Test
    fun testDeterminantMismatch() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = rows + 1
        val lhs = randomMatrix(rows, columns).matrix
        display("lhs", lhs)

        // Act & Assert
        val ex = assertFailsWith<SizeMismatchException>("exception thrown") {
            lhs.determinant
        }
        println(ex.message)

        // Assert
        assertThat("message", ex.message!!, containsSubstring("determinant"))
        assertThat("message", ex.message!!, containsSubstring("matrix must be square (${lhs.rows} != ${lhs.columns})"))
    }

    @Test
    fun testDeterminant1x1() {
        // Arrange
        val number: Number = Random.nextDouble()
        val lhs = Matrix.Builder(1, 1)
            .withValue(0, 0, number)
            .build()
        display("lhs", lhs)

        // Act
        val result = lhs.determinant
        println("determinant = $result")

        // Assert
        assertThat("determinant", result, equalTo(number))
    }

    @Test
    fun testDeterminant2x2() {
        // Arrange
        // Example from: https://www.mathsisfun.com/algebra/matrix-determinant.html
        val lhs = Matrix.Builder(2, 2)
            .withDps(0)
            .withValue(0, 0, 4)
            .withValue(0, 1, 6)
            .withValue(1, 0, 3)
            .withValue(1, 1, 8)
            .build()
        val expected: Number = 14.0
        display("lhs", lhs)

        // Act
        val result = lhs.determinant
        println("determinant = $result")

        // Assert
        assertThat("determinant", result, equalTo(expected))
    }

    @Test
    fun testDeterminant3x3() {
        // Arrange
        // Example from: https://www.mathsisfun.com/algebra/matrix-determinant.html
        val lhs = Matrix.Builder(3, 3)
            .withDps(0)
            .withValue(0, 0, 6)
            .withValue(0, 1, 1)
            .withValue(0, 2, 1)
            .withValue(1, 0, 4)
            .withValue(1, 1, -2)
            .withValue(1, 2, 5)
            .withValue(2, 0, 2)
            .withValue(2, 1, 8)
            .withValue(2, 2, 7)
            .build()
        val expected: Number = -306.0
        display("lhs", lhs)

        // Act
        val result = lhs.determinant
        println("determinant = $result")

        // Assert
        assertThat("determinant", result, equalTo(expected))
    }

    @Test
    fun testDeterminant5x5() {
        // Arrange
        // Example from: https://math.stackexchange.com/questions/1955784/how-to-find-the-determinant-of-a-5x5-matrix
        val lhs = Matrix.Builder(5, 5)
            .withDps(0)
            .withValue(0, 1, 6)
            .withValue(0, 2, -2)
            .withValue(0, 3, -1)
            .withValue(0, 4, 5)
            .withValue(1, 3, -9)
            .withValue(1, 4, -7)
            .withValue(2, 1, 15)
            .withValue(2, 2, 35)
            .withValue(3, 1, -1)
            .withValue(3, 2, -11)
            .withValue(3, 3, -2)
            .withValue(3, 4, 1)
            .withValue(4, 0, -2)
            .withValue(4, 1, -2)
            .withValue(4, 2, 3)
            .withValue(4, 4, -2)
            .build()
        val expected: Number = 2480.0
        display("lhs", lhs)

        // Act
        val result = lhs.determinant
        println("determinant = $result")

        // Assert
        assertThat("determinant", result, equalTo(expected))
    }

    // endregion

    // region Inverse

    @Test
    fun testInverseMismatch() {
        // Arrange
        val rows = Random.nextInt(2, 10)
        val columns = rows + 1
        val lhs = randomMatrix(rows, columns).matrix
        display("lhs", lhs)

        // Act & Assert
        val ex = assertFailsWith<SizeMismatchException>("exception thrown") {
            lhs.inverse
        }
        println(ex.message)

        // Assert
        assertThat("message", ex.message!!, containsSubstring("inverse"))
        assertThat("message", ex.message!!, containsSubstring("matrix must be square (${lhs.rows} != ${lhs.columns})"))
    }

    @Test
    fun testInverseIdentity() {
        // Arrange
        val size = Random.nextInt(1, 3) // TODO - extend to more than 2x2
        val lhs = Matrix.identity(size)
        display("lhs", lhs)

        // Act
        val result = lhs.inverse
        val result2 = result.inverse

        // Assert
        display("inverse", result)
        assertThat("inverse", result, equalTo(lhs))
        display("double inverse", result2)
        assertThat("double inverse", result2, equalTo(lhs))
    }

    @Test
    fun testInverse1x1() {
        // Arrange
        val number = Random.nextDouble()
        val lhs = Matrix.Builder(1, 1)
            .withValue(0, 0, number)
            .build()
        val expected = mapOf(Matrix.Pos(0, 0) to 1.0 / number)
        display("lhs", lhs)

        // Act
        val result = lhs.inverse
        val result2 = result.inverse

        // Assert
        assertMatrix("inverse", result, 1, 1, expected)
        display("double inverse", result2)
        assertThat("double inverse", result2, equalTo(lhs))
    }

    @Test
    fun testInverse2x2() {
        // Arrange
        // Example from: https://www.mathsisfun.com/algebra/matrix-inverse.html
        val lhs = Matrix.Builder(2, 2)
            .withValue(0, 0, 4)
            .withValue(0, 1, 7)
            .withValue(1, 0, 2)
            .withValue(1, 1, 6)
            .build()
        val expected = Matrix.Builder(2, 2)
            .withValue(0, 0, 0.6)
            .withValue(0, 1, -0.7)
            .withValue(1, 0, -0.2)
            .withValue(1, 1, 0.4)
            .build()
        display("lhs", lhs)

        // Act
        val result = lhs.inverse
        val result2 = result.inverse

        // Assert
        display("inverse", result)
        assertThat("inverse", result, equalTo(expected))
        display("double inverse", result2)
        assertThat("double inverse", result2, equalTo(lhs))
    }

    // endregion
}



