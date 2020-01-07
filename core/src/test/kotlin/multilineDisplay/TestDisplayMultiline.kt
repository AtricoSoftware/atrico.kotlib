package multilineDisplay

import atrico.kotlib.multilineDisplay.MultilineDisplayable
import atrico.kotlib.multilineDisplay.MultilineDisplayableParameterised
import atrico.kotlib.multilineDisplay.displayMultiline
import atrico.kotlib.testing.TestBase
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.io.OutputStream
import java.io.PrintStream

class TestDisplayMultiline : TestBase() {
    @Test
    fun testNoParameters() {
        // Arrange
        val lines: Iterable<String> = listOf("Line1", "Line2", "Line3")
        val obj = TestClass1(lines)
        val buffer = StringListOutputStream()

        // Act
        obj.displayMultiline(PrintStream(buffer))

        // Assert
        assertThat("lines", buffer.getLines(), equalTo(lines))
    }

    @Test
    fun testParameter() {
        // Arrange
        val lines: Iterable<String> = listOf("Line1", "Line2", "Line3")
        val obj = TestClass1(lines)
        val buffer = StringListOutputStream()

        // Act
        obj.displayMultiline('>', PrintStream(buffer))

        // Assert
        assertThat("lines", buffer.getLines(), equalTo(lines.map { ">$it" }.asIterable()))
    }

    @Test
    fun testHelperWithNoInterfaceSingleLine() {
        // Arrange
        val obj = "hello"
        val buffer = StringListOutputStream()

        // Act
        obj.displayMultiline(PrintStream(buffer))

        // Assert
        assertThat("lines", buffer.getLines(), equalTo(listOf("hello").asIterable()))
    }

    @Test
    fun testHelperWithNoInterface() {
        // Arrange
        val lines: Iterable<String> = listOf("Line1", "Line2", "Line3")
        val obj = TestClass2(lines)
        val buffer = StringListOutputStream()

        // Act
        obj.displayMultiline(PrintStream(buffer))

        // Assert
        assertThat("lines", buffer.getLines(), equalTo(lines))
    }

    private class TestClass1(private val lines: Iterable<String>) : MultilineDisplayableParameterised<Char?>,
        MultilineDisplayable {

        override fun toMultilineString(parameter: Char?): Sequence<String> = sequence {
            lines.forEach {
                yield("${parameter ?: ""}$it")
            }
        }

        override fun toMultilineString(): Sequence<String> = toMultilineString(null)
    }

    private class TestClass2(private val lines: Iterable<String>) {
        override fun toString() = lines.joinToString("\n")
    }

    private class StringListOutputStream : OutputStream() {
        private val lines = ArrayList<String>()
        private val buffer = StringBuilder()

        fun getLines(): Iterable<String> {
            terminateLine()
            return lines
        }

        override fun write(b: Int) {
            when (b.toChar()) {
                '\r' -> {
                } // Ignore
                '\n' -> terminateLine()
                else -> buffer.append(b.toChar())
            }
        }

        private fun terminateLine() {
            if (buffer.isNotEmpty()) {
                lines.add(buffer.toString())
                buffer.clear()
            }
        }
    }
}

