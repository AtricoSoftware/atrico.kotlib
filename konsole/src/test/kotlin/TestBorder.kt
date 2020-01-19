import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.IntersectionRuleImpl
import atrico.kotlib.konsole.Tile
import atrico.kotlib.konsole.colors.Colors
import atrico.kotlib.konsole.kolor.*
import org.junit.jupiter.api.Test

class TestBorder : DisplayElementTestBase() {
    private val content = Tile("123", "456", "789")
    private val contentLines = content.toMultilineString().asIterable()

    @Test
    fun testNoBorder() {
        // Act
        val border = Border.Builder(content)
            .build()

        // Assert
        assertDisplay(border, contentLines)
    }

    @Test
    fun testLeft() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l')
            .build()

        // Assert
        assertDisplay(border, contentLines.map { "l$it" })
    }

    @Test
    fun testRight() {
        // Act
        val border = Border.Builder(content)
            .withRight('r')
            .build()

        // Assert
        assertDisplay(border, contentLines.map { "${it}r" })
    }

    @Test
    fun testTop() {
        // Act
        val border = Border.Builder(content)
            .withTop('t')
            .build()

        // Assert
        assertDisplay(border, listOf("ttt") + contentLines)
    }

    @Test
    fun testBottom() {
        // Act
        val border = Border.Builder(content)
            .withBottom('b')
            .build()

        // Assert
        assertDisplay(border, contentLines + "bbb")
    }

    @Test
    fun testAllNoRules() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l')
            .withRight('r')
            .withTop('t')
            .withBottom('b')
            .build()

        // Assert
        assertDisplay(border.render(emptyList()), listOf("ttttt") + contentLines.map { "l${it}r" } + "bbbbb")
    }

    @Test
    fun testAllWithRules() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l')
            .withRight('r')
            .withTop('t')
            .withBottom('b')
            .build()
        val rules = listOf(
            IntersectionRuleImpl('A', right = 't', below = 'l'),
            IntersectionRuleImpl('B', left = 't', below = 'r'),
            IntersectionRuleImpl('C', right = 'b', above = 'l'),
            IntersectionRuleImpl('D', left = 'b', above = 'r')
        )
        // Assert
        assertDisplay(border.render(rules), listOf("AtttB") + contentLines.map { "l${it}r" } + "CbbbD")
    }


    @Test
    fun testSimpleAscii() {
        // Act
        val border = Border.Builder(content)
            .withAscii()
            .build()

        // Assert
        assertDisplay(border, listOf("+---+") + contentLines.map { "|${it}|" } + "+---+")
    }

    @Test
    fun testUnicodeSingle() {
        // Act
        val border = Border.Builder(content)
            .withUnicodeSingle()
            .build()

        // Assert
        assertDisplay(border, listOf("┌───┐") + contentLines.map { "│${it}│" } + "└───┘")
    }

    @Test
    fun testUnicodeDouble() {
        // Act
        val border = Border.Builder(content)
            .withUnicodeDouble()
            .build()

        // Assert
        assertDisplay(border, listOf("╔═══╗") + contentLines.map { "║${it}║" } + "╚═══╝")
    }

    // region Colors
    @Test
    fun testLeftColoured() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l', Colors(Color.RED))
            .build()

        // Assert
        assertDisplay(border, contentLines.map { "l".red() + "$it" })
    }

    @Test
    fun testRightColoured() {
        // Act
        val border = Border.Builder(content)
            .withRight('r', Colors(Color.BLUE))
            .build()

        // Assert
        assertDisplay(border, contentLines.map { "$it" + "r".blue() })
    }

    @Test
    fun testTopColoured() {
        // Act
        val border = Border.Builder(content)
            .withTop('t', Colors(background = Color.GREEN))
            .build()

        // Assert
        assertDisplay(border, listOf("ttt".greenBackground()) + contentLines)
    }

    @Test
    fun testBottomColoured() {
        // Act
        val border = Border.Builder(content)
            .withBottom('b', Colors(Color.WHITE, Color.BLACK))
            .build()

        // Assert
        assertDisplay(border, contentLines + Kolor.colors("bbb", Color.WHITE, Color.BLACK))
    }

    @Test
    fun testAllWithRulesColoured() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l', Colors(Color.BLUE))
            .withRight('r', Colors(Color.BLUE))
            .withTop('t', Colors(Color.BLUE))
            .withBottom('b', Colors(Color.BLUE))
            .build()
        val rules = listOf(
            IntersectionRuleImpl('A', right = 't', below = 'l'),
            IntersectionRuleImpl('B', left = 't', below = 'r'),
            IntersectionRuleImpl('C', right = 'b', above = 'l'),
            IntersectionRuleImpl('D', left = 'b', above = 'r')
        )
        // Assert
        assertDisplay(
            border.render(rules),
            listOf("AtttB".blue()) + contentLines.map { "l".blue() + it + "r".blue() } + listOf("CbbbD".blue()))
    }


    @Test
    fun testSimpleAsciiColoured() {
        // Act
        val border = Border.Builder(content)
            .withAscii(Colors(Color.BLUE))
            .build()

        // Assert
        assertDisplay(
            border,
            listOf("+---+".blue()) + contentLines.map { "|".blue() + it + "|".blue() } + "+---+".blue())
    }

    @Test
    fun testUnicodeSingleColoured() {
        // Act
        val border = Border.Builder(content)
            .withUnicodeSingle(Colors(Color.BLUE))
            .build()

        // Assert
        assertDisplay(
            border,
            listOf("┌───┐".blue()) + contentLines.map { "│".blue() + it + "│".blue() } + "└───┘".blue())
    }

    @Test
    fun testUnicodeDoubleColoured() {
        // Act
        val border = Border.Builder(content)
            .withUnicodeDouble(Colors(Color.BLUE))
            .build()

        // Assert
        assertDisplay(
            border,
            listOf("╔═══╗".blue()) + contentLines.map { "║".blue() + it + "║".blue() } + "╚═══╝".blue())
    }

    // endregion
}