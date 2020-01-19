package atrico.kotlib.konsole.kolor

import atrico.kotlib.konsole.colors.ColoredString
import atrico.kotlib.konsole.colors.Colors

/**
 * Object to add color information to strings
 * @author Andrea Pivetta
 */
object Kolor {
    internal const val ESCAPE = '\u001B'
    internal const val RESET = "$ESCAPE[0m"
    private val ansiRegex = Regex("$ESCAPE\\[(\\d+)m")

    /**
     * Create a string that will be printed with the specified color as foreground
     * @param string The string to color
     * @param color The color to use
     * @return The colored string
     */
    fun foreground(string: String, color: Color) = color(string, color.foreground)

    /**
     * Create a string that will be printed with the specified color as background
     * @param string The string to color
     * @param color The color to use
     * @return The colored string
     */
    fun background(string: String, color: Color) = color(string, color.background)

    /**
     * Create a string that will be printed with the specified color as background
     * @param string The string to color
     * @param colors The colors to use
     * @return The colored string
     */
    fun colors(string: String, colors: Colors): String {
        val ansiString = colors.foreground?.foreground ?: "" + colors.background?.background ?: ""
        return if (ansiString.isBlank()) string
        else color(string, ansiString)
    }


    /**
     * Create a string that will be printed with the specified color as background
     * @param string The string to color
     * @param foreground The foreground color to use
     * @param background The background color to use
     * @return The colored string
     */
    fun colors(string: String, foreground: Color, background: Color) =
        color(string, foreground.foreground + background.background)

    /** Parse an ANSI string into multi coloured parts
     * @param string The string to parse
     * @return List of partial strings with their corresponding colours
     * */
    fun parse(string: String): Iterable<ColoredString> {
        val matches = ansiRegex.findAll(string)
        val parts = ArrayList<ColoredString>()
        var fore: Color? = null
        var back: Color? = null
        var idx = 0
        matches.forEach {
            val range = it.groups[0]!!.range
            if (range.first > idx) {
                parts.add(
                    ColoredString(
                        string.substring(
                            idx,
                            range.first
                        ), Colors(fore, back)
                    )
                )
            }
            val colorValue = it.groups[1]!!.value.toInt()
            if (colorValue == 0) {
                fore = null
                back = null
            } else {
                when (val color = Color.fromValue(colorValue)) {
                    is TextColor.Foreground -> fore = color.color
                    is TextColor.Background -> back = color.color
                    else -> {
                    } // Invalid color, ignore!
                }
            }
            idx = range.last + 1
        }
        if (idx < string.length) {
            parts.add(ColoredString(string.substring(idx), Colors(fore, back)))
        }
        return parts
    }

    /**
     * Strip theo color codes from a string
     * @param string The string to parse
     * @return Simple string with no colors
     */
    fun stripColors(string: String): String = ansiRegex.split(string).joinToString("")

    private fun color(string: String, ansiString: String) = "$ansiString$string$RESET"
}

