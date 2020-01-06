package atrico.kotlib.konsole

/**
 * An object that can be rendered (to a [DisplayElement])
 */
interface Renderable {
    /**
     * Render this object
     */
    fun render() = render(Separator.defaultRules)
    /**
     * Render this object with specified [intersectionRules]
     */
    fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement
}

/**
 * [Renderable] object with position [offset]
 */
data class RenderableWithOffset(val renderable: Renderable, val offset: Pos = Pos.ORIGIN)

/**
 * Create [Renderable] with [x],[y] offset
 */
fun Renderable.withOffset(x: Int, y: Int): RenderableWithOffset = RenderableWithOffset(this, Pos(x, y))

/**
 * Create [Renderable] with [offset]
 */
fun Renderable.withOffset(offset: Pos = Pos.ORIGIN): RenderableWithOffset = RenderableWithOffset(this, offset)
