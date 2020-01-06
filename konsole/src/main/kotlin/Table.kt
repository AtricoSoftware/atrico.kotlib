package atrico.kotlib.konsole

import java.lang.Integer.max

/**
 * Renderable table
 */
class Table private constructor(
    private val cells: Map<Pos, Renderable>,
    private val horizontalSeparator: Char?,
    private val verticalSeparator: Char?
) : Renderable {
    val rows: Int
    val columns: Int

    init {
        getExtent(cells.keys).apply {
            rows = height
            columns = width
        }
    }

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement {
        // Render all cells
        val renderedCells = cells.map { it.key to it.value.render(intersectionRules) }.toMap()
        // Calculate column widths and row heights
        val columnWidths = Array(columns) { 0 }
        val rowHeights = Array(rows) { 0 }
        for (cell in renderedCells) {
            columnWidths[cell.key.x] = max(columnWidths[cell.key.x], cell.value.width)
            rowHeights[cell.key.y] = max(rowHeights[cell.key.y], cell.value.height)
        }
        // Calculate column/row offsets
        val columnOffsets = Array(columns) { 0 }
        (1 until columns).forEach {
            columnOffsets[it] = columnOffsets[it - 1] + columnWidths[it - 1] + (verticalSeparator?.let { 1 } ?: 0)
        }
        val rowOffsets = Array(rows) { 0 }
        (1 until rows).forEach {
            rowOffsets[it] = rowOffsets[it - 1] + rowHeights[it - 1] + (horizontalSeparator?.let { 1 } ?: 0)
        }
        // Create children with offsets
        val children = ArrayList(renderedCells.map {
            it.value.withOffset(columnOffsets[it.key.x], rowOffsets[it.key.y])
        })
        // Add separators
        verticalSeparator?.apply {
            val height = rowHeights.sum() + (horizontalSeparator?.let { columns - 1 } ?: 0)
            children.addAll((1 until columns).map {
                Separator.verticalSeparator(this, height).withOffset(columnOffsets[it] - 1, 0)
            })
        }
        horizontalSeparator?.apply {
            val width = columnWidths.sum() + (verticalSeparator?.let { columns - 1 } ?: 0)
            children.addAll((1 until rows).map {
                Separator.horizontalSeparator(this, width).withOffset(0, rowOffsets[it] - 1)
            })
        }
        return Panel(children).render(intersectionRules)
    }


    class Builder {
        private val cells = mutableMapOf<Pos, Renderable>()
        private var horizontalSeparator: Char? = null
        private var verticalSeparator: Char? = null

        private var topLeft: Pos = Pos.ORIGIN
        private var bottomRight: Pos = Pos.ORIGIN

        fun setCell(x: Int, y: Int, obj: Any): Builder = setCell(Pos(x, y), obj)
        fun setCell(pos: Pos, obj: Any): Builder {
            cells[pos] = when (obj) {
                is Renderable -> obj
                else -> Tile(obj)
            }
            topLeft = topLeft.topLeft(pos)
            bottomRight = bottomRight.bottomRight(pos)
            return this
        }

        fun appendRow(vararg objs: Any?): Builder {
            var pos = Pos(topLeft.x - 1, bottomRight.y + 1)
            for (obj in objs) {
                pos = pos.right()
                if (obj != null) {
                    cells[pos] = when (obj) {
                        is Renderable -> obj
                        else -> Tile(obj)
                    }
                }
            }
            bottomRight = bottomRight.bottomRight(pos)
            return this
        }

        fun withHorizontalSeparator(ch: Char?): Builder {
            horizontalSeparator = ch
            return this
        }

        fun withVerticalSeparator(ch: Char?): Builder {
            verticalSeparator = ch
            return this
        }

        fun withSeparatorsAscii() =
            withVerticalSeparator(Separator.asciiVertical)
                .withHorizontalSeparator(Separator.asciiHorizontal)


        fun withSeparatorsUnicodeSingle() =
            withVerticalSeparator(Separator.unicodeVerticalSingle)
                .withHorizontalSeparator(Separator.unicodeHorizontalSingle)

        fun withSeparatorsUnicodeDouble() =
            withVerticalSeparator(Separator.unicodeVerticalDouble)
                .withHorizontalSeparator(Separator.unicodeHorizontalDouble)


        fun build(): Table =
            Table(normaliseCells(cells), horizontalSeparator, verticalSeparator)
    }
}