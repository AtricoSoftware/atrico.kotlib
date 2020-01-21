package atrico.kotlib.konsole.color

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

    /**
     *  ANSI modifier string to apply the color to the text itself
     */
    val foreground: String = "${Color.ESCAPE}[${baseCode}m"

    /**
     *  ANSI modifier string to apply the color the text's background
     */
    val background: String = "${Color.ESCAPE}[${baseCode + Color.BG_JUMP}m"

    companion object {
        /** Offset to foreground codes to produce background ansi strings */
        private const val BG_JUMP = 10
        /** Escape code for ANSI strings */
        private const val ESCAPE = '\u001B'
        /** Regex for parsing ANSI control strings */
        private val ansiRegex = Regex("$ESCAPE\\[(\\d+)m")

        /**
         * Create reset colours ansi string
         */
        const val reset = "$ESCAPE[0m"

        // region Create coloured string

        /**
         * Return a string with the specified foreground color
         * String will terminate with a reset
         */
        fun foreground(obj: Any, color: Color) = colored(obj, Colors(color))

        /**
         * Return a string with the specified background color
         * String will terminate with a reset
         */
        fun background(obj: Any, color: Color) = colored(obj, Colors(background = color))

        /**
         * Return a string with the specified colors
         * String will terminate with a reset
         */
        fun colored(obj: Any, foreground: Color?, background: Color?) = colored(obj, Colors(foreground, background))

        /**
         * Return a string with the specified colors
         * String will terminate with a reset
         */
        fun colored(obj: Any, colors: Colors) = "${colors.foreground?.foreground ?: ""}${colors.background?.background
            ?: ""}$obj${colors.foreground?.let { reset } ?: colors.background?.let { reset } ?: ""}"

        // endregion Create coloured string

        /**
         * Create color from its baseCode
         */
        fun fromValue(value: Int): TextColor? =
            if (value == 0) TextColor.Reset
            else fromValueImpl(value)?.let { TextColor.Foreground(it) }
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

        // region Parse
        /**
         * Parse an ansi code into the color (or null if not value)
         */
        fun parse(text: String): TextColor? = parseRegexMatch(ansiRegex.matchEntire(text))

        /** Parse a string (with ANSI codes) into multi coloured parts
         * @param text The string to parse
         * @return List of partial strings with their corresponding colours
         * */
        fun parseText(text: String): Iterable<ColoredString> {
            val matches = ansiRegex.findAll(text).map { it.groups[0] }.filterNotNull()
            val parts = ArrayList<ColoredString>()
            var currentColors = Colors.none
            var idx = 0
            matches.forEach {
                // Add text since last color control with current colors
                if (it.range.first > idx) {
                    parts.add(ColoredString(text.substring(idx, it.range.first), currentColors))
                }
                // Calculate colour change
                currentColors = when (val colorChange = parse(it.value)) {
                    is TextColor.Foreground -> currentColors.setForeground(colorChange.color)
                    is TextColor.Background -> currentColors.setBackground(colorChange.color)
                    is TextColor.Reset -> Colors.none
                    else -> currentColors
                }
                idx = it.range.last + 1
            }
            if (idx < text.length) {
                parts.add(ColoredString(text.substring(idx), currentColors))
            }
            return parts
        }

        /**
         * Check a match result (on [ansiRegex]) and return color code or null
         */
        private fun parseRegexMatch(match: MatchResult?): TextColor? =
            match?.groups?.get(1)?.value?.toInt()?.let { fromValue(it) }

        // endregion Parse

        /**
         * Strip the color codes from a string
         * @param string The string to parse
         * @return Simple string with no colors
         */
        fun stripColors(string: String): String = ansiRegex.split(string).joinToString("")

    }
}

