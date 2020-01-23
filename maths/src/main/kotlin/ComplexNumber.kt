package atrico.kotlib.maths

import atrico.kotlib.number.*

data class ComplexNumber internal constructor(internal val coords: Coords2) {
    val real = coords.x
    val imag = coords.y

    constructor(real: Number, imag: Number, dps: Int = DoublesHelper.DefaultDps) : this(Coords2(real, imag, dps))

    operator fun plus(rhs: ComplexNumber) = ComplexNumber(coords + rhs.coords)
    operator fun minus(rhs: ComplexNumber) = ComplexNumber(coords - rhs.coords)
    operator fun times(rhs: ComplexNumber) =
        ComplexNumber(real * rhs.real - imag * rhs.imag, real * rhs.imag + imag * rhs.real)

    operator fun times(rhs: Number) = ComplexNumber(coords * rhs.toDouble())
    operator fun div(rhs: ComplexNumber) = times(rhs.reciprocal())
    operator fun div(rhs: Number) = ComplexNumber(coords / rhs.toDouble())

    fun modulus() = coords.length
    fun modulusSquared() = coords.lengthSquared

    fun conjugate() = ComplexNumber(real, -imag)
    fun reciprocal() = ComplexNumber(real / modulusSquared(), -imag / modulusSquared())

    override fun toString() = toString
    private val toString by lazy {
        StringBuilder()
            .append('(')
            .append(coords.doublesHelper.format(real))
            .append(if (imag < 0) " - " else " + ")
            .append(coords.doublesHelper.format(abs(imag)))
            .append("j)")
            .toString()
    }
}
