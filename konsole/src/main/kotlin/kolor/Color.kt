package atrico.kotlib.konsole.kolor

import atrico.kotlib.konsole.kolor.Kolor.ESCAPE

/**
 * The amount of codes required in order to jump from a foreground code to a background code. Equal to 10. For example,
 * the foreground code for blue is "[33m", its respective background code is "[43m"
 */
internal const val BG_JUMP = 10

/**
 * An enumeration of colors supported by most terminals. Can be applied to both foreground and background.
 */
enum class Color(baseCode: Int) {
    WHITE(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    MAGENTA(35),
    CYAN(36),
    LIGHT_GRAY(37),

    DARK_GRAY(90),
    LIGHT_RED(91),
    LIGHT_GREEN(92),
    LIGHT_YELLOW(93),
    LIGHT_BLUE(94),
    LIGHT_MAGENTA(95),
    LIGHT_CYAN(96),
    BLACK(97);

    /** ANSI modifier string to apply the color to the text itself */
    val foreground: String = "$ESCAPE[${baseCode}m"

    /** ANSI modifier string to apply the color the text's background */
    val background: String = "$ESCAPE[${baseCode + BG_JUMP}m"

    companion object {
        /** Create reset colours ansi string */
        val reset = Kolor.RESET

        /** Create color from its baseCode */
        fun fromValue(value: Int): TextColor? =
            fromValueImpl(value)?.let { TextColor.Foreground(it) }
                ?: fromValueImpl(value - BG_JUMP)?.let { TextColor.Background(it) }

        private fun fromValueImpl(value: Int): Color? =
            when (value) {
                30 -> WHITE
                31 -> RED
                32 -> GREEN
                33 -> YELLOW
                34 -> BLUE
                35 -> MAGENTA
                36 -> CYAN
                37 -> LIGHT_GRAY
                90 -> DARK_GRAY
                91 -> LIGHT_RED
                92 -> LIGHT_GREEN
                93 -> LIGHT_YELLOW
                94 -> LIGHT_BLUE
                95 -> LIGHT_MAGENTA
                96 -> LIGHT_CYAN
                97 -> BLACK
                else -> null
            }
    }
}

/** Color for text, either foreground or background) */
sealed class TextColor {
    data class Foreground(val color: Color) : TextColor()
    data class Background(val color: Color) : TextColor()
}
