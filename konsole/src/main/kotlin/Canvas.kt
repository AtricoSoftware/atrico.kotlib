package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.Color
import atrico.kotlib.konsole.color.Colors

/**
 * Mutable representation of a 2D display surface
 */
class Canvas : Renderable {

    private val cells = mutableMapOf<Pos, Cell>()

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement = Tile(cells)

    fun getCell(x: Int, y: Int) = getCell(Pos(x, y))
    fun getCell(pos: Pos): Cell? = cells[pos]
    fun getAllCells(): Map<Pos, Cell> = cells

    fun setCell(x: Int, y: Int, char: Char, colors: Colors = Colors.none): Canvas =
        setCell(Pos(x, y), Cell(char, colors))

    fun setCell(pos: Pos, char: Char, colors: Colors = Colors.none): Canvas = setCell(pos, Cell(char, colors))
    fun setCell(x: Int, y: Int, cell: Cell): Canvas = setCell(Pos(x, y), cell)
    fun setCell(pos: Pos, cell: Cell): Canvas {
        cells[pos] = cell
        return this
    }

    fun setString(x: Int, y: Int, obj: Any, flags: Set<CellFlags> = emptySet()): Canvas =
        setString(Pos(x, y), obj, flags)

    fun setString(pos: Pos, obj: Any, flags: Set<CellFlags> = emptySet()): Canvas {
        val parsedString = Color.parseText(obj.toString())
        val stringAsCells = coloredStringsToCells(parsedString)
        for (cell in stringAsCells.withIndex()) {
            cells[pos.right(cell.index)] = cell.value.plusFlags(flags)
        }
        return this
    }

    companion object {
        val blank: Canvas
            get() {
                return Canvas()
            }
    }
}