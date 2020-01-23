package atrico.kotlib.maths.matrix

import atrico.kotlib.maths.DoublesHelper
import atrico.kotlib.number.*
import atrico.kotlib.multilineDisplay.MultilineDisplayable
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min

class Matrix(val rows: Int, val columns: Int, private val values: ValueProvider, dps: Int = DoublesHelper.DefaultDps) :
    MultilineDisplayable {
    private val doublesHelper = DoublesHelper(dps)
    private val widthAdjustment = dps + (if (dps > 0) 1 else 0)

    // Position in matrix
    data class Pos(val row: Int, val column: Int) {
        companion object {
            fun all(rows: Int, columns: Int): Sequence<Pos> = sequence {
                (0 until rows).forEach { row ->
                    (0 until columns).forEach { col ->
                        yield(Pos(row, col))
                    }
                }
            }
        }
    }

    // Provider of values
    interface ValueProvider {
        fun get(pos: Pos): Number
    }

    fun get(row: Int, column: Int) = get(Pos(row, column))
    fun get(pos: Pos) = values.get(pos)

    override fun equals(other: Any?): Boolean {
        if (other == null ||
            other !is Matrix ||
            rows != other.rows ||
            columns != other.columns
        ) return false
        return Pos.all(rows, columns)
            .all { doublesHelper.equals(get(it), other.get(it)) }
    }

    // region Arithmetic

    operator fun plus(rhs: Matrix): Matrix {
        checkDimensions(rhs, "plus", DimensionMatch.RowRow, DimensionMatch.ColumnColumn)
        val newValues = Pos.all(rows, columns).associateWith { get(it) + rhs.get(it) }
        return Matrix(rows, columns, newValues)
    }

    operator fun minus(rhs: Matrix): Matrix {
        checkDimensions(rhs, "minus", DimensionMatch.RowRow, DimensionMatch.ColumnColumn)
        val newValues = Pos.all(rows, columns).associateWith { get(it) - rhs.get(it) }
        return Matrix(rows, columns, newValues)
    }

    operator fun times(rhs: Number): Matrix {
        val newValues = Pos.all(rows, columns).associateWith { get(it) * rhs }
        return Matrix(rows, columns, newValues)
    }

    operator fun div(rhs: Number): Matrix {
        val newValues = Pos.all(rows, columns).associateWith { get(it) / rhs }
        return Matrix(rows, columns, newValues)
    }

    operator fun times(rhs: Matrix): Matrix {
        checkDimensions(rhs, "times", DimensionMatch.ColumnRow)
        val newValues = Pos.all(rows, columns).associateWith { pos ->
            (0 until columns).fold(0 as Number) { total, idx -> total + (get(pos.row, idx) * rhs.get(idx, pos.column)) }
        }
        return Matrix(rows, columns, newValues)
    }

    // endregion

    val transpose: Matrix by lazy {
        Matrix(
            columns,
            rows,
            Pos.all(columns, rows).associateWith { get(it.column, it.row) })
    }

    val determinant: Number by lazy {
        checkSquare("determinant")
        when (rows) {
            1 -> get(0, 0)
            2 -> get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0)
            else -> (0 until columns).fold(0 as Number) { total, col ->
                total + (get(0, col) * (if ((col and 1) == 1) -1 else 1) * subMatrix(col).determinant)
            }
        }
    }

    val inverse: Matrix by lazy {
        checkSquare("inverse")
        when (rows) {
            1 -> Matrix(1, 1, mapOf(Matrix.Pos(0, 0) to 1 / get(0, 0)))
            2 -> Matrix(
                2, 2, mapOf(
                    Pos(0, 0) to get(1, 1),
                    Pos(1, 1) to get(0, 0),
                    Pos(0, 1) to get(0, 1) * -1,
                    Pos(1, 0) to get(1, 0) * -1
                )
            ) / determinant
            else -> throw Exception("NYI")
        }
    }

    override fun toMultilineString(): Sequence<String> {
        // Calculate column formatting
        var colmax = Pos.all(rows, columns)
            .groupBy({ it.column }, { get(it) }).entries.associate {
            it.key to Pair(
                truncate(
                    it.value.map { item -> abs(item) }.max() ?: 0 as Number
                ), (it.value.min() ?: 0.0) < 0.0
            )
        }
        var width = colmax.entries.associate {
            it.key to (if (it.value.first > 0) log10(it.value.first).toInt() + 1 else 1) + (if (it.value.second) 1 else 0) + widthAdjustment
        }
        // Output each line
        return sequence {

            val buffer = StringBuilder()
            (0 until rows).forEach { row ->
                buffer.append('|')
                (0 until columns).forEach { col ->
                    if (col > 0) buffer.append(' ')
                    buffer.append(doublesHelper.format(get(row, col)).padStart(width[col] ?: 2))
                }
                buffer.append('|')
                yield(buffer.toString())
                buffer.clear()
            }
        }
    }

    override fun toString(): String {
        return toMultilineString().joinToString(", ")
    }

    private fun subMatrix(dropColumn: Int): Matrix {
        return Matrix(rows - 1, columns - 1,
            Pos.all(rows, columns)
                .filter { it.row != 0 && it.column != dropColumn }
                .associate {
                    Pos(
                        it.row - 1,
                        if (it.column > dropColumn) it.column - 1 else it.column
                    ) to get(it)
                }
        )
    }

    private enum class DimensionMatch {
        RowRow,
        ColumnColumn,
        RowColumn,
        ColumnRow
    }

    private fun checkSquare(operator: String) {
        if (rows != columns) throw SizeMismatchException(operator, "matrix must be square ($rows != $columns)")
    }

    private fun checkDimensions(rhs: Matrix, operator: String, vararg dimensions: DimensionMatch) {
        val errors = ArrayList<String>()
        if (dimensions.contains(DimensionMatch.RowRow) && rows != rhs.rows) {
            errors.add("rows must match ($rows != ${rhs.rows})")
        }
        if (dimensions.contains(DimensionMatch.ColumnColumn) && columns != rhs.columns) {
            errors.add("columns must match ($columns != ${rhs.columns})")
        }
        if (dimensions.contains(DimensionMatch.RowColumn) && rows != rhs.columns) {
            errors.add("rows must match columns($rows != ${rhs.columns})")
        }
        if (dimensions.contains(DimensionMatch.ColumnRow) && columns != rhs.rows) {
            errors.add("columns must match rows($columns != ${rhs.rows})")
        }
        if (errors.isNotEmpty()) throw SizeMismatchException(operator, errors.joinToString(", "))
    }

    companion object {
        // Create with sparse values
        operator fun invoke(rows: Int, columns: Int, values: Map<Pos, Number>, dps: Int = DoublesHelper.DefaultDps) =
            Matrix(rows, columns, SparseValues(values), dps)

        // Create with full values
        operator fun invoke(
            rows: Int,
            columns: Int,
            values: Array<Array<Number>>,
            dps: Int = DoublesHelper.DefaultDps
        ) =
            Matrix(rows, columns, FullValues(values), dps)

        // Identity matrix
        fun identity(size: Int): Matrix = Matrix(size, size, (0 until size).associate { Pos(it, it) to 1 })
    }

    class Builder(private val rows: Int, private val columns: Int) {
        private val values = HashMap<Pos, Number>()
        private var dps = DoublesHelper.DefaultDps
        private var sparse = true

        constructor(size: Int) : this(size, size)

        fun build(): Matrix =
            if (sparse) Matrix(rows, columns, values, dps)
            else {
                val array = Array(rows) { r -> Array(columns) { c -> values.getOrDefault(Pos(r, c), 0) } }
                Matrix(rows, columns, array, dps)
            }


        fun asIdentity(): Builder {
            (0 until min(rows, columns)).forEach { values[Pos(it, it)] = 1 }
            return this
        }

        fun asSpares(): Builder {
            sparse = true
            return this
        }

        fun asFull(): Builder {
            sparse = false
            return this
        }

        fun withValue(row: Int, column: Int, value: Number): Builder {
            values[Pos(row, column)] = value.toDouble()
            return this
        }

        fun withDps(newDps: Int): Builder {
            dps = newDps
            return this
        }
    }
}


