package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.ColoredChar
import atrico.kotlib.konsole.color.Colors

/**
 * A border around a [Renderable] object
 * Object is [content] and the chars used for the border are specified in [left],[right], [top] and [bottom]
 * Intersections between the borders (corners) are controlled by the intersectionRules in the render function
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
        val contentOffset = Pos(if (left != null) 1 else 0, if (top != null) 1 else 0)
        val width = contentRender.width + (left?.let { 1 } ?: 0) + (right?.let { 1 } ?: 0)
        val height = contentRender.height + (top?.let { 1 } ?: 0) + (bottom?.let { 1 } ?: 0)
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
    interface Builder {
        fun withLeft(c: Char, color: Colors? = null)
        fun withLeft(c: ColoredChar)
        fun withRight(c: Char, color: Colors? = null)
        fun withRight(c: ColoredChar)
        fun withTop(c: Char, color: Colors? = null)
        fun withTop(c: ColoredChar)
        fun withBottom(c: Char, color: Colors? = null)
        fun withBottom(c: ColoredChar)
        fun withAscii(color: Colors? = null)
        fun withUnicodeSingle(color: Colors? = null)
        fun withUnicodeDouble(color: Colors? = null)
    }

    private class BuilderImpl(private val content: Renderable) : Builder {
        private var left: ColoredChar? = null
        private var right: ColoredChar? = null
        private var top: ColoredChar? = null
        private var bottom: ColoredChar? = null

        fun build(): Border = Border(content, left, right, top, bottom)

        override fun withLeft(c: Char, color: Colors?) = withLeft(ColoredChar(c, color ?: Colors.none))
        override fun withLeft(c: ColoredChar) {
            left = c
        }

        override fun withRight(c: Char, color: Colors?) = withRight(ColoredChar(c, color ?: Colors.none))
        override fun withRight(c: ColoredChar) {
            right = c
        }

        override fun withTop(c: Char, color: Colors?) = withTop(ColoredChar(c, color ?: Colors.none))
        override fun withTop(c: ColoredChar) {
            top = c
        }

        override fun withBottom(c: Char, color: Colors?) = withBottom(ColoredChar(c, color ?: Colors.none))
        override fun withBottom(c: ColoredChar) {
            bottom = c
        }

        override fun withAscii(color: Colors?) {
            withLeft(Separator.asciiVertical, color)
            withRight(Separator.asciiVertical, color)
            withTop(Separator.asciiHorizontal, color)
            withBottom(Separator.asciiHorizontal, color)
        }

        override fun withUnicodeSingle(color: Colors?) {
            withLeft(Separator.unicodeVerticalSingle, color)
            withRight(Separator.unicodeVerticalSingle, color)
            withTop(Separator.unicodeHorizontalSingle, color)
            withBottom(Separator.unicodeHorizontalSingle, color)
        }

        override fun withUnicodeDouble(color: Colors?) {
            withLeft(Separator.unicodeVerticalDouble, color)
            withRight(Separator.unicodeVerticalDouble, color)
            withTop(Separator.unicodeHorizontalDouble, color)
            withBottom(Separator.unicodeHorizontalDouble, color)
        }
    }

    companion object {
        operator fun invoke(content: Renderable) = invoke(content, {})
        operator fun invoke(content: Renderable, config: Builder.() -> Unit): Border {
            val builder = BuilderImpl(content)
            builder.config()
            return builder.build()
        }
    }
}