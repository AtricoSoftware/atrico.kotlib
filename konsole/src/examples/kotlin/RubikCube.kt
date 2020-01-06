import atrico.kotlib.konsole.*
import atrico.kotlib.multilineDisplay.displayMultiline

/**
 * Example of how a Rubik's cube could be rendered
 * NOTE: There is no attempt here to model the positions of the cube
 */
class RubikCubeExample(
    private val large: Boolean
) : Renderable {

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement {
        // TODO add colour
        val surface = createTable(SeparatorType.NONE)
            .setCell(1, 0, createFace('W', left = true, right = true, top = true))
            .setCell(0, 1, createFace('G', left = true, top = true, bottom = true))
            .setCell(1, 1, createFace('R', left = true, right = true, top = true, bottom = true))
            .setCell(2, 1, createFace('B', right = true, top = true, bottom = true))
            .setCell(3, 1, createFace('O', right = true, top = true, bottom = true))
            .setCell(1, 2, createFace('Y', left = true, right = true, bottom = true))
        return surface.build().render(intersectionRules)
    }

    private fun createFace(
        color: Char,
        left: Boolean = false,
        right: Boolean = false,
        top: Boolean = false,
        bottom: Boolean = false
    ): Renderable {
        val face = Table.Builder()
        Pos.allPos(3, 3).forEach {
            face.setCell(it, color)
        }
        if (large) face.withSeparatorsUnicodeSingle()
        val border = Border.Builder(face.build())
        if (left) border.withLeft(if (large) Separator.unicodeVerticalDouble else Separator.unicodeVerticalSingle)
        if (right) border.withRight(if (large) Separator.unicodeVerticalDouble else Separator.unicodeVerticalSingle)
        if (top) border.withTop(if (large) Separator.unicodeHorizontalDouble else Separator.unicodeHorizontalSingle)
        if (bottom) border.withBottom(if (large) Separator.unicodeHorizontalDouble else Separator.unicodeHorizontalSingle)
        return border.build()
    }
}

fun main() {
    val rubikCube1 = RubikCubeExample(false)
    println("Example 1")
    rubikCube1.render().displayMultiline()
    val rubikCube2 = RubikCubeExample(true)
    println("Example 2")
    rubikCube2.render().displayMultiline()
}