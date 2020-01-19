package atrico.kotlib.konsole

import atrico.kotlib.konsole.colors.Colors
import atrico.kotlib.konsole.kolor.Color
import atrico.kotlib.konsole.kolor.Kolor
import java.lang.Integer.max

/**
 * Immutable display element
 * [cells] should contain no positions outside 0 until [width], 0 until [height]
 */
class Tile private constructor(override val width: Int, override val height: Int, private val cells: Map<Pos, Cell>) :
    DisplayElement {

    override fun getCell(position: Pos): Cell? = cells[position]

    override fun toMultilineString(): Sequence<String> =
        sequence {
            (0 until height).forEach { itY ->
                var currentColors = Colors.none
                val line = StringBuilder()
                (0 until width).forEach { itX ->
                    val cell = cells[Pos(itX, itY)] ?: Cell(' ')
                    // Update colors
                    line.append(generateColorChange(currentColors, cell.content.colors))
                    currentColors = cell.content.colors
                    line.append(cell.content.char)
                }
                if (currentColors != Colors.none) line.append(Color.reset)
                yield(line.toString())
            }
        }

    private fun generateColorChange(old: Colors, new: Colors): String {
        var reset = ""
        var fore = ""
        var back = ""
        if (old.foreground != new.foreground) {
            new.foreground?.let { fore = it.foreground } ?: run { reset = Color.reset }
        }
        if (old.background != new.background) {
            new.background?.let { back = it.background } ?: run { reset = Color.reset }
        }
        return "$reset$fore$back"
    }

    companion object {
        // Create from vararg objects
        operator fun invoke(vararg lines: Any) = invoke(lines.asIterable())

        /**
         * Create from list of objects
         * [transparent] characters are ignored
         */
        operator fun invoke(lines: Iterable<Any>, transparent: Char? = null): Tile {
            val cells = mutableMapOf<Pos, Cell>()
            val height = lines.count()
            var width = 0
            for (y in 0 until height) {
                val line = Kolor.parse(lines.elementAt(y).toString())
                val lineCells = coloredStringsToCells(line)
                width = max(width, lineCells.count())
                for (cell in lineCells.withIndex()) {
                    if (cell.value.content.char != transparent) cells[Pos(cell.index, y)] = cell.value
                }
            }
            return Tile(width, height, cells)
        }

        /**
         * Create from map of cells
         * Map will be normalised
         */
        operator fun invoke(cells: Map<Pos, Cell>): Tile {
            if (cells.isEmpty()) return Tile()
            val normalised = normaliseCells(cells)
            val extent = getExtent(normalised.keys)
            return Tile(extent.width, extent.height, normalised)
        }
    }
}
