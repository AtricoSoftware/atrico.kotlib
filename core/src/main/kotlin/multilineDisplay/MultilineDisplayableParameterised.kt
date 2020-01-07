package atrico.kotlib.multilineDisplay

/**
 * This object can be displayed as a "2D string"
 * This version takes a Parameter to allow control of the output
 */
interface MultilineDisplayableParameterised<TParam> {
    /**
     * Create 2D representation of object
     * This version takes a [parameter] to allow control of the output
     */
    fun toMultilineString(parameter: TParam): Sequence<String>
}
