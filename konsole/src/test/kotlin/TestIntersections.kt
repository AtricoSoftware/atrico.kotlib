import atrico.kotlib.konsole.Border
import atrico.kotlib.konsole.Separator
import atrico.kotlib.konsole.Table
import atrico.kotlib.konsole.Tile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestIntersections : DisplayElementTestBase() {
    @Test
    fun testAsciiSeparatorsBorder() {
        // Act
        val table = create3x3Table()
            .withSeparatorsAscii()
            .build()
        val border = Border.Builder(table)
            .withAscii()
            .build()

        // Assert
        assertDisplay(border, "+-+-+-+", "|1|2|3|", "+-+-+-+", "|4|5|6|", "+-+-+-+", "|7|8|9|", "+-+-+-+")
    }

    @Test
    fun testUnicodeSeparatorsSingleBorderSingle() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeSingle()
            .build()
        val border = Border.Builder(table)
            .withUnicodeSingle()
            .build()

        // Assert
        assertDisplay(border, "┌─┬─┬─┐", "│1│2│3│", "├─┼─┼─┤", "│4│5│6│", "├─┼─┼─┤", "│7│8│9│", "└─┴─┴─┘")
    }

    @Test
    fun testUnicodeSeparatorsDoubleBorderSingle() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeDouble()
            .build()
        val border = Border.Builder(table)
            .withUnicodeSingle()
            .build()

        // Assert
        assertDisplay(border, "┌─╥─╥─┐", "│1║2║3│", "╞═╬═╬═╡", "│4║5║6│", "╞═╬═╬═╡", "│7║8║9│", "└─╨─╨─┘")
    }

    @Test
    fun testUnicodeSeparatorsSingleDoubleBorderSingle() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator(Separator.unicodeHorizontalSingle)
            .withVerticalSeparator(Separator.unicodeVerticalDouble)
            .build()
        val border = Border.Builder(table)
            .withUnicodeSingle()
            .build()

        // Assert
        assertDisplay(border, "┌─╥─╥─┐", "│1║2║3│", "├─╫─╫─┤", "│4║5║6│", "├─╫─╫─┤", "│7║8║9│", "└─╨─╨─┘")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingleBorderSingle() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator(Separator.unicodeHorizontalDouble)
            .withVerticalSeparator(Separator.unicodeVerticalSingle)
            .build()
        val border = Border.Builder(table)
            .withUnicodeSingle()
            .build()

        // Assert
        assertDisplay(border, "┌─┬─┬─┐", "│1│2│3│", "╞═╪═╪═╡", "│4│5│6│", "╞═╪═╪═╡", "│7│8│9│", "└─┴─┴─┘")
    }

    @Test
    fun testUnicodeSeparatorsSingleBorderDouble() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeSingle()
            .build()
        val border = Border.Builder(table)
            .withUnicodeDouble()
            .build()

        // Assert
        assertDisplay(border, "╔═╤═╤═╗", "║1│2│3║", "╟─┼─┼─╢", "║4│5│6║", "╟─┼─┼─╢", "║7│8│9║", "╚═╧═╧═╝")
    }

    @Test
    fun testUnicodeSeparatorsDoubleBorderDouble() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeDouble()
            .build()
        val border = Border.Builder(table)
            .withUnicodeDouble()
            .build()

        // Assert
        assertDisplay(border, "╔═╦═╦═╗", "║1║2║3║", "╠═╬═╬═╣", "║4║5║6║", "╠═╬═╬═╣", "║7║8║9║", "╚═╩═╩═╝")
    }

    @Test
    fun testUnicodeSeparatorsSingleDoubleBorderDouble() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator(Separator.unicodeHorizontalSingle)
            .withVerticalSeparator(Separator.unicodeVerticalDouble)
            .build()
        val border = Border.Builder(table)
            .withUnicodeDouble()
            .build()

        // Assert
        assertDisplay(border, "╔═╦═╦═╗", "║1║2║3║", "╟─╫─╫─╢", "║4║5║6║", "╟─╫─╫─╢", "║7║8║9║", "╚═╩═╩═╝")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingleBorderDouble() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator(Separator.unicodeHorizontalDouble)
            .withVerticalSeparator(Separator.unicodeVerticalSingle)
            .build()
        val border = Border.Builder(table)
            .withUnicodeDouble()
            .build()

        // Assert
        assertDisplay(border, "╔═╤═╤═╗", "║1│2│3║", "╠═╪═╪═╣", "║4│5│6║", "╠═╪═╪═╣", "║7│8│9║", "╚═╧═╧═╝")
    }

    @Test
    fun testCornersSingleDouble() {
        // Act
        val table = create3x3Table()
            .build()
        val border = Border.Builder(table)
            .withTop(Separator.unicodeHorizontalSingle)
            .withBottom(Separator.unicodeHorizontalSingle)
            .withLeft(Separator.unicodeVerticalDouble)
            .withRight(Separator.unicodeVerticalDouble)
            .build()

        // Assert
        assertDisplay(border, "╓───╖", "║123║", "║456║", "║789║", "╙───╜")
    }

    @Test
    fun testCornersDoubleSingle() {
        // Act
        val table = create3x3Table()
            .build()
        val border = Border.Builder(table)
            .withTop(Separator.unicodeHorizontalDouble)
            .withBottom(Separator.unicodeHorizontalDouble)
            .withLeft(Separator.unicodeVerticalSingle)
            .withRight(Separator.unicodeVerticalSingle)
            .build()

        // Assert
        assertDisplay(border, "╒═══╕", "│123│", "│456│", "│789│", "╘═══╛")
    }

    @Test
    fun testDeepTree() {
        // Act
        val tile01 = Tile("A")
        val border11 = Border.Builder(tile01)
            .withUnicodeSingle()
            .build()
        val tile02 = Tile("B")
        val border12 = Border.Builder(tile02)
            .withRight(Separator.unicodeVerticalSingle)
            .withTop(Separator.unicodeHorizontalSingle)
            .withBottom(Separator.unicodeHorizontalSingle)
            .build()
        val table2 = Table.Builder()
            .setCell(0, 0, border11)
            .setCell(1, 0, border12)
            .build()

        // Assert
        assertDisplay(table2, "┌─┬─┐", "│A│B│", "└─┴─┘")
    }


    private fun assertTable(table: Table, rows: Int, columns: Int, vararg lines: String) =
        assertTable(table, rows, columns, lines.asIterable())

    private fun assertTable(table: Table, rows: Int, columns: Int, lines: Iterable<String>) {
        assertThat("Rows", table.rows, equalTo(rows))
        assertThat("Columns", table.columns, equalTo(columns))
        assertDisplay(table, lines)
    }

    private fun create3x3Table() =
        Table.Builder()
            .appendRow(1, 2, 3)
            .appendRow(4, 5, 6)
            .appendRow(7, 8, 9)

}