import SeparatorType.*
import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.Renderable
import atrico.kotlib.konsole.Table

enum class SeparatorType {
    NONE,
    SINGLE,
    DOUBLE
}

fun createTable(separators: SeparatorType) = createTable(separators, {})
fun createTable(separators: SeparatorType, extra: Table.Builder.() -> Unit) = Table {
    when (separators) {
        DOUBLE -> withSeparatorsUnicodeDouble()
        SINGLE -> withSeparatorsUnicodeSingle()
        NONE -> {
        }
    }
    extra()
}

fun createBorder(content: Renderable, separators: SeparatorType) = Border(content) {
    when (separators) {
        DOUBLE -> withUnicodeDouble()
        SINGLE -> withUnicodeSingle()
        NONE -> {
        }
    }
}
