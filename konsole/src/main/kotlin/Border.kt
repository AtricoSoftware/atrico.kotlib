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
    private val bottom: Char?,
    private val intersectionRules: Iterable<IntersectionRule>
) : Renderable {

    override fun render(): DisplayElement {
        val contentRender = content.render()
        var contentOffset = Pos(if (left != null) 1 else 0, if (top != null) 1 else 0)
        var width = contentRender.width + (left?.let { 1 } ?: 0) + (right?.let { 1 } ?: 0)
        var height = contentRender.height + (top?.let { 1 } ?: 0) + (bottom?.let { 1 } ?: 0)
        val children = mutableListOf(content.withOffset(contentOffset))
        if (left != null) children.add(Separator.verticalSeparator(left, height).withOffset())
        if (right != null) children.add(Separator.verticalSeparator(right, height).withOffset(Pos.right(width - 1)))
        if (top != null) children.add(Separator.horizontalSeparator(top, width).withOffset())
        if (bottom != null) children.add(Separator.horizontalSeparator(bottom, width).withOffset(Pos.down(height - 1)))

        return Panel(children, intersectionRules).render()
    }

    /**
     * Builder for creation of a [Border]
     */
    class Builder(private val content: Renderable) {
        private var left: Char? = null
        private var right: Char? = null
        private var top: Char? = null
        private var bottom: Char? = null
        private val intersectionRules = mutableListOf<IntersectionRule>()

        fun build(): Border = Border(content, left, right, top, bottom, intersectionRules)
        fun withLeft(c: Char): Builder {
            left = c
            return this
        }
        fun withLeftAscii() : Builder = withLeft(Separator.asciiVertical).withAsciiIntersectRules()
        fun withLeftUnicodeSingle() : Builder = withLeft(Separator.unicodeVerticalSingle).withUnicodeIntersectRules()
        fun withLeftUnicodeDouble() : Builder = withLeft(Separator.unicodeVerticalDouble).withUnicodeIntersectRules()

        fun withRight(c: Char): Builder {
            right = c
            return this
        }
        fun withRightAscii() : Builder = withRight(Separator.asciiVertical).withAsciiIntersectRules()
        fun withRightUnicodeSingle() : Builder = withRight(Separator.unicodeVerticalSingle).withUnicodeIntersectRules()
        fun withRightUnicodeDouble() : Builder = withRight(Separator.unicodeVerticalDouble).withUnicodeIntersectRules()

        fun withTop(c: Char): Builder {
            top = c
            return this
        }
        fun withTopAscii() : Builder = withTop(Separator.asciiHorizontal).withAsciiIntersectRules()
        fun withTopUnicodeSingle() : Builder = withTop(Separator.unicodeHorizontalSingle).withUnicodeIntersectRules()
        fun withTopUnicodeDouble() : Builder = withTop(Separator.unicodeHorizontalDouble).withUnicodeIntersectRules()

        fun withBottom(c: Char): Builder {
            bottom = c
            return this
        }
        fun withBottomAscii() : Builder = withBottom(Separator.asciiHorizontal).withAsciiIntersectRules()
        fun withBottomUnicodeSingle() : Builder = withBottom(Separator.unicodeHorizontalSingle).withUnicodeIntersectRules()
        fun withBottomUnicodeDouble() : Builder = withBottom(Separator.unicodeHorizontalDouble).withUnicodeIntersectRules()

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

        fun withAscii(): Builder {
            return withLeft(Separator.asciiVertical)
                .withRight(Separator.asciiVertical)
                .withTop(Separator.asciiHorizontal)
                .withBottom(Separator.asciiHorizontal)
                .withAsciiIntersectRules()
        }

        fun withUnicodeSingle(): Builder {
            return withLeft(Separator.unicodeVerticalSingle)
                .withRight(Separator.unicodeVerticalSingle)
                .withTop(Separator.unicodeHorizontalSingle)
                .withBottom(Separator.unicodeHorizontalSingle)
                .withUnicodeIntersectRules()
        }

        fun withUnicodeDouble(): Builder {
            return withLeft(Separator.unicodeVerticalDouble)
                .withRight(Separator.unicodeVerticalDouble)
                .withTop(Separator.unicodeHorizontalDouble)
                .withBottom(Separator.unicodeHorizontalDouble)
                .withUnicodeIntersectRules()
        }
    }
}