package atrico.kotlib.konsole.color

/**
 * Colours that apply to a cell
 */
data class Colors(val foreground: Color? = null, val background: Color? = null) {
    /**
     * Create a new object with the foreground changed
     */
    fun setForeground(color: Color?) = Colors(color, background)

    /**
     * Create a new object with the background changed
     */
    fun setBackground(color: Color?) = Colors(foreground, color)

    companion object {
        val none = Colors()
    }
}