package atrico.kotlib.multilineDisplay

import java.io.PrintStream

/**
 * Helper function for displaying [MultilineDisplayable] objects to the specified [stream]
 * By default, the function outputs to [System.out]
 */
fun MultilineDisplayable.displayMultiline(stream: PrintStream = System.out) {
    toMultilineString().forEach { stream.println(it) }
}

/**
 * Helper function for displaying [MultilineDisplayable] objects to the specified [stream]
 * By default, the function outputs to [System.out]
 */
fun <TParam> MultilineDisplayableParameterised<TParam>.displayMultiline(
    parameter: TParam,
    stream: PrintStream = System.out
) {
    toMultilineString(parameter).forEach { stream.println(it) }
}

/**
 * Helper function for displaying non [MultilineDisplayable] objects
 * The existing [toString] is split on newline
 */
fun Any.displayMultiline(stream: PrintStream = System.out) {
    toString().split('\n').forEach { stream.println(it) }
}
