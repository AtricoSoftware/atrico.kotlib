package atrico.kotlib.konsole

/**
 * Panel containing multiple [Renderable] objects
 */
class Panel private constructor(
    private val children: Iterable<RenderableWithOffset>, private val intersectionRules: Iterable<IntersectionRule>
) : Renderable {
    override fun render(): DisplayElement {
        val canvas = Canvas.blank
        for (child in children) {
            child.renderable.render().forEachPopulatedCell { pos, cell -> canvas.setCell(pos + child.offset, cell) }
        }
        // Update intersections
        val allSeparators = canvas.getAllCells().filter { it.value.hasFlag(CellFlags.SEPARATOR) }
        for (cell in allSeparators) {
            val left = allSeparators[cell.key.left()]?.char
            val right = allSeparators[cell.key.right()]?.char
            val above = allSeparators[cell.key.up()]?.char
            val below = allSeparators[cell.key.down()]?.char
            intersectionRules
                .mapNotNull { it.match(left, right, above, below) }
                .firstOrNull()
                ?.apply { canvas.setCell(cell.key, Cell(this, CellFlags.SEPARATOR)) }
        }
        return canvas.render()
    }

    companion object {
        operator fun invoke(firstChild: Renderable, vararg children: Renderable) =
            invoke(listOf(firstChild) + children.asIterable())

        @JvmName("invokeRenderable") // Avoid signature clash
        operator fun invoke(children: Iterable<Renderable>) =
            invoke(children.map { RenderableWithOffset(it) })

        operator fun invoke(vararg children: RenderableWithOffset) = invoke(children.asIterable())
        operator fun invoke(children: Iterable<RenderableWithOffset>) = invoke(children, emptyList())
        operator fun invoke(children: Iterable<RenderableWithOffset>, intersectionRules: Iterable<IntersectionRule>) =
            Panel(children, intersectionRules)
    }
}