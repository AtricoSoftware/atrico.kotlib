package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.ColoredChar
import atrico.kotlib.konsole.color.Colors

interface IntersectionRule {
    fun match(
        left: ColoredChar? = null,
        right: ColoredChar? = null,
        above: ColoredChar? = null,
        below: ColoredChar? = null
    ): ColoredChar?;
}

class IntersectionRuleImpl(
    private val intersection: Char,
    private val left: Char? = null,
    private val right: Char? = null,
    private val above: Char? = null,
    private val below: Char? = null
) : IntersectionRule {
    override fun match(
        left: ColoredChar?,
        right: ColoredChar?,
        above: ColoredChar?,
        below: ColoredChar?
    ): ColoredChar? {
        if (left?.char != this.left || right?.char != this.right || above?.char != this.above || below?.char != this.below) return null
        // Choose most common colour
        val colors = HashMap<Colors, Int>()
        left?.colors?.apply { colors[this] = 1 }
        right?.colors?.apply { colors[this] = (colors[this] ?: 0) + 1 }
        above?.colors?.apply { colors[this] = (colors[this] ?: 0) + 1 }
        below?.colors?.apply { colors[this] = (colors[this] ?: 0) + 1 }
        return ColoredChar(intersection, getMostCommonColor(left?.colors, right?.colors, above?.colors, below?.colors))
    }
}

fun getMostCommonColor(vararg colors: Colors?): Colors =
    // Choose most common colour
    colors.fold(HashMap<Colors, Int>()) { acc, item ->
        if (item != null) acc[item] = (acc[item] ?: 0) + 1; acc
    }.entries.maxBy { it.value }?.key ?: Colors.none
