package atrico.kotlib.testing

import java.io.OutputStream

/**
 * Mock [OutputStream]
 * Stores lines output
 */
class MockOutputStream : OutputStream() {
    private val outputLines = ArrayList<String>()
    private val buffer = StringBuilder()

    /**
     * The lines that were output
     */
    val lines: Iterable<String>
        get() =
            if (buffer.isNotEmpty()) outputLines + buffer.toString() else outputLines

    override fun write(b: Int) {
        when (b.toChar()) {
            '\r' -> {
            } // Ignore
            '\n' -> terminateLine()
            else -> buffer.append(b.toChar())
        }
    }

    private fun terminateLine() {
        outputLines.add(buffer.toString())
        buffer.clear()
    }
}
