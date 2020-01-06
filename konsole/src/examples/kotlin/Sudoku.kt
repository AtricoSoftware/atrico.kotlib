import atrico.kotlib.konsole.*
import atrico.kotlib.multilineDisplay.displayMultiline
import kotlin.random.Random

/**
 * Example of how a sudoku could be rendered
 */
class SudokuExample(
    private val border: SeparatorType,
    private val major: SeparatorType,
    private val minor: SeparatorType
) : Renderable {
    private val cells: Map<Pos, Int>

    init {
        // Add some random cell values
        cells = (1..30).map {
            val x = Random.Default.nextInt(9)
            val y = Random.Default.nextInt(9)
            val value = Random.Default.nextInt(1, 10)
            Pos(x, y) to value
        }.toMap()
    }

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement {
        val outerTable = createTable(major)
        Pos.allPos(3, 3).forEach { itBox ->
            val innerTable = createTable(minor)
            Pos.allPos(3, 3).forEach { itOffset ->
                val cellValue = cells[itBox * 3 + itOffset]
                innerTable.setCell(itOffset, cellValue ?: ' ')
            }
            outerTable.setCell(itBox, innerTable.build())
        }
        return createBorder(outerTable.build(), border).build().render(intersectionRules)

    }
}

fun main() {
    val sudoku1 = SudokuExample(SeparatorType.DOUBLE, SeparatorType.SINGLE, SeparatorType.NONE)
    println("Example 1")
    sudoku1.render().displayMultiline()
    val sudoku2 = SudokuExample(SeparatorType.DOUBLE, SeparatorType.DOUBLE, SeparatorType.SINGLE)
    println("Example 2")
    sudoku2.render().displayMultiline()
}