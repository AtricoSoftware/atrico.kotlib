import atrico.kotlib.konsole.Canvas
import atrico.kotlib.konsole.Pos
import atrico.kotlib.konsole.colors.Colors
import atrico.kotlib.konsole.kolor.*
import org.junit.jupiter.api.Test

class TestCanvas : DisplayElementTestBase() {
    @Test
    fun testBlank() {
        // Arrange

        // Act
        val canvas = Canvas.blank

        // Assert
        assertDisplay(canvas)
    }

    @Test
    fun testSetCell() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas.setCell(Pos.ORIGIN, 'A')
        // Assert
        assertDisplay(canvas, "A")
    }

    @Test
    fun testSetCells() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas
            .setCell(0, 0, 'A')
            .setCell(1, 1, 'B')
            .setCell(2, 2, 'C')

        // Assert
        assertDisplay(canvas, "A  ", " B ", "  C")
    }

    @Test
    fun testSetString() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas
            .setString(0, 0, "ABCDE")
            .setString(2, 0, "abcde")
            .setString(4, 0, "12345")

        // Assert
        assertDisplay(canvas, "ABab12345")
    }

    // region Colors

    @Test
    fun testSetCellColoured() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas.setCell(Pos.ORIGIN, 'A', Colors(Color.WHITE, Color.BLUE))

        // Assert
        assertDisplay(canvas, Kolor.colors("A", Color.WHITE, Color.BLUE))
    }

    @Test
    fun testSetCellsColoured() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas
            .setCell(0, 0, 'A', Colors(Color.RED))
            .setCell(1, 1, 'B', Colors(Color.GREEN))
            .setCell(2, 2, 'C', Colors(background = Color.BLUE))

        // Assert
        assertDisplay(canvas, "A".red() + "  ", " " + "B".green() + " ", "  " + "C".blueBackground())
    }

    @Test
    fun testSetStringColoured() {
        // Arrange
        val canvas = Canvas.blank

        // Act
        canvas
            .setString(0, 0, "ABCDE".red())
            .setString(2, 0, "abcde".greenBackground())
            .setString(4, 0, "12345".blue())

        // Assert
        assertDisplay(canvas, "AB".red() + "ab".greenBackground() + "12345".blue())
    }

    // endregion
}