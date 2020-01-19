import atrico.kotlib.konsole.DisplayElement
import atrico.kotlib.konsole.Renderable
import atrico.kotlib.konsole.kolor.stripColors
import atrico.kotlib.multilineDisplay.displayMultiline
import atrico.kotlib.testing.TestBase
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

abstract class DisplayElementTestBase : TestBase() {
    protected fun assertDisplay(renderable: Renderable, vararg colorLines: String) =
        assertDisplay(renderable.render(), colorLines.asIterable())

    protected fun assertDisplay(renderable: Renderable, colorLines: Iterable<String>) =
        assertDisplay(renderable.render(), colorLines)

    protected fun assertDisplay(element: DisplayElement, vararg colorLines: String) =
        assertDisplay(element, colorLines.asIterable())

    protected fun assertDisplay(element: DisplayElement, colorLines: Iterable<String>) {
        element.displayMultiline()
        val lines = colorLines.map { it.stripColors() }
        val width = lines.map { it.length }.max() ?: 0
        assertThat("Width", element.width, equalTo(width))
        val height = lines.count()
        assertThat("Height", element.height, equalTo(height))
        // Cells
        (0 until height).forEach { itY ->
            (0 until width).forEach { itX ->
                val cell = element.getCell(itX, itY)?.content?.char ?: ' '
                val expected = lines.elementAt(itY).padEnd(width)[itX]
                assertThat("Cells: ($itX,$itY)", cell, equalTo(expected))
            }
        }
        // Multiline
        val display = element.toMultilineString().asIterable()
        assertThat("Display: Line count", display.count(), equalTo(height));
        var i = 0
        for (pair in display.zip(colorLines)) {
            assertThat("Display: Line ${i++}", pair.first, equalTo(pair.second));
        }
    }
}