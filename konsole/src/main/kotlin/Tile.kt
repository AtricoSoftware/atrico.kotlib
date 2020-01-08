package atrico.kotlib.konsole

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
                val line = StringBuilder()
                (0 until width).forEach { itX ->
                    line.append(cells[Pos(itX, itY)]?.char ?: ' ')
                }
                yield(line.toString())
            }
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
                val line = lines.elementAt(y).toString()
                width = max(width, line.length)
                for (x in line.indices) {
                    if (line[x] != transparent) cells[Pos(x, y)] = Cell(line[x])
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

