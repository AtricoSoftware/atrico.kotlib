package atrico.kotlib.konsole.color

/** Char with color information */
data class ColoredChar(val char: Char, val colors: Colors = Colors.none) {
    override fun toString() = Color.colored(char, colors)
}