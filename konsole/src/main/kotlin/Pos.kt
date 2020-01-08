package atrico.kotlib.konsole

data class Pos(val x: Int, val y: Int) {

    operator fun plus(rhs: Pos) = Pos(x + rhs.x, y + rhs.y)
    operator fun minus(rhs: Pos) = Pos(x - rhs.x, y - rhs.y)
    operator fun times(rhs: Int) = Pos(x * rhs, y * rhs)

    companion object {
        val ORIGIN by lazy { Pos(0, 0) }
        fun allPos(width: Int, height: Int) = allPos(ORIGIN, Pos(width - 1, height - 1))
        fun allPos(topRight: Pos, bottomLeft: Pos) =
            sequence {
                (topRight.x..bottomLeft.x).forEach { itX ->
                    (topRight.y..bottomLeft.y).forEach { itY ->
                        yield(Pos(itX, itY))
                    }
                }
            }

        fun topLeft(vararg list: Pos) = Pos(list.map { it.x }.min() ?: 0, list.map { it.y }.min() ?: 0)
        fun bottomRight(vararg list: Pos) = Pos(list.map { it.x }.max() ?: 0, list.map { it.y }.max() ?: 0)

        fun up(offset: Int = 1): Pos = ORIGIN.up(offset)
        fun down(offset: Int = 1): Pos = ORIGIN.down(offset)
        fun left(offset: Int = 1): Pos = ORIGIN.left(offset)
        fun right(offset: Int = 1): Pos = ORIGIN.right(offset)
        fun upLeft(offset: Int = 1): Pos = ORIGIN.upLeft(offset)
        fun upRight(offset: Int = 1): Pos = ORIGIN.upRight(offset)
        fun downLeft(offset: Int = 1): Pos = ORIGIN.downLeft(offset)
        fun downRight(offset: Int = 1): Pos = ORIGIN.downRight(offset)
    }
}

fun Pos.up(offset: Int = 1): Pos = Pos(x, y - offset)
fun Pos.down(offset: Int = 1): Pos = Pos(x, y + offset)
fun Pos.left(offset: Int = 1): Pos = Pos(x - offset, y)
fun Pos.right(offset: Int = 1): Pos = Pos(x + offset, y)
fun Pos.upLeft(offset: Int = 1): Pos = Pos(x - offset, y - offset)
fun Pos.upRight(offset: Int = 1): Pos = Pos(x + offset, y - offset)
fun Pos.downLeft(offset: Int = 1): Pos = Pos(x - offset, y + offset)
fun Pos.downRight(offset: Int = 1): Pos = Pos(x + offset, y + offset)

fun Pos.topLeft(other: Pos) = Pos.topLeft(this, other)
fun Pos.bottomRight(other: Pos) = Pos.bottomRight(this, other)