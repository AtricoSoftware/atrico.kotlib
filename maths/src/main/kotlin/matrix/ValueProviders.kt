package atrico.kotlib.maths.matrix

class SparseValues(private val values: Map<Matrix.Pos, Number>) : Matrix.ValueProvider {
    override fun get(pos: Matrix.Pos) = values.getOrDefault(pos, 0)
}

class FullValues(private val values: Array<Array<Number>>) : Matrix.ValueProvider {
    override fun get(pos: Matrix.Pos) = values[pos.row][pos.column]
}