import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.Separator
import atrico.kotlib.konsole.Table
import atrico.kotlib.konsole.Tile
import org.junit.jupiter.api.Test

class TestIntersections : DisplayElementTestBase() {
    @Test
    fun testAsciiSeparatorsBorder() {
        // Act
        val table = create3x3Table {
            withSeparatorsAscii()
        }
        val border = Border(table) {
            withAscii()
        }

        // Assert
        assertDisplay(border, "+-+-+-+", "|1|2|3|", "+-+-+-+", "|4|5|6|", "+-+-+-+", "|7|8|9|", "+-+-+-+")
    }

    @Test
    fun testUnicodeSeparatorsSingleBorderSingle() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeSingle()
        }
        val border = Border(table) {
            withUnicodeSingle()
        }

        // Assert
        assertDisplay(border, "┌─┬─┬─┐", "│1│2│3│", "├─┼─┼─┤", "│4│5│6│", "├─┼─┼─┤", "│7│8│9│", "└─┴─┴─┘")
    }

    @Test
    fun testUnicodeSeparatorsDoubleBorderSingle() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeDouble()
        }
        val border = Border(table) {
            withUnicodeSingle()
        }

        // Assert
        assertDisplay(border, "┌─╥─╥─┐", "│1║2║3│", "╞═╬═╬═╡", "│4║5║6│", "╞═╬═╬═╡", "│7║8║9│", "└─╨─╨─┘")
    }

    @Test
    fun testUnicodeSeparatorsSingleDoubleBorderSingle() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalSingle)
            withVerticalSeparator(Separator.unicodeVerticalDouble)
        }
        val border = Border(table) {
            withUnicodeSingle()
        }

        // Assert
        assertDisplay(border, "┌─╥─╥─┐", "│1║2║3│", "├─╫─╫─┤", "│4║5║6│", "├─╫─╫─┤", "│7║8║9│", "└─╨─╨─┘")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingleBorderSingle() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalDouble)
            withVerticalSeparator(Separator.unicodeVerticalSingle)
        }
        val border = Border(table) {
            withUnicodeSingle()
        }

        // Assert
        assertDisplay(border, "┌─┬─┬─┐", "│1│2│3│", "╞═╪═╪═╡", "│4│5│6│", "╞═╪═╪═╡", "│7│8│9│", "└─┴─┴─┘")
    }

    @Test
    fun testUnicodeSeparatorsSingleBorderDouble() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeSingle()
        }
        val border = Border(table) {
            withUnicodeDouble()
        }

        // Assert
        assertDisplay(border, "╔═╤═╤═╗", "║1│2│3║", "╟─┼─┼─╢", "║4│5│6║", "╟─┼─┼─╢", "║7│8│9║", "╚═╧═╧═╝")
    }

    @Test
    fun testUnicodeSeparatorsDoubleBorderDouble() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeDouble()
        }
        val border = Border(table) {
            withUnicodeDouble()
        }

        // Assert
        assertDisplay(border, "╔═╦═╦═╗", "║1║2║3║", "╠═╬═╬═╣", "║4║5║6║", "╠═╬═╬═╣", "║7║8║9║", "╚═╩═╩═╝")
    }

    @Test
    fun testUnicodeSeparatorsSingleDoubleBorderDouble() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalSingle)
            withVerticalSeparator(Separator.unicodeVerticalDouble)
        }
        val border = Border(table) {
            withUnicodeDouble()
        }

        // Assert
        assertDisplay(border, "╔═╦═╦═╗", "║1║2║3║", "╟─╫─╫─╢", "║4║5║6║", "╟─╫─╫─╢", "║7║8║9║", "╚═╩═╩═╝")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingleBorderDouble() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalDouble)
            withVerticalSeparator(Separator.unicodeVerticalSingle)
        }
        val border = Border(table) {
            withUnicodeDouble()
        }

        // Assert
        assertDisplay(border, "╔═╤═╤═╗", "║1│2│3║", "╠═╪═╪═╣", "║4│5│6║", "╠═╪═╪═╣", "║7│8│9║", "╚═╧═╧═╝")
    }

    @Test
    fun testCornersSingleDouble() {
        // Act
        val table = create3x3Table {}
        val border = Border(table) {
            withTop(Separator.unicodeHorizontalSingle)
            withBottom(Separator.unicodeHorizontalSingle)
            withLeft(Separator.unicodeVerticalDouble)
            withRight(Separator.unicodeVerticalDouble)
        }

        // Assert
        assertDisplay(border, "╓───╖", "║123║", "║456║", "║789║", "╙───╜")
    }

    @Test
    fun testCornersDoubleSingle() {
        // Act
        val table = create3x3Table {}
        val border = Border(table) {
            withTop(Separator.unicodeHorizontalDouble)
            withBottom(Separator.unicodeHorizontalDouble)
            withLeft(Separator.unicodeVerticalSingle)
            withRight(Separator.unicodeVerticalSingle)
        }

        // Assert
        assertDisplay(border, "╒═══╕", "│123│", "│456│", "│789│", "╘═══╛")
    }

    @Test
    fun testDeepTree() {
        // Act
        val tile01 = Tile("A")
        val border11 = Border(tile01) {
            withUnicodeSingle()
        }
        val tile02 = Tile("B")
        val border12 = Border(tile02) {
            withRight(Separator.unicodeVerticalSingle)
            withTop(Separator.unicodeHorizontalSingle)
            withBottom(Separator.unicodeHorizontalSingle)
        }
        val table2 = Table {
            setCell(0, 0, border11)
            setCell(1, 0, border12)
        }

        // Assert
        assertDisplay(table2, "┌─┬─┐", "│A│B│", "└─┴─┘")
    }
}