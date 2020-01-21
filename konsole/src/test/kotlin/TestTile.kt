import atrico.kotlib.konsole.Tile
import atrico.kotlib.konsole.color.blue
import atrico.kotlib.konsole.color.greenBackground
import atrico.kotlib.konsole.color.red
import atrico.kotlib.konsole.color.stripColors
import org.junit.jupiter.api.Test

class TestTile : DisplayElementTestBase() {
    @Test
    fun testCreateFromStrings() {
        // Arrange
        val strings = listOf("1", "123", "12")

        // Act
        val tile = Tile(strings)

        // Assert
        assertDisplay(tile, strings.map { it.padEnd(3) })
    }

    @Test
    fun testCreateFromColouredStrings() {
        // Arrange
        val strings = listOf("1".red(), "123".greenBackground(), "12".blue())

        // Act
        val tile = Tile(strings)

        // Assert
        assertDisplay(tile, strings.map { it + " ".repeat(3 - it.stripColors().length) })
    }
}
