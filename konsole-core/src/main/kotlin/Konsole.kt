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
        "${(Attribute.Set.foregroundColor(foreground)+Attribute.Set.backgroundColor(background)).ansiString}$text${(Attribute.Reset.foregroundColor+Attribute.Reset.backgroundColor).ansiString}"
}