import atrico.kotlib.konsole.*
import atrico.kotlib.konsole.colors.Colors
import atrico.kotlib.konsole.kolor.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestTable : DisplayElementTestBase() {
    @Test
    fun testEmpty() {
        // Act
        val table = Table()

        // Assert
        assertTable(table, 0, 0)
    }

    @Test
    fun testSetCells() {
        // Act
        val table = Table {
            setCell(0, 0, 'a')
            setCell(1, 1, 'b')
            setCell(2, 2, 'c')
        }

        // Assert
        assertTable(table, 3, 3, "a  ", " b ", "  c")
    }

    @Test
    fun testSetCellsDifferentWidths() {
        // Act
        val table = Table {
            setCell(0, 0, "one")
            setCell(1, 1, '2')
            setCell(2, 2, "three")
        }

        // Assert
        assertTable(table, 3, 3, "one      ", "   2     ", "    three")
    }

    @Test
    fun testSetCellsDifferentHeights() {
        // Act
        val table = Table {
            setCell(0, 0, Tile('1'))
            setCell(1, 1, Tile("t", "w", "o"))
            setCell(2, 2, Tile("t", "h", "r", "e", "e"))
        }

        // Assert
        assertTable(table, 3, 3, "1  ", " t ", " w ", " o ", "  t", "  h", "  r", "  e", "  e")
    }

    @Test
    fun testAppendRow() {
        // Act
        val table = Table {
            appendRow('a', 'b', 'c')
            appendRow('a', Tile('U', 'D'))
            appendRow('a', "LR", 'c', 'd')
            appendRow(null, 'b', null, 'd')
        }

        // Assert
        assertTable(table, 4, 4, "ab c ", "aU   ", " D   ", "aLRcd", " b  d")
    }

    @Test
    fun testHorizontalSeparator() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator('h')
        }

        // Assert
        assertTable(table, 3, 3, "123", "hhh", "456", "hhh", "789")
    }

    @Test
    fun testVerticalSeparator() {
        // Act
        val table = create3x3Table {
            withVerticalSeparator('v')
        }

        // Assert
        assertTable(table, 3, 3, "1v2v3", "4v5v6", "7v8v9")
    }

    @Test
    fun testBothSeparatorsWithRules() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator('h')
            withVerticalSeparator('v')
        }
        val rules = listOf(IntersectionRuleImpl('X', 'h', 'h', 'v', 'v'))

        // Assert
        assertTable(table, rules, 3, 3, "1v2v3", "hXhXh", "4v5v6", "hXhXh", "7v8v9")
    }

    @Test
    fun testAsciiSeparators() {
        // Act
        val table = create3x3Table {
            withSeparatorsAscii()
        }

        // Assert
        assertTable(table, 3, 3, "1|2|3", "-+-+-", "4|5|6", "-+-+-", "7|8|9")
    }

    @Test
    fun testUnicodeSeparatorsSingle() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeSingle()
        }

        // Assert
        assertTable(table, 3, 3, "1│2│3", "─┼─┼─", "4│5│6", "─┼─┼─", "7│8│9")
    }

    @Test
    fun testUnicodeSeparatorsDouble() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeDouble()
        }

        // Assert
        assertTable(table, 3, 3, "1║2║3", "═╬═╬═", "4║5║6", "═╬═╬═", "7║8║9")
    }

    @Test
    fun testUnicodeSeparatorsSingleDouble() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalSingle)
            withVerticalSeparator(Separator.unicodeVerticalDouble)
        }

        // Assert
        assertTable(table, 3, 3, "1║2║3", "─╫─╫─", "4║5║6", "─╫─╫─", "7║8║9")
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingle() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalDouble)
            withVerticalSeparator(Separator.unicodeVerticalSingle)
        }

        // Assert
        assertTable(table, 3, 3, "1│2│3", "═╪═╪═", "4│5│6", "═╪═╪═", "7│8│9")
    }

    // region Colors
    @Test
    fun testSetCellsColoured() {
        // Act
        val table = Table {
            setCell(0, 0, "a".red())
            setCell(1, 1, "b".green())
            setCell(2, 2, "c".blue())
        }

        // Assert
        assertTable(table, 3, 3, "a".red() + "  ", " " + "b".green() + " ", "  " + "c".blue())
    }

    @Test
    fun testAppendRowColoured() {
        // Act
        val table = Table {
            appendRow('a', 'b', 'c')
            appendRow('a', Tile('U', "D".red()))
            appendRow('a', "LR".green(), 'c', 'd')
            appendRow(null, 'b', null, "d".blueBackground())
        }

        // Assert
        assertTable(
            table,
            4,
            4,
            "ab c ",
            "aU   ",
            " " + "D".red() + "   ",
            "a" + "LR".green() + "cd",
            " b  " + "d".blueBackground()
        )
    }

    @Test
    fun testBothSeparatorsWithRulesColoured() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator('h', Colors(Color.BLUE))
            withVerticalSeparator('v', Colors(Color.BLUE))
        }
        val rules = listOf(IntersectionRuleImpl('X', 'h', 'h', 'v', 'v'))

        // Assert
        val v = "v".blue()
        assertTable(
            table,
            rules,
            3,
            3,
            "1" + v + "2" + v + "3",
            "hXhXh".blue(),
            "4" + v + "5" + v + "6",
            "hXhXh".blue(),
            "7" + v + "8" + v + "9"
        )
    }

    @Test
    fun testAsciiSeparatorsColoured() {
        // Act
        val table = create3x3Table {
            withSeparatorsAscii(Colors(Color.GREEN))
        }

        // Assert
        val l = "|".green()
        assertTable(
            table,
            3,
            3,
            "1" + l + "2" + l + "3",
            "-+-+-".green(),
            "4" + l + "5" + l + "6",
            "-+-+-".green(),
            "7" + l + "8" + l + "9"
        )
    }

    @Test
    fun testUnicodeSeparatorsSingleColoured() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeSingle(Colors(background = Color.CYAN))
        }

        // Assert
        val l = "│".cyanBackground()
        assertTable(
            table,
            3,
            3,
            "1" + l + "2" + l + "3",
            "─┼─┼─".cyanBackground(),
            "4" + l + "5" + l + "6",
            "─┼─┼─".cyanBackground(),
            "7" + l + "8" + l + "9"
        )
    }

    @Test
    fun testUnicodeSeparatorsDoubleColoured() {
        // Act
        val table = create3x3Table {
            withSeparatorsUnicodeDouble(Colors(Color.LIGHT_BLUE))
        }

        // Assert
        val l = "║".lightBlue()
        assertTable(
            table,
            3,
            3,
            "1" + l + "2" + l + "3",
            "═╬═╬═".lightBlue(),
            "4" + l + "5" + l + "6",
            "═╬═╬═".lightBlue(),
            "7" + l + "8" + l + "9"
        )
    }

    @Test
    fun testUnicodeSeparatorsSingleDoubleColoured() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalSingle, Colors(Color.BLUE))
            withVerticalSeparator(Separator.unicodeVerticalDouble, Colors(Color.BLUE))
        }

        // Assert
        val l = "║".blue()
        assertTable(
            table,
            3,
            3,
            "1" + l + "2" + l + "3",
            "─╫─╫─".blue(),
            "4" + l + "5" + l + "6",
            "─╫─╫─".blue(),
            "7" + l + "8" + l + "9"
        )
    }

    @Test
    fun testUnicodeSeparatorsDoubleSingleColoured() {
        // Act
        val table = create3x3Table {
            withHorizontalSeparator(Separator.unicodeHorizontalDouble, Colors(Color.YELLOW))
            withVerticalSeparator(Separator.unicodeVerticalSingle, Colors(Color.YELLOW))
        }

        // Assert
        val l = "│".yellow()
        assertTable(
            table,
            3,
            3,
            "1" + l + "2" + l + "3",
            "═╪═╪═".yellow(),
            "4" + l + "5" + l + "6",
            "═╪═╪═".yellow(),
            "7" + l + "8" + l + "9"
        )
    }

    // endregion
    private fun assertTable(table: Table, rows: Int, columns: Int, vararg lines: String) =
        assertTable(table, rows, columns, lines.asIterable())

    private fun assertTable(table: Table, rows: Int, columns: Int, lines: Iterable<String>) {
        assertThat("Rows", table.rows, equalTo(rows))
        assertThat("Columns", table.columns, equalTo(columns))
        assertDisplay(table, lines)
    }

    private fun assertTable(
        table: Table,
        intersectionRules: Iterable<IntersectionRule>,
        rows: Int,
        columns: Int,
        vararg lines: String
    ) {
        assertThat("Rows", table.rows, equalTo(rows))
        assertThat("Columns", table.columns, equalTo(columns))
        assertDisplay(table.render(intersectionRules), lines.asIterable())
    }
}

internal fun create3x3Table(extra: Table.Builder.() -> Unit) = Table {
    appendRow(1, 2, 3)
    appendRow(4, 5, 6)
    appendRow(7, 8, 9)
    extra()
}
