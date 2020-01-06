package atrico.kotlib.konsole

/**
 * A border around a [Renderable] object
 * Object is [content] and the chars used for the border are specified in [left],[right], [top] and [bottom]
 * Intersections between the borders (corners) are controlled by the [intersectionRules]
 */
class Border(
    private val content: Renderable,
    private val left: Char?,
    private val right: Char?,
    private val top: Char?,
    private val bottom: Char?
) : Renderable {

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement {
        val contentRender = content.render(intersectionRules)
        var contentOffset = Pos(if (left != null) 1 else 0, if (top != null) 1 else 0)
        var width = contentRender.width + (left?.let { 1 } ?: 0) + (right?.let { 1 } ?: 0)
        var height = contentRender.height + (top?.let { 1 } ?: 0) + (bottom?.let { 1 } ?: 0)
        val children = mutableListOf(content.withOffset(contentOffset))
        if (left != null) children.add(Separator.verticalSeparator(left, height).withOffset())
        if (right != null) children.add(Separator.verticalSeparator(right, height).withOffset(Pos.right(width - 1)))
        if (top != null) children.add(Separator.horizontalSeparator(top, width).withOffset())
        if (bottom != null) children.add(Separator.horizontalSeparator(bottom, width).withOffset(Pos.down(height - 1)))

        return Panel(children).render(intersectionRules)
    }

    /**
     * Builder for creation of a [Border]
     */
    class Builder(private val content: Renderable) {
        private var left: Char? = null
        private var right: Char? = null
        private var top: Char? = null
        private var bottom: Char? = null

        fun build(): Border = Border(content, left, right, top, bottom)
        fun withLeft(c: Char): Builder {
            left = c
            return this
        }

        fun withRight(c: Char): Builder {
            right = c
            return this
        }

        fun withTop(c: Char): Builder {
            top = c
            return this
        }

        fun withBottom(c: Char): Builder {
            bottom = c
            return this
        }

        fun withAscii(): Builder {
            return withLeft(Separator.asciiVertical)
                .withRight(Separator.asciiVertical)
                .withTop(Separator.asciiHorizontal)
                .withBottom(Separator.asciiHorizontal)
        }

        fun withUnicodeSingle(): Builder {
            return withLeft(Separator.unicodeVerticalSingle)
                .withRight(Separator.unicodeVerticalSingle)
                .withTop(Separator.unicodeHorizontalSingle)
                .withBottom(Separator.unicodeHorizontalSingle)
        }

        fun withUnicodeDouble(): Builder {
            return withLeft(Separator.unicodeVerticalDouble)
                .withRight(Separator.unicodeVerticalDouble)
                .withTop(Separator.unicodeHorizontalDouble)
                .withBottom(Separator.unicodeHorizontalDouble)
        }
    }
}