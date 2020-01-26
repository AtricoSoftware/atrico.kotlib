import atrico.kotlib.konsole.IntersectionRule
import atrico.kotlib.konsole.Pos
import atrico.kotlib.konsole.Renderable
import atrico.kotlib.multilineDisplay.displayMultiline
import kotlin.random.Random

/**
 * Example of how tic tac toe could be rendered
 */
class TicTacToeExample(
    private val lines: SeparatorType
) : Renderable {
    private val cells: Map<Pos, Char?>

    init {
        // Add some random cell values
        val players = arrayOf('X', 'O')
        var player = 0
        cells = Pos.allPos(3, 3).map {
            it to if (Random.Default.nextDouble() < 0.5) {
                players[(player++ % 2)]
            } else {
                null
            }
        }.toMap()
    }

    override fun render(intersectionRules: Iterable<IntersectionRule>) =
        createTable(lines) {
            Pos.allPos(3, 3).forEach {
                setCell(it, cells[it] ?: ' ')
            }
        }.render(intersectionRules)
}


fun main() {
    val ticTacToe1 = TicTacToeExample(SeparatorType.SINGLE)
    println("Example 1")
    ticTacToe1.render().displayMultiline()
    val ticTacToe2 = TicTacToeExample(SeparatorType.DOUBLE)
    println("Example 2")
    ticTacToe2.render().displayMultiline()
}