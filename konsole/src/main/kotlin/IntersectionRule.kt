package atrico.kotlib.konsole

interface IntersectionRule {
    fun match(
        left: Char? = null,
        right: Char? = null,
        above: Char? = null,
        below: Char? = null
    ): Char?;
}

class IntersectionRuleImpl(
    private val intersection: Char,
    private val left: Char? = null,
    private val right: Char? = null,
    private val above: Char? = null,
    private val below: Char? = null
) : IntersectionRule {
    override fun match(left: Char?, right: Char?, above: Char?, below: Char?): Char? =
        if (left == this.left && right == this.right && above == this.above && below == this.below) intersection else null
}