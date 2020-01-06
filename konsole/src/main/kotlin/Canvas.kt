package atrico.kotlib.konsole

import java.util.*

/**
 * Mutable representation of a 2D display surface
 */
class Canvas : Renderable {

    private val cells = mutableMapOf<Pos, Cell>()

    override fun render(intersectionRules: Iterable<IntersectionRule>): DisplayElement = Tile(cells)

    fun getCell(x: Int, y: Int) = getCell(Pos(x, y))
    fun getCell(pos: Pos): Cell? = cells[pos]
    fun getAllCells(): Map<Pos, Cell> = cells

    fun setCell(x: Int, y: Int, char: Char): Canvas = setCell(Pos(x, y), Cell(char))
    fun setCell(pos: Pos, char: Char): Canvas = setCell(pos, Cell(char))
    fun setCell(x: Int, y: Int, cell: Cell): Canvas = setCell(Pos(x, y), cell)
    fun setCell(pos: Pos, cell: Cell): Canvas {
        cells[pos] = cell
        return this
    }

    fun setString(x: Int, y: Int, obj: Any, flags: EnumSet<CellFlags> = EnumSet.noneOf(CellFlags::class.java)): Canvas =
        setString(Pos(x, y), obj, flags)

    fun setString(pos: Pos, obj: Any, flags: EnumSet<CellFlags> = EnumSet.noneOf(CellFlags::class.java)): Canvas {
        var idx = 0;
        for (ch in obj.toString()) {
            cells[pos.right(idx++)] = Cell(ch, flags)
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