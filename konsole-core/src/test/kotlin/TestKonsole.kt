import atrico.kotlib.konsole.core.Attribute
import atrico.kotlib.konsole.core.Color
import atrico.kotlib.konsole.core.Konsole
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TestKonsole : TestKonsoleBase() {

    // region Create coloured string

    @TestFactory
    fun testColoredStringsForeground() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color foreground") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.foreground(text, color)
            println(result)

            // Assert
            val set = Attribute.Set.foregroundColor(color).ansiString
            val reset = Attribute.Reset.foregroundColor.ansiString
            assertThat("Foreground", result, equalTo("$set$text$reset"))
        }
    }

    @TestFactory
    fun testColoredStringsBackground() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color background") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.background(text, color)
            println(result)

            // Assert
            val set = Attribute.Set.backgroundColor(color).ansiString
            val reset = Attribute.Reset.backgroundColor.ansiString
            assertThat("Background", result, equalTo("$set$text$reset"))
        }
    }

    @TestFactory
    fun testColoredStringsColor() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color both") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.color(text, color, color)
            println(result)

            // Assert
            val set = (Attribute.Set.foregroundColor(color) + Attribute.Set.backgroundColor(color)).ansiString
            val reset = (Attribute.Reset.foregroundColor + Attribute.Reset.backgroundColor).ansiString
            assertThat("Colors", result, equalTo("$set$text$reset"))
        }
    }


    // region TODO
/*

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
    fun testParseColours() = ansiCodes_old.map { (color, foreground, background) ->
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

    @Test
    fun testDisplayColors() {
        Table {
            appendRow("Foreground")
            Color.values()
                .forEach { appendRow(Color.colored(it.name, it, Color.BLACK), Color.colored(it.name, it, Color.WHITE)) }
            appendRow("Background")
            Color.values()
                .forEach { appendRow(Color.colored(it.name, Color.BLACK, it), Color.colored(it.name, Color.WHITE, it)) }
        }.render().displayMultiline()
    }

    data class AnsiColorCode private constructor(val color: Color, val foreground: String, val background: String) {
        constructor(color: Color, code: Int) : this(color, "\u001B[${code}m", "\u001B[${code + 10}m")
    }

    private val ansiColorCodes = listOf(
        AnsiColorCode(Color.BLACK, 30),
        AnsiColorCode(Color.RED, 31),
        AnsiColorCode(Color.GREEN, 32),
        AnsiColorCode(Color.YELLOW, 33),
        AnsiColorCode(Color.BLUE, 34),
        AnsiColorCode(Color.MAGENTA, 35),
        AnsiColorCode(Color.CYAN, 36),
        AnsiColorCode(Color.LIGHT_GRAY, 37),
        AnsiColorCode(Color.DARK_GRAY, 90),
        AnsiColorCode(Color.LIGHT_RED, 91),
        AnsiColorCode(Color.LIGHT_GREEN, 92),
        AnsiColorCode(Color.LIGHT_YELLOW, 93),
        AnsiColorCode(Color.LIGHT_BLUE, 94),
        AnsiColorCode(Color.LIGHT_MAGENTA, 95),
        AnsiColorCode(Color.LIGHT_CYAN, 96),
        AnsiColorCode(Color.WHITE, 97)
    )
    */   // endregion TODO


}

