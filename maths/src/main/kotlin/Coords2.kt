package atrico.kotlib.maths

import atrico.kotlib.maths.matrix.Matrix
import atrico.kotlib.number.div
import atrico.kotlib.number.minus
import atrico.kotlib.number.plus
import atrico.kotlib.number.times
import kotlin.math.sqrt

typealias HomogeneousMatrix2 = Matrix
typealias TransformMatrix2 = Matrix

class Coords2(val x: Number, val y: Number, private val dps: Int) {
    val doublesHelper = DoublesHelper(dps)

    constructor(x: Number, y: Number) : this(x,y,DoublesHelper.DefaultDps)
    private constructor(matrix: HomogeneousMatrix2, dps: Int) : this(matrix.get(0, 0), matrix.get(1, 0), dps)

    operator fun plus(rhs: Coords2) = Coords2(x + rhs.x, y + rhs.y, dps)
    operator fun minus(rhs: Coords2) = Coords2(x - rhs.x, y - rhs.y, dps)
    operator fun times(rhs: Number) = Coords2(x * rhs, y * rhs, dps)
    operator fun div(rhs: Number) = Coords2(x / rhs, y / rhs, dps)

    fun transform(matrix: TransformMatrix2) = Coords2(matrix * asHomogeneous(), dps)
    val lengthSquared by lazy { x * x + y * y }
    val length by lazy { sqrt(lengthSquared.toDouble()) }

    private fun asHomogeneous(): HomogeneousMatrix2 =
        Matrix(3, 1, mapOf(Matrix.Pos(0, 0) to x.toDouble(), Matrix.Pos(1, 0) to y.toDouble(), Matrix.Pos(2, 0) to 1.0))

    override fun equals(other: Any?): Boolean =
        (other != null && other is Coords2 &&
                doublesHelper.equals(x, other.x) &&
                doublesHelper.equals(y, other.y))

    fun toString(open: String, close: String, sep: String = ",") =
        "$open${doublesHelper.format(x)}$sep${doublesHelper.format(y)}$close"
}