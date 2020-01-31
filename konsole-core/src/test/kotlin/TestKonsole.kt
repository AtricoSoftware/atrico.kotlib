import atrico.kotlib.konsole.core.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class TestKonsole : TestKonsoleBase() {

    @TestFactory
    fun testColoredStringsForeground() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color foreground") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.foreground(text, color)
            val resultExtension = text.foregroundColor(color)
            println(result)
            println(resultExtension)

            // Assert
            val set = Attribute.Set.foregroundColor(color).ansiString
            val reset = Attribute.Reset.foregroundColor.ansiString
            assertThat("Foreground", result, equalTo("$set$text$reset"))
            assertThat("Foreground extension", resultExtension, equalTo("$set$text$reset"))
        }
    }

    @TestFactory
    fun testColoredStringsBackground() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color background") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.background(text, color)
            val resultExtension = text.backgroundColor(color)
            println(result)
            println(resultExtension)

            // Assert
            val set = Attribute.Set.backgroundColor(color).ansiString
            val reset = Attribute.Reset.backgroundColor.ansiString
            assertThat("Background", result, equalTo("$set$text$reset"))
            assertThat("Background extension", resultExtension, equalTo("$set$text$reset"))
        }
    }

    @TestFactory
    fun testColoredStringsColor() = Color.values().map { color ->
        DynamicTest.dynamicTest("Color string with $color both") {
            // Arrange
            val text = randomString()

            // Act
            val result = Konsole.color(text, color, color)
            val resultExtension = text.colored(color, color)
            println(result)
            println(resultExtension)

            // Assert
            val set = (Attribute.Set.foregroundColor(color) + Attribute.Set.backgroundColor(color)).ansiString
            val reset = (Attribute.Reset.foregroundColor + Attribute.Reset.backgroundColor).ansiString
            assertThat("Colors", result, equalTo("$set$text$reset"))
            assertThat("Colors extension", resultExtension, equalTo("$set$text$reset"))
        }
    }

    @Test
    fun testBold() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.bold(text)
        val resultExtension = text.bold()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.bold).ansiString
        val reset = Attribute.Reset.bold.ansiString
        assertThat("Bold", result, equalTo("$set$text$reset"))
        assertThat("Bold extension", resultExtension, equalTo("$set$text$reset"))
    }

    @Test
    fun testDim() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.dim(text)
        val resultExtension = text.dim()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.dim).ansiString
        val reset = Attribute.Reset.dim.ansiString
        assertThat("Dim", result, equalTo("$set$text$reset"))
        assertThat("Dim extension", resultExtension, equalTo("$set$text$reset"))
    }

    @Test
    fun testUnderline() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.underline(text)
        val resultExtension = text.underline()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.underline).ansiString
        val reset = Attribute.Reset.underline.ansiString
        assertThat("Underline", result, equalTo("$set$text$reset"))
        assertThat("Underline extension", resultExtension, equalTo("$set$text$reset"))
    }

    @Test
    fun testBlink() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.blink(text)
        val resultExtension = text.blink()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.blink).ansiString
        val reset = Attribute.Reset.blink.ansiString
        assertThat("Blink", result, equalTo("$set$text$reset"))
        assertThat("Blink extension", resultExtension, equalTo("$set$text$reset"))
    }

    @Test
    fun testInvert() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.invert(text)
        val resultExtension = text.invert()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.invert).ansiString
        val reset = Attribute.Reset.invert.ansiString
        assertThat("Invert", result, equalTo("$set$text$reset"))
        assertThat("Invert extension", resultExtension, equalTo("$set$text$reset"))
    }

    @Test
    fun testHidden() {
        // Arrange
        val text = randomString()

        // Act
        val result = Konsole.hide(text)
        val resultExtension = text.hide()
        println(result)
        println(resultExtension)

        // Assert
        val set = (Attribute.Set.hidden).ansiString
        val reset = Attribute.Reset.hidden.ansiString
        assertThat("Hidden", result, equalTo("$set$text$reset"))
        assertThat("Hidden extension", resultExtension, equalTo("$set$text$reset"))
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

