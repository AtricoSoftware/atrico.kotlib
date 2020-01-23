package atrico.kotlib.maths.matrix

import atrico.kotlib.multilineDisplay.displayMultiline
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import kotlin.random.Random

abstract class TestMatrixBase {

    protected fun assertMatrix(
        title: String,
        matrix: Matrix,
        rows: Int,
        columns: Int,
        values: Map<Matrix.Pos, Number>
    ) {
        display(title, matrix)
        assertThat("rows", matrix.rows, equalTo(rows))
        assertThat("columns", matrix.columns, equalTo(columns))
        values.forEach {
            assertThat("value: $it.key", matrix.get(it.key), equalTo(it.value))
        }
    }

    protected fun display(title: String, matrix: Matrix) {
        println(title)
        println("${matrix.rows} x ${matrix.columns}")
        matrix.displayMultiline()
        println()
    }

    protected fun randomValues(rows: Int, columns: Int): Map<Matrix.Pos, Double> {
        return (0 until rows)
            .flatMap { row ->
                (0 until columns).map { col ->
                    Matrix.Pos(row, col) to Random.nextDouble()
                }
            }.toMap()
    }

    protected data class RandomMatrix(
        val rows: Int,
        val columns: Int,
        val values: Map<Matrix.Pos, Double>,
        val matrix: Matrix
    )

    protected fun randomMatrix(): RandomMatrix {
        val rows = Random.nextInt(2, 10)
        val columns = Random.nextInt(2, 10)
        return randomMatrix(rows, columns)
    }

    protected fun randomMatrix(size: Int): RandomMatrix {
        return randomMatrix(size, size)
    }

    protected fun randomMatrix(rows: Int, columns: Int): RandomMatrix {
        val values = randomValues(rows, columns)
        return RandomMatrix(rows, columns, values, Matrix(rows, columns, values))
    }
}



