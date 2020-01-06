package atrico.kotlib.konsole

import atrico.kotlib.multilineDisplay.MultilineDisplayable

/**
 * An element that can be displayed
 * Cells start at 0,0
 */
interface DisplayElement : MultilineDisplayable, Renderable {

    /**
     * Width (in characters)
     */
    val width: Int

    /**
     * Height (in characters)
     */
    val height: Int

    /**
     * Get the cell contents at this [position]
     */
    fun getCell(position: Pos): Cell?

    /**
     * Get the cell contents at this [x],[y] position
     */
    fun getCell(x: Int, y: Int) = getCell(Pos(x, y))

    /**
     * Default implementation of [Renderable]
     */
    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement = this
}

/**
 * Perform an action on all populated cells
 */
fun DisplayElement.forEachPopulatedCell(action: (pos: Pos, cell: Cell) -> Unit) {
    (0 until height).forEach { itY ->
        (0 until width).forEach { itX ->
            val pos = Pos(itX, itY)
            val cell = getCell(pos)
            if (cell != null) action(pos, cell)
        }
    }
}

/**
 * Perform an action on all cells (including empty)
 */
fun DisplayElement.forEachCell(action: (pos: Pos, cell: Cell?) -> Unit) {
    (0 until height).forEach { itY ->
        (0 until width).forEach { itX ->
            val pos = Pos(itX, itY)
            val cell = getCell(pos)
            action(pos, cell)
        }
    }
}
