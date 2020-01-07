package atrico.kotlib.testing

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.io.PrintStream

class TestMockOutputStream {
    @Test
    fun testNoOutput() {
        // Arrange
        val mockOutput = MockOutputStream()

        // Assert
        assertThat(mockOutput.lines, equalTo(emptyList()))
    }

    @Test
    fun testMultipleLines() {
        // Arrange
        val mockOutput = MockOutputStream()
        val print = PrintStream(mockOutput)
        val lines = listOf("A", "B", "C").asIterable()

        // Act
        lines.forEach { print.println(it) }

        // Assert
        assertThat(mockOutput.lines, equalTo(lines))
    }

    @Test
    fun testPartialLines() {
        // Arrange
        val mockOutput = MockOutputStream()
        val print = PrintStream(mockOutput)
        val line1a = "ABC"
        val line1b = "abc"
        val line2a = "123"
        val line2b = "456"

        // Act
        print.print(line1a)
        print.println(line1b)
        print.print(line2a)
        val partialResult = mockOutput.lines
        print.println(line2b)

        // Assert
        assertThat("Partial", partialResult, equalTo(listOf(line1a + line1b, line2a).asIterable()))
        assertThat("Full", mockOutput.lines, equalTo(listOf(line1a + line1b, line2a + line2b).asIterable()))
    }

    @Test
    fun testBlankLines() {
        // Arrange
        val mockOutput = MockOutputStream()
        val print = PrintStream(mockOutput)
        val line1 = "ABC"
        val line2 = "123"

        // Act
        print.println(line1)
        print.println()
        print.println(line2)
        print.println()

        // Assert
        assertThat(mockOutput.lines, equalTo(listOf(line1, "", line2, "").asIterable()))
    }

}