package atrico.kotlib.konsole.color

/** String with color information */
data class ColoredString(val string: String, val colors: Colors = Colors.none) {
    override fun toString() = Color.colored(string, colors)
}

