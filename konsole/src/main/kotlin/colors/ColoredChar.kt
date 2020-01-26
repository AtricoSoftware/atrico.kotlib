package atrico.kotlib.konsole.colors

import atrico.kotlib.konsole.kolor.Kolor

/** Char with color information */
data class ColoredChar(val char: Char, val colors: Colors = Colors.none) {
    override fun toString() = Kolor.colors(char.toString(), colors)
}