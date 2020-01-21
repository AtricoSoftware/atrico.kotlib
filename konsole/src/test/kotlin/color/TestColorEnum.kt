package color

import atrico.kotlib.konsole.color.*
import atrico.kotlib.testing.TestBase
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class TestColorEnum : TestBase() {
    @TestFactory
    fun testAnsiStrings() = ansiCodes.map { (color, expectedForeground, expectedBackground) ->
        DynamicTest.dynamicTest("Color $color produces ANSI strings: Foreground = $expectedForeground, Background = $expectedBackground") {
            // Act
            val resultForeground = color.foreground
            val resultBackground = color.background

            // Assert
            assertThat("Foreground", resultForeground, equalTo(expectedForeground))
            assertThat("Background", resultBackground, equalTo(expectedBackground))
        }
    }

    @Test
    fun testReset() {
        // Act
        val result = Color.reset

        // Assert
        assertThat("Reset", result, equalTo("\u001B[0m"))
    }

    // region Create coloured string

    @TestFactory
    fun testColoredStrings() = ansiCodes.map { (color, foreground, background) ->
        DynamicTest.dynamicTest("Color string with $color") {
            // Arrange
            val text = randomString()

            // Act
            val resultForeground = Color.foreground(text, color)
            val resultBackground = Color.background(text, color)
            val resultBoth = Color.colored(text, color, color)
            val resultBothColors = Color.colored(text, Colors(color, color))
            println("Foreground = $resultForeground")
            println("Background = $resultBackground")
            println("Both = $resultBoth")
            println("Both (Colors) = $resultBothColors")

            // Assert
            assertThat("Foreground", resultForeground, equalTo("$foreground$text${Color.reset}"))
            assertThat("Background", resultBackground, equalTo("$background$text${Color.reset}"))
            assertThat("Both", resultBoth, equalTo("$foreground$background$text${Color.reset}"))
            assertThat("Both (Colors)", resultBothColors, equalTo("$foreground$background$text${Color.reset}"))
        }
    }

    @Test
    fun testNoResetIfNoColor() {
        // Arrange
        val text = randomString()

        // Act
        val resultBoth = Color.colored(text, null, null)
        val resultBothColors = Color.colored(text, Colors.none)
        println("Both = $resultBoth")
        println("Both (Colors) = $resultBothColors")

        // Assert
        assertThat("Both", resultBoth, equalTo(text))
        assertThat("Both (Colors)", resultBothColors, equalTo(text))
    }

    // endregion Create coloured string

    // region Parse
    @TestFactory
    fun testParseColours() = ansiCodes.map { (color, foreground, background) ->
        DynamicTest.dynamicTest("Foreground = $foreground, Background = $background parses to $color") {
            // Act
            val resultForeground = Color.parse(foreground)
            val resultBackground = Color.parse(background)

            // Assert
            assertThat("Foreground", resultForeground, equalTo(TextColor.Foreground(color) as TextColor))
            assertThat("Background", resultBackground, equalTo(TextColor.Background(color) as TextColor))
        }
    }

    @Test
    fun testParseReset() {
        // Act
        val result = Color.parse("\u001B[0m")

        // Assert
        assertThat("Reset", result, equalTo(TextColor.Reset as TextColor))
    }

    @Test
    fun testParseTextNoColours() {
        // Arrange
        val text = randomString()
        val expected = listOf(ColoredString(text, Colors.none)).asIterable()

        // Act
        val result = Color.parseText(text)
        val resultExtension = text.parseColoredText()

        // Assert
        assertThat("Func", result, equalTo(expected))
        assertThat("Extension", resultExtension, equalTo(expected))
    }

    @Test
    fun testParseTextWithForeground() {
        // Arrange
        val text = randomString()
        val colored = Color.foreground(text, Color.RED)
        val expected = listOf(ColoredString(text, Colors(Color.RED))).asIterable()

        // Act
        val result = Color.parseText(colored)
        val resultExtension = colored.parseColoredText()

        // Assert
        assertThat("Func", result, equalTo(expected))
        assertThat("Extension", resultExtension, equalTo(expected))
    }

    @Test
    fun testParseTextWithBackground() {
        // Arrange
        val text = randomString()
        val colored = Color.background(text, Color.RED)
        val expected = listOf(ColoredString(text, Colors(background = Color.RED))).asIterable()

        // Act
        val result = Color.parseText(colored)
        val resultExtension = colored.parseColoredText()

        // Assert
        assertThat("Func", result, equalTo(expected))
        assertThat("Extension", resultExtension, equalTo(expected))
    }

    @Test
    fun testParseTextWithBoth() {
        // Arrange
        val text = randomString()
        val colored = Color.colored(text, Color.RED, Color.BLUE)
        val expected = listOf(ColoredString(text, Colors(Color.RED, Color.BLUE))).asIterable()

        // Act
        val result = Color.parseText(colored)
        val resultExtension = colored.parseColoredText()

        // Assert
        assertThat("Func", result, equalTo(expected))
        assertThat("Extension", resultExtension, equalTo(expected))
    }

    @Test
    fun testParseTextWithMultiple() {
        // Arrange
        val textParts = uniqueValues(7) { randomString() }
        val colored = textParts[0] +
                Color.foreground(textParts[1], Color.RED) +
                textParts[2] +
                Color.colored(textParts[3], Color.GREEN, Color.YELLOW) +
                textParts[4] +
                Color.background(textParts[5], Color.BLUE) +
                textParts[6]
        val expected = listOf(
            ColoredString(textParts[0], Colors.none),
            ColoredString(textParts[1], Colors(Color.RED)),
            ColoredString(textParts[2], Colors.none),
            ColoredString(textParts[3], Colors(Color.GREEN, Color.YELLOW)),
            ColoredString(textParts[4], Colors.none),
            ColoredString(textParts[5], Colors(background = Color.BLUE)),
            ColoredString(textParts[6], Colors.none)
        ).asIterable()

        // Act
        val result = Color.parseText(colored)
        val resultExtension = colored.parseColoredText()

        // Assert
        assertThat("Func", result, equalTo(expected))
        assertThat("Extension", resultExtension, equalTo(expected))
    }

    // endregion Parse

    @Test
    fun testStripColorsNoColours() {
        // Arrange
        val text = randomString()

        // Act
        val result = Color.stripColors(text)
        val resultExtension = text.stripColors()

        // Assert
        assertThat("Func", result, equalTo(text))
        assertThat("Extension", resultExtension, equalTo(text))
    }

    @Test
    fun testStripColors() {
        // Arrange
        val text = randomString()
        val withForeground = Color.foreground(text, Color.RED)
        val withBackground = Color.background(text, Color.BLUE)
        val withBoth = Color.colored(text, Color.BLACK, Color.WHITE)

        // Act
        val resultForeground = Color.stripColors(withForeground)
        val resultForegroundExtension = withForeground.stripColors()
        val resultBackground = Color.stripColors(withBackground)
        val resultBackgroundExtension = withBackground.stripColors()
        val resultBoth = Color.stripColors(withBoth)
        val resultBothExtension = withBoth.stripColors()

        // Assert
        assertThat("Foreground", resultForeground, equalTo(text))
        assertThat("Foreground Extension", resultForegroundExtension, equalTo(text))
        assertThat("Background", resultBackground, equalTo(text))
        assertThat("Background Extension", resultBackgroundExtension, equalTo(text))
        assertThat("Both", resultBoth, equalTo(text))
        assertThat("Both Extension", resultBothExtension, equalTo(text))
    }

    data class AnsiCode private constructor(val color: Color, val foreground: String, val background: String) {
        constructor(color: Color, code: Int) : this(color, "\u001B[${code}m", "\u001B[${code + 10}m")
    }

    private val ansiCodes = listOf(
        AnsiCode(Color.WHITE, 30),
        AnsiCode(Color.RED, 31),
        AnsiCode(Color.GREEN, 32),
        AnsiCode(Color.YELLOW, 33),
        AnsiCode(Color.BLUE, 34),
        AnsiCode(Color.MAGENTA, 35),
        AnsiCode(Color.CYAN, 36),
        AnsiCode(Color.LIGHT_GRAY, 37),
        AnsiCode(Color.DARK_GRAY, 90),
        AnsiCode(Color.LIGHT_RED, 91),
        AnsiCode(Color.LIGHT_GREEN, 92),
        AnsiCode(Color.LIGHT_YELLOW, 93),
        AnsiCode(Color.LIGHT_BLUE, 94),
        AnsiCode(Color.LIGHT_MAGENTA, 95),
        AnsiCode(Color.LIGHT_CYAN, 96),
        AnsiCode(Color.BLACK, 97)
    )
}

