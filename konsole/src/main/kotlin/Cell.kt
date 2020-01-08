package atrico.kotlib.konsole

/**
 * Contents of a cell
 */
class Cell(val char: Char, private val flags: Set<CellFlags>) {
    constructor(char: Char, vararg flags: CellFlags) : this(char, flags.toSet())

    /**
     * Is this [flag] applied to this [Cell]
     */
    fun hasFlag(flag: CellFlags) = flags.contains(flag)

    override fun toString(): String = "$char:(${flags.joinToString()})"
}

/**
 * Flags applied to a [Cell]
 */
enum class CellFlags {
    SEPARATOR
}
