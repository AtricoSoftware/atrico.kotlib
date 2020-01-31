package atrico.kotlib.konsole.core

object Konsole {
    /**
     * Return a string with ansi foreground color codes (and reset at the end)
     */
    fun foreground(text: String, color: Color) =
        "${Attribute.Set.foregroundColor(color).ansiString}$text${Attribute.Reset.foregroundColor.ansiString}"

    /**
     * Return a string with ansi background color codes (and reset at the end)
     */
    fun background(text: String, color: Color) =
        "${Attribute.Set.backgroundColor(color).ansiString}$text${Attribute.Reset.backgroundColor.ansiString}"

    /**
     * Return a string with ansi foreground and background color codes (and reset at the end)
     */
    fun color(text: String, foreground: Color, background: Color) =
        "${(Attribute.Set.foregroundColor(foreground) + Attribute.Set.backgroundColor(background)).ansiString}$text${(Attribute.Reset.foregroundColor + Attribute.Reset.backgroundColor).ansiString}"

    /**
     * Return a string with ansi bold code (and reset at the end)
     */
    fun bold(text: String) = "${Attribute.Set.bold.ansiString}$text${Attribute.Reset.bold.ansiString}"

    /**
     * Return a string with ansi dim code (and reset at the end)
     */
    fun dim(text: String) = "${Attribute.Set.dim.ansiString}$text${Attribute.Reset.dim.ansiString}"

    /**
     * Return a string with ansi underline code (and reset at the end)
     */
    fun underline(text: String) = "${Attribute.Set.underline.ansiString}$text${Attribute.Reset.underline.ansiString}"

    /**
     * Return a string with ansi blink code (and reset at the end)
     */
    fun blink(text: String) = "${Attribute.Set.blink.ansiString}$text${Attribute.Reset.blink.ansiString}"

    /**
     * Return a string with ansi invert code (and reset at the end)
     */
    fun invert(text: String) = "${Attribute.Set.invert.ansiString}$text${Attribute.Reset.invert.ansiString}"

    /**
     * Return a string with ansi hidden code (and reset at the end)
     */
    fun hide(text: String) = "${Attribute.Set.hidden.ansiString}$text${Attribute.Reset.hidden.ansiString}"

    /**
     * Return a string with ansi codes (and reset at the end)
     */
    fun withAttributes(text: String, attributes: Iterable<Attribute>): String {
        val set = Attribute.combine(attributes)
        return "${set.ansiString}$text${set.reset.ansiString}"
    }

    /**
     * Return a string with ansi codes (and reset at the end) - vararg
     */
    fun withAttributes(text: String, vararg attributes: Attribute) = withAttributes(text, attributes.asIterable())
}