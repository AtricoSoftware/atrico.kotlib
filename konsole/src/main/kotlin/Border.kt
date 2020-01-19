package atrico.kotlib.konsole

import atrico.kotlib.konsole.colors.ColoredChar
import atrico.kotlib.konsole.colors.Colors

/**
 * A border around a [Renderable] object
 * Object is [content] and the chars used for the border are specified in [left],[right], [top] and [bottom]
 * Intersections between the borders (corners) are controlled by the [intersectionRules]
 */
class Border(
    private val content: Renderable,
    private val left: ColoredChar?,
    private val right: ColoredChar?,
    private val top: ColoredChar?,
    private val bottom: ColoredChar?
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
        private var left: ColoredChar? = null
        private var right: ColoredChar? = null
        private var top: ColoredChar? = null
        private var bottom: ColoredChar? = null

        fun build(): Border = Border(content, left, right, top, bottom)

        fun withLeft(c: Char, color: Colors? = null): Builder = withLeft(ColoredChar(c, color ?: Colors.none))
        fun withLeft(c: ColoredChar): Builder {
            left = c
            return this
        }

        fun withRight(c: Char, color: Colors? = null): Builder = withRight(ColoredChar(c, color ?: Colors.none))
        fun withRight(c: ColoredChar): Builder {
            right = c
            return this
        }

        fun withTop(c: Char, color: Colors? = null): Builder = withTop(ColoredChar(c, color ?: Colors.none))
        fun withTop(c: ColoredChar): Builder {
            top = c
            return this
        }

        fun withBottom(c: Char, color: Colors? = null): Builder = withBottom(ColoredChar(c, color ?: Colors.none))
        fun withBottom(c: ColoredChar): Builder {
            bottom = c
            return this
        }

        fun withAscii(color: Colors? = null): Builder {
            return withLeft(Separator.asciiVertical, color)
                .withRight(Separator.asciiVertical, color)
                .withTop(Separator.asciiHorizontal, color)
                .withBottom(Separator.asciiHorizontal, color)
        }

        fun withUnicodeSingle(color: Colors? = null): Builder {
            return withLeft(Separator.unicodeVerticalSingle, color)
                .withRight(Separator.unicodeVerticalSingle, color)
                .withTop(Separator.unicodeHorizontalSingle, color)
                .withBottom(Separator.unicodeHorizontalSingle, color)
        }

        fun withUnicodeDouble(color: Colors? = null): Builder {
            return withLeft(Separator.unicodeVerticalDouble, color)
                .withRight(Separator.unicodeVerticalDouble, color)
                .withTop(Separator.unicodeHorizontalDouble, color)
                .withBottom(Separator.unicodeHorizontalDouble, color)
        }
    }
}