package atrico.kotlib.maths

// Calculate factorial of the number
fun calculateFactorial(value: Int): Int {

    return when {
        value < 0 -> throw Exception("Invalid input")
        value == 0 -> 1
        else -> (2..value).fold(1) { a, v -> a * v }
    }
}