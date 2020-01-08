package atrico.kotlib.konsole

import java.lang.Integer.max

/**
 * Renderable table
 */
class Table private constructor(
    private val cells: Map<Pos, Renderable>,
    private val horizontalSeparator: Char?,
    private val verticalSeparator: Char?,
    private val intersectionRules: Iterable<IntersectionRule>
) : Renderable {
    val rows: Int
    val columns: Int

    init {
        getExtent(cells.keys).apply {
            rows = height
            columns = width
        }
    }

    override fun render(): DisplayElement {
        // Render all cells
        val renderedCells = cells.map { it.key to it.value.render() }.toMap()
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
        return Panel(children, intersectionRules).render()
    }


    class Builder {
        private val cells = mutableMapOf<Pos, Renderable>()
        private var horizontalSeparator: Char? = null
        private var verticalSeparator: Char? = null
        private val intersectionRules = mutableListOf<IntersectionRule>()

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

        fun withHorizontalSeparatorAscii(): Builder =
            withHorizontalSeparator(Separator.asciiHorizontal).withAsciiIntersectRules()

        fun withHorizontalSeparatorUnicodeSingle(): Builder =
            withHorizontalSeparator(Separator.unicodeHorizontalSingle).withUnicodeIntersectRules()

        fun withHorizontalSeparatorUnicodeDouble(): Builder =
            withHorizontalSeparator(Separator.unicodeHorizontalDouble).withUnicodeIntersectRules()

        fun withVerticalSeparator(ch: Char?): Builder {
            verticalSeparator = ch
            return this
        }

        fun withVerticalSeparatorAscii(): Builder =
            withVerticalSeparator(Separator.asciiVertical).withAsciiIntersectRules()

        fun withVerticalSeparatorUnicodeSingle(): Builder =
            withVerticalSeparator(Separator.unicodeVerticalSingle).withUnicodeIntersectRules()

        fun withVerticalSeparatorUnicodeDouble(): Builder =
            withVerticalSeparator(Separator.unicodeVerticalDouble).withUnicodeIntersectRules()

        fun withIntersectRule(
            intersection: Char,
            left: Char? = null,
            right: Char? = null,
            above: Char? = null,
            below: Char? = null
        ): Builder {
            if (listOf(left, right, above, below).count { it != null } < 1) throw Exception("Not enough sides")
            intersectionRules.add(IntersectionRuleImpl(intersection, left, right, above, below))
            return this
        }

        fun withAsciiIntersectRules(): Builder {
            intersectionRules.add(Separator.asciiRules)
            return this
        }

        fun withUnicodeIntersectRules(): Builder {
            intersectionRules.add(Separator.unicodeRules)
            return this
        }

        fun withSeparatorsAscii(): Builder {
            return withVerticalSeparatorAscii()
                .withHorizontalSeparatorAscii()
                .withAsciiIntersectRules()
        }

        fun withSeparatorsUnicodeSingle(): Builder {
            return withVerticalSeparatorUnicodeSingle()
                .withHorizontalSeparatorUnicodeSingle()
                .withUnicodeIntersectRules()
        }

        fun withSeparatorsUnicodeDouble(): Builder {
            return withVerticalSeparatorUnicodeDouble()
                .withHorizontalSeparatorUnicodeDouble()
                .withUnicodeIntersectRules()
        }

        fun build(): Table =
            Table(normaliseCells(cells), horizontalSeparator, verticalSeparator, intersectionRules.distinct())
    }
}