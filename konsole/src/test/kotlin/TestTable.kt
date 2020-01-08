import atrico.kotlib.konsole.Table
import atrico.kotlib.konsole.Tile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestTable : DisplayElementTestBase() {
    @Test
    fun testEmpty() {
        // Act
        val table = Table.Builder()
            .build()

        // Assert
        assertTable(table, 0, 0)
    }

    @Test
    fun testSetCells() {
        // Act
        val table = Table.Builder()
            .setCell(0, 0, 'a')
            .setCell(1, 1, 'b')
            .setCell(2, 2, 'c')
            .build()

        // Assert
        assertTable(table, 3, 3, "a  ", " b ", "  c")
    }

    @Test
    fun testSetCellsDifferentWidths() {
        // Act
        val table = Table.Builder()
            .setCell(0, 0, "one")
            .setCell(1, 1, '2')
            .setCell(2, 2, "three")
            .build()

        // Assert
        assertTable(table, 3, 3, "one      ", "   2     ", "    three")
    }

    @Test
    fun testSetCellsDifferentHeights() {
        // Act
        val table = Table.Builder()
            .setCell(0, 0, Tile('1'))
            .setCell(1, 1, Tile("t", "w", "o"))
            .setCell(2, 2, Tile("t", "h", "r", "e", "e"))
            .build()

        // Assert
        assertTable(table, 3, 3, "1  ", " t ", " w ", " o ", "  t", "  h", "  r", "  e", "  e")
    }

    @Test
    fun testAppendRow() {
        // Act
        val table = Table.Builder()
            .appendRow('a', 'b', 'c')
            .appendRow('a', Tile('U', 'D'))
            .appendRow('a', "LR", 'c', 'd')
            .appendRow(null, 'b', null, 'd')
            .build()

        // Assert
        assertTable(table, 4, 4, "ab c ", "aU   ", " D   ", "aLRcd", " b  d")
    }

    @Test
    fun testHorizontalSeparator() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator('h')
            .build()

        // Assert
        assertTable(table, 3, 3, "123", "hhh", "456", "hhh", "789")
    }

    @Test
    fun testVerticalSeparator() {
        // Act
        val table = create3x3Table()
            .withVerticalSeparator('v')
            .build()

        // Assert
        assertTable(table, 3, 3, "1v2v3", "4v5v6", "7v8v9")
    }

    @Test
    fun testBothSeparatorsWithRules() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparator('h')
            .withVerticalSeparator('v')
            .withIntersectRule('X', 'h', 'h', 'v', 'v')
            .build()

        // Assert
        assertTable(table, 3, 3, "1v2v3", "hXhXh", "4v5v6", "hXhXh", "7v8v9")
    }

    @Test
    fun testAsciiSeparators() {
        // Act
        val table = create3x3Table()
            .withSeparatorsAscii()
            .build()

        // Assert
        assertTable(table, 3, 3, "1|2|3", "-+-+-", "4|5|6", "-+-+-", "7|8|9")
    }

    @Test
    fun testUnicodeSeparatorsSingle() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeSingle()
            .build()

        // Assert
        assertTable(table, 3, 3, "1│2│3", "─┼─┼─", "4│5│6", "─┼─┼─", "7│8│9")
    }

    @Test
    fun testUnicodeSeparatorsDouble() {
        // Act
        val table = create3x3Table()
            .withSeparatorsUnicodeDouble()
            .build()

        // Assert
        assertTable(table, 3, 3, "1║2║3", "═╬═╬═", "4║5║6", "═╬═╬═", "7║8║9")
    }

    @Test
    fun testUnicodeSeparatorsSingleDouble() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparatorUnicodeSingle()
            .withVerticalSeparatorUnicodeDouble()
            .build()

        // Assert
        assertTable(table, 3, 3, "1║2║3", "─╫─╫─", "4║5║6", "─╫─╫─", "7║8║9")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingle() {
        // Act
        val table = create3x3Table()
            .withHorizontalSeparatorUnicodeDouble()
            .withVerticalSeparatorUnicodeSingle()
            .build()

        // Assert
        assertTable(table, 3, 3, "1│2│3", "═╪═╪═", "4│5│6", "═╪═╪═", "7│8│9")
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