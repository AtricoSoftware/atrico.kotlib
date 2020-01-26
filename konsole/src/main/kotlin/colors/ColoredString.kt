package atrico.kotlib.konsole.colors

import atrico.kotlib.konsole.kolor.Kolor

/** String with color information */
data class ColoredString(val string: String, val colors: Colors = Colors.none) {
    override fun toString() = Kolor.colors(string, colors)
}

