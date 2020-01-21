package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.ColoredChar
import atrico.kotlib.konsole.color.Colors
import java.lang.Integer.max

/**
 * Renderable table
 */
class Table private constructor(
    private val cells: Map<Pos, Renderable>,
    private val horizontalSeparator: ColoredChar?,
    private val verticalSeparator: ColoredChar?
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

    interface Builder {
        fun setCell(x: Int, y: Int, obj: Any)
        fun setCell(pos: Pos, obj: Any)
        fun appendRow(vararg objs: Any?)
        fun withHorizontalSeparator(c: Char, color: Colors? = null)
        fun withHorizontalSeparator(c: ColoredChar)
        fun withVerticalSeparator(c: Char, color: Colors? = null)
        fun withVerticalSeparator(c: ColoredChar)
        fun withSeparatorsAscii(color: Colors = Colors.none)
        fun withSeparatorsUnicodeSingle(color: Colors = Colors.none)
        fun withSeparatorsUnicodeDouble(color: Colors = Colors.none)
    }

    private class BuilderImpl : Builder {
        private val cells = mutableMapOf<Pos, Renderable>()
        private var horizontalSeparator: ColoredChar? = null
        private var verticalSeparator: ColoredChar? = null

        private var topLeft: Pos = Pos.ORIGIN
        private var bottomRight: Pos = Pos.ORIGIN

        fun build(): Table = Table(normaliseCells(cells), horizontalSeparator, verticalSeparator)

        override fun setCell(x: Int, y: Int, obj: Any) = setCell(Pos(x, y), obj)
        override fun setCell(pos: Pos, obj: Any) {
            cells[pos] = when (obj) {
                is Renderable -> obj
                else -> Tile(obj)
            }
            topLeft = topLeft.topLeft(pos)
            bottomRight = bottomRight.bottomRight(pos)
        }

        override fun appendRow(vararg objs: Any?) {
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
        }

        override fun withHorizontalSeparator(c: Char, color: Colors?) =
            withHorizontalSeparator(ColoredChar(c, color ?: Colors.none))

        override fun withHorizontalSeparator(c: ColoredChar) {
            horizontalSeparator = c
        }

        override fun withVerticalSeparator(c: Char, color: Colors?) =
            withVerticalSeparator(ColoredChar(c, color ?: Colors.none))

        override fun withVerticalSeparator(c: ColoredChar) {
            verticalSeparator = c
        }

        override fun withSeparatorsAscii(color: Colors) {
            withVerticalSeparator(ColoredChar(Separator.asciiVertical, color))
            withHorizontalSeparator(ColoredChar(Separator.asciiHorizontal, color))

        }


        override fun withSeparatorsUnicodeSingle(color: Colors) {

            withVerticalSeparator(ColoredChar(Separator.unicodeVerticalSingle, color))
            withHorizontalSeparator(ColoredChar(Separator.unicodeHorizontalSingle, color))
        }

        override fun withSeparatorsUnicodeDouble(color: Colors) {
            withVerticalSeparator(ColoredChar(Separator.unicodeVerticalDouble, color))
            withHorizontalSeparator(ColoredChar(Separator.unicodeHorizontalDouble, color))
        }

    }

    companion object {
        operator fun invoke() = invoke({})
        operator fun invoke(config: Builder.() -> Unit): Table {
            val builder = BuilderImpl()
            builder.config()
            return builder.build()
        }
    }
}