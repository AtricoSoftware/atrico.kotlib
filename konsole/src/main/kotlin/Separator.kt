package atrico.kotlib.konsole

object Separator {
    const val asciiHorizontal = '-'
    const val asciiVertical = '|'
    val asciiRules: IntersectionRule by lazy { AsciiIntersectionRules() }
    const val unicodeHorizontalSingle = '─'
    const val unicodeHorizontalDouble = '═'
    const val unicodeVerticalSingle = '│'
    const val unicodeVerticalDouble = '║'
    val unicodeRules: IntersectionRule by lazy { UnicodeIntersectionRules() }
    val defaultRules: Iterable<IntersectionRule> by lazy{listOf(asciiRules, unicodeRules)}

    fun horizontalSeparator(char: Char, length: Int): Renderable = HorizontalSeparator(char, length)
    fun verticalSeparator(char: Char, length: Int): Renderable = VerticalSeparator(char, length)

    private class HorizontalSeparator(private val char: Char, private val length: Int) : Renderable {
        override fun render(intersectionRules: Iterable<IntersectionRule>) = Tile((0 until length).map { Pos(it, 0) to Cell(char, CellFlags.SEPARATOR) }.toMap())
    }

    private class VerticalSeparator(private val char: Char, private val length: Int) : Renderable {
        override fun render(intersectionRules: Iterable<IntersectionRule>) = Tile((0 until length).map { Pos(0, it) to Cell(char, CellFlags.SEPARATOR) }.toMap())
    }

    private class AsciiIntersectionRules : IntersectionRule {
        private object Parts {
            const val LEFT = 0x01
            const val RIGHT = 0x02
            const val ABOVE = 0x04
            const val BELOW = 0x08
        }

        private val corner = '+'
        private val matchers = mapOf(
            // Corners
            (Parts.LEFT + Parts.ABOVE) to corner,
            (Parts.RIGHT + Parts.ABOVE) to corner,
            (Parts.LEFT + Parts.BELOW) to corner,
            (Parts.RIGHT + Parts.BELOW) to corner,
            // T
            (Parts.LEFT + Parts.RIGHT + Parts.ABOVE) to corner,
            (Parts.LEFT + Parts.RIGHT + Parts.BELOW) to corner,
            (Parts.LEFT + Parts.ABOVE + Parts.BELOW) to corner,
            (Parts.RIGHT + Parts.ABOVE + Parts.BELOW) to corner,
            // X
            (Parts.LEFT + Parts.RIGHT + Parts.ABOVE + Parts.BELOW) to corner
        )

        override fun match(left: Char?, right: Char?, above: Char?, below: Char?): Char? {
            var parts = 0
            if (left == asciiHorizontal) parts += Parts.LEFT
            if (right == asciiHorizontal) parts += Parts.RIGHT
            if (above == asciiVertical) parts += Parts.ABOVE
            if (below == asciiVertical) parts += Parts.BELOW
            return matchers[parts]
        }
    }

    private class UnicodeIntersectionRules : IntersectionRule {
        private object Parts {
            const val LEFT_SINGLE = 0x01
            const val RIGHT_SINGLE = 0x02
            const val ABOVE_SINGLE = 0x04
            const val BELOW_SINGLE = 0x08
            const val LEFT_DOUBLE = 0x10
            const val RIGHT_DOUBLE = 0x20
            const val ABOVE_DOUBLE = 0x40
            const val BELOW_DOUBLE = 0x80
        }

        private val matchers = mapOf(
            // SINGLE
            // Corners
            (Parts.LEFT_SINGLE + Parts.ABOVE_SINGLE) to '┘',
            (Parts.RIGHT_SINGLE + Parts.ABOVE_SINGLE) to '└',
            (Parts.LEFT_SINGLE + Parts.BELOW_SINGLE) to '┐',
            (Parts.RIGHT_SINGLE + Parts.BELOW_SINGLE) to '┌',
            // T
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.ABOVE_SINGLE) to '┴',
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.BELOW_SINGLE) to '┬',
            (Parts.LEFT_SINGLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '┤',
            (Parts.RIGHT_SINGLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '├',
            // X
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '┼',
            // DOUBLE
            // Corners
            (Parts.LEFT_DOUBLE + Parts.ABOVE_DOUBLE) to '╝',
            (Parts.RIGHT_DOUBLE + Parts.ABOVE_DOUBLE) to '╚',
            (Parts.LEFT_DOUBLE + Parts.BELOW_DOUBLE) to '╗',
            (Parts.RIGHT_DOUBLE + Parts.BELOW_DOUBLE) to '╔',
            // T
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.ABOVE_DOUBLE) to '╩',
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.BELOW_DOUBLE) to '╦',
            (Parts.LEFT_DOUBLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╣',
            (Parts.RIGHT_DOUBLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╠',
            // X
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╬',
            // MIXED
            // Corners
            (Parts.LEFT_SINGLE + Parts.ABOVE_DOUBLE) to '╜',
            (Parts.RIGHT_SINGLE + Parts.ABOVE_DOUBLE) to '╙',
            (Parts.LEFT_SINGLE + Parts.BELOW_DOUBLE) to '╖',
            (Parts.RIGHT_SINGLE + Parts.BELOW_DOUBLE) to '╓',
            (Parts.LEFT_DOUBLE + Parts.ABOVE_SINGLE) to '╛',
            (Parts.RIGHT_DOUBLE + Parts.ABOVE_SINGLE) to '╘',
            (Parts.LEFT_DOUBLE + Parts.BELOW_SINGLE) to '╕',
            (Parts.RIGHT_DOUBLE + Parts.BELOW_SINGLE) to '╒',
            // T (Double external)
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.ABOVE_SINGLE) to '╧',
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.BELOW_SINGLE) to '╤',
            (Parts.LEFT_SINGLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╢',
            (Parts.RIGHT_SINGLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╟',
            // T (Single external)
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.ABOVE_DOUBLE) to '╨',
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.BELOW_DOUBLE) to '╥',
            (Parts.LEFT_DOUBLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '╡',
            (Parts.RIGHT_DOUBLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '╞',
            // X
            (Parts.LEFT_SINGLE + Parts.RIGHT_SINGLE + Parts.ABOVE_DOUBLE + Parts.BELOW_DOUBLE) to '╫',
            (Parts.LEFT_DOUBLE + Parts.RIGHT_DOUBLE + Parts.ABOVE_SINGLE + Parts.BELOW_SINGLE) to '╪'
        )

        override fun match(left: Char?, right: Char?, above: Char?, below: Char?): Char? {
            var parts = 0
            if (left == unicodeHorizontalSingle) parts += Parts.LEFT_SINGLE
            else if (left == unicodeHorizontalDouble) parts += Parts.LEFT_DOUBLE
            if (right == unicodeHorizontalSingle) parts += Parts.RIGHT_SINGLE
            else if (right == unicodeHorizontalDouble) parts += Parts.RIGHT_DOUBLE
            if (above == unicodeVerticalSingle) parts += Parts.ABOVE_SINGLE
            else if (above == unicodeVerticalDouble) parts += Parts.ABOVE_DOUBLE
            if (below == unicodeVerticalSingle) parts += Parts.BELOW_SINGLE
            else if (below == unicodeVerticalDouble) parts += Parts.BELOW_DOUBLE
            return matchers[parts]
        }
    }
}