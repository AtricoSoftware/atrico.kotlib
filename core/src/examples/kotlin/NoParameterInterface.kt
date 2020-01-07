import atrico.kotlib.multilineDisplay.MultilineDisplayable
import atrico.kotlib.multilineDisplay.displayMultiline

/**
 * Example of using the no parameter version of the interface and the helper function
 */
class AddBorder(private val text: String) : MultilineDisplayable {
    override fun toMultilineString(): Sequence<String> = sequence {
        // Top/Bottom border
        val border = "+${"-".repeat(text.length + 2)}+"
        yield(border)
        yield("| $text |")
        yield(border)
    }
}

fun main() {
    val obj = AddBorder("Hello world")
    obj.displayMultiline()
}