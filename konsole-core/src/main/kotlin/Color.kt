package atrico.kotlib.konsole.core

enum class Color(val baseCode: Int) {
    BLACK(30),
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
    WHITE(97);

    companion object {
        fun fromValue(value: Int): Color? =
            when (value) {
                30 -> BLACK
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
                97 -> WHITE
                else -> null
            }
    }
}