package atrico.kotlib.multilineDisplay

/**
 * This object can be displayed as a "2D string"
 */
interface MultilineDisplayable {
    /**
     * Create 2D representation of object
     * This is similar to [toString] but produces a list of strings to be displayed one above the other
     * on a fixed width display (such as the console)
     */
    fun toMultilineString(): Sequence<String>
}
