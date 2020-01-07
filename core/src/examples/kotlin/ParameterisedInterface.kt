import atrico.kotlib.multilineDisplay.MultilineDisplayable
import atrico.kotlib.multilineDisplay.MultilineDisplayableParameterised
import atrico.kotlib.multilineDisplay.displayMultiline

/**
 * Example of using the parameterised version of the interface and the helper function
 * The class supports both interfaces and has a default parameter
 */
class Parameter(val horizontal: Char, val vertical: Char, val corner: Char)

class AddBorderWithParameter(private val text: String) : MultilineDisplayableParameterised<Parameter>,
    MultilineDisplayable {
    override fun toMultilineString(parameter: Parameter): Sequence<String> = sequence {
        // Top/Bottom border
        val border = "${parameter.corner}${parameter.horizontal.toString().repeat(text.length + 2)}${parameter.corner}"
        yield(border)
        yield("${parameter.vertical} $text ${parameter.vertical}")
        yield(border)
    }

    override fun toMultilineString(): Sequence<String> = toMultilineString(Parameter('-', '|', '+'))
}

fun main() {
    val obj = AddBorderWithParameter("Hello world")
    println("Parameters")
    obj.displayMultiline(Parameter('=', 'I', '#'))
    println("Default")
    obj.displayMultiline()
}