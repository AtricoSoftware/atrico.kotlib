package atrico.kotlib.maths

import atrico.kotlib.number.abs
import atrico.kotlib.number.compareTo
import atrico.kotlib.number.minus
import java.text.DecimalFormat
import kotlin.math.pow

class DoublesHelper(dps: Int = DefaultDps) {
    private val decimalFormat = DecimalFormat("0${if (dps > 0) "." else ""}${"0".repeat(dps)}")
    private val equalsThreshold = (10.0).pow(-dps)

    fun equals(lhs: Number, rhs: Number) = abs((lhs - rhs)) < equalsThreshold
    fun format(value: Number) = decimalFormat.format(value)

    companion object {
        var DefaultDps = 3
    }
}