import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.Tile
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
        assertDisplay(border, listOf("ttttt") + contentLines.map { "l${it}r" } + "bbbbb")
    }

    @Test
    fun testAllWithRules() {
        // Act
        val border = Border.Builder(content)
            .withLeft('l')
            .withRight('r')
            .withTop('t')
            .withBottom('b')
            .withIntersectRule('A', right = 't', below = 'l')
            .withIntersectRule('B', left = 't', below = 'r')
            .withIntersectRule('C', right = 'b', above = 'l')
            .withIntersectRule('D', left = 'b', above = 'r')
            .build()

        // Assert
        assertDisplay(border, listOf("AtttB") + contentLines.map { "l${it}r" } + "CbbbD")
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
}