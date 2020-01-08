import atrico.kotlib.konsole.Tile
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
}