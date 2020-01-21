package atrico.kotlib.konsole

import atrico.kotlib.konsole.color.ColoredChar
import atrico.kotlib.konsole.color.Colors

/**
 * Contents of a cell
 */
class Cell(val content: ColoredChar, private val flags: Set<CellFlags>) {
    constructor(char: Char, colors: Colors, vararg flags: CellFlags) : this(ColoredChar(char, colors), flags.toSet())
    constructor(char: Char, vararg flags: CellFlags) : this(char, flags.toSet())
    constructor(content: ColoredChar, vararg flags: CellFlags) : this(content, flags.toSet())
    constructor(char: Char, flags: Set<CellFlags>) : this(ColoredChar(char), flags)

    /**
     * Is this [flag] applied to this [Cell]
     */
    fun hasFlag(flag: CellFlags) = flags.contains(flag)

    /**
     * Create new [Cell] with extra flags
     */
    fun plusFlags(newFlags: Set<CellFlags>) = Cell(content, flags + newFlags)

    override fun toString(): String = "${content.char}, (${content.colors}):[${flags.joinToString()}]"
}

/**
 * Flags applied to a [Cell]
 */
enum class CellFlags {
    SEPARATOR
}
