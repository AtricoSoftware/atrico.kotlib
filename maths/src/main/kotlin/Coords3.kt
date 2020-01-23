package atrico.kotlib.maths

import atrico.kotlib.maths.matrix.Matrix
import atrico.kotlib.number.div
import atrico.kotlib.number.minus
import atrico.kotlib.number.plus
import atrico.kotlib.number.times
import kotlin.math.sqrt

typealias HomogeneousMatrix3 = Matrix
typealias TransformMatrix3 = Matrix

class Coords3(val x: Number, val y: Number, val z: Number, private val dps: Int) {
    val doublesHelper = DoublesHelper(dps)

    private constructor(matrix: HomogeneousMatrix3, dps: Int) : this(
        matrix.get(0, 0),
        matrix.get(1, 0),
        matrix.get(2, 0),
        dps
    )

    operator fun plus(rhs: Coords3) = Coords3(x + rhs.x, y + rhs.y, z + rhs.z, dps)
    operator fun minus(rhs: Coords3) = Coords3(x - rhs.x, y - rhs.y, z - rhs.z, dps)
    operator fun times(rhs: Number) = Coords3(x * rhs, y * rhs, z * rhs, dps)
    operator fun div(rhs: Number) = Coords3(x / rhs, y / rhs, z / rhs, dps)

    fun transform(matrix: TransformMatrix3) = Coords3(matrix * asHomogeneous(), dps)
    val lengthSquared by lazy { x * x + y * y + z * z }
    val length by lazy { sqrt(lengthSquared.toDouble()) }

    private fun asHomogeneous(): HomogeneousMatrix3 =
        Matrix(
            4,
            1,
            mapOf(
                Matrix.Pos(0, 0) to x.toDouble(),
                Matrix.Pos(1, 0) to y.toDouble(),
                Matrix.Pos(2, 0) to z.toDouble(),
                Matrix.Pos(3, 0) to 1.0
            )
        )

    override fun equals(other: Any?): Boolean =
        (other != null && other is Coords3 &&
                doublesHelper.equals(x, other.x) &&
                doublesHelper.equals(y, other.y) &&
                doublesHelper.equals(z, other.z))

    fun toString(open: String, close: String, sep: String = ",") =
        "$open${doublesHelper.format(x)}$sep${doublesHelper.format(y)}$sep${doublesHelper.format(z)}$close"
}