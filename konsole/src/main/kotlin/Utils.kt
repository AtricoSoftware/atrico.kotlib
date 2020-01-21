package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.ColoredString
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * The extent of a range of points
 */
data class Extent(val topLeft: Pos, val width: Int, val height: Int) {
    constructor() : this(Pos.ORIGIN, 0, 0)
    constructor(topLeft: Pos, bottomRight: Pos) : this(
        topLeft,
        bottomRight.x - topLeft.x + 1,
        bottomRight.y - topLeft.y + 1
    )
}

/**
 * Get the extent of a list of [positions]
 */
fun getExtent(positions: Iterable<Pos>): Extent {
    if (positions.count() == 0) return Extent()
    val (topLeft: Pos, bottomRight: Pos) = positions.drop(1).fold(
        Pair(
            positions.first(),
            positions.first()
        )
    ) { acc, pos ->
        Pair(
            Pos(min(acc.first.x, pos.x), min(acc.first.y, pos.y)),
            Pos(max(acc.second.x, pos.x), max(acc.second.y, pos.y))
        )
    }
    return Extent(topLeft, bottomRight)
}

/**
 * Normalise the positions to origin based
 */
fun <T> normaliseCells(cells: Map<Pos, T>): Map<Pos, T> {
    if (cells.count() == 0) return cells
    val topLeft = cells.keys.drop(1).fold(cells.keys.first()) { acc, pos -> Pos.topLeft(acc, pos) }
    if (topLeft == Pos.ORIGIN) return cells
    return cells.map { it.key - topLeft to it.value }.toMap()
}

internal fun coloredStringsToCells(strings: Iterable<ColoredString>): Iterable<Cell> =
    strings.flatMap { it.string.map { ch -> Cell(ch, it.colors) } }
