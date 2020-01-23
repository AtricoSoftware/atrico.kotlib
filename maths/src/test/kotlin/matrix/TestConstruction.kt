package atrico.kotlib.maths.matrix

import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestConstruction : TestMatrixBase() {
    @Test
    fun testConstruct() {
        // Arrange
        val (rows, columns, values) = randomMatrix()

        // Act
        val matrix = Matrix(rows, columns, values)

        // Assert
        assertMatrix("ctor", matrix, rows, columns, values)
    }

    @Test
    fun testIdentity() {
        // Arrange
        val size = Random.nextInt(1, 10)

        // Act
        val matrix = Matrix.identity(size)

        // Assert
        val expected = (0 until size)
            .flatMap { row ->
                (0 until size).map { col ->
                    Matrix.Pos(row, col) to if (row == col) 1 else 0
                }
            }.toMap()
        assertMatrix("identity", matrix, size, size, expected)
    }
}



