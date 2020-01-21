package atrico.kotlib.konsole.color

/**
 *  Color for text, either foreground, background or a reset)
 */
sealed class TextColor {
    data class Foreground(val color: Color) : TextColor()
    data class Background(val color: Color) : TextColor()
    object Reset : TextColor()
}