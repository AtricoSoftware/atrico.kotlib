import atrico.kotlib.konsole.*
import atrico.kotlib.konsole.colors.ColoredChar
import atrico.kotlib.konsole.colors.Colors
import atrico.kotlib.konsole.kolor.Color
import atrico.kotlib.multilineDisplay.displayMultiline

/**
 * Example of how a Rubik's cube could be rendered
 * NOTE: There is no attempt here to model the positions of the cube
 */
class RubikCubeExample(
    private val large: Boolean
) : Renderable {

    override fun render(intersectionRules: Iterable<IntersectionRule>) =
        createTable(SeparatorType.NONE) {
            setCell(1, 0, createFace(Color.WHITE, left = true, right = true, top = true))
            setCell(0, 1, createFace(Color.GREEN, left = true, top = true, bottom = true))
            setCell(1, 1, createFace(Color.RED, left = true, right = true, top = true, bottom = true))
            setCell(2, 1, createFace(Color.BLUE, right = true, top = true, bottom = true))
            setCell(3, 1, createFace(Color.YELLOW, right = true, top = true, bottom = true))
            setCell(1, 2, createFace(Color.LIGHT_YELLOW, left = true, right = true, bottom = true))
        }.render(intersectionRules)


    private fun createFace(
        color: Color,
        left: Boolean = false,
        right: Boolean = false,
        top: Boolean = false,
        bottom: Boolean = false
    ) = Border(
        Table {
            Pos.allPos(3, 3).forEach {
                setCell(it, ColoredChar(' ', Colors(background = color)))
            }
            if (large) withSeparatorsUnicodeSingle()
        }
    ) {
        if (left) withLeft(if (large) Separator.unicodeVerticalDouble else Separator.unicodeVerticalSingle)
        if (right) withRight(if (large) Separator.unicodeVerticalDouble else Separator.unicodeVerticalSingle)
        if (top) withTop(if (large) Separator.unicodeHorizontalDouble else Separator.unicodeHorizontalSingle)
        if (bottom) withBottom(if (large) Separator.unicodeHorizontalDouble else Separator.unicodeHorizontalSingle)
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