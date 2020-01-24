package atrico.kotlib.maths

// Calculate factors of the number
fun calculateFactors(value: Int): List<Int> {
    val factors = mutableListOf(1)
    for (f in 2..value) {
        val calc = value / f * f;
        if (calc == value) factors.add(f)
    }
    return factors
}