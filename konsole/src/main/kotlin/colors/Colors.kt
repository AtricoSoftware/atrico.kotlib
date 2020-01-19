package atrico.kotlib.konsole.colors

import atrico.kotlib.konsole.kolor.Color

/**
 * Colours that apply to a cell
 */
data class Colors(val foreground: Color? = null, val background: Color? = null) {
    companion object {
        val none = Colors()
    }
}