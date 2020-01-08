import SeparatorType.*
import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.Renderable
import atrico.kotlib.konsole.Table

enum class SeparatorType {
    NONE,
    SINGLE,
    DOUBLE
}

fun createTable(separators: SeparatorType): Table.Builder {
    val table = Table.Builder()
    when (separators) {
        DOUBLE -> table.withSeparatorsUnicodeDouble()
        SINGLE -> table.withSeparatorsUnicodeSingle()
        NONE -> {}// Nothing to do
    }
    return table;
}

fun createBorder(content: Renderable, separators: SeparatorType): Border.Builder {
    val border = Border.Builder(content)
    when (separators) {
        DOUBLE -> border.withUnicodeDouble()
        SINGLE -> border.withUnicodeSingle()
        NONE -> {}// Nothing to do
    }
    return border;
}
