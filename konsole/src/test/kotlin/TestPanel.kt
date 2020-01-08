import atrico.kotlib.konsole.Panel
import atrico.kotlib.konsole.Pos
import atrico.kotlib.konsole.Tile
import atrico.kotlib.konsole.withOffset
import org.junit.jupiter.api.Test

class TestPanel : DisplayElementTestBase() {
    @Test
    fun testNoChildren() {
        // Arrange

        // Act
        val panel = Panel()

        // Assert
        assertDisplay(panel)
    }

    @Test
    fun testSingleChild() {
        // Arrange
        var content = Tile("abc", "123", "DEF")

        // Act
        val panel = Panel(content)

        // Assert
        assertDisplay(panel, "abc", "123", "DEF")
    }

    @Test
    fun testOverlay() {
        // Arrange
        var content1 = Tile("abc", "123", "DEF")
        var content2 = Tile("x", "y", "z")

        // Act
        val panel = Panel(content1, content2)

        // Assert
        assertDisplay(panel, "xbc", "y23", "zEF")
    }

    @Test
    fun testOverlayOrder() {
        // Arrange
        var content1 = Tile("abc")
        var content2 = Tile("x")
        var content3 = Tile("y")
        var content4 = Tile("z")

        // Act
        val panel = Panel(content1, content2, content3, content4)

        // Assert
        assertDisplay(panel, "zbc")
    }

    @Test
    fun testOverlayWithOffset() {
        // Arrange
        var content1 = Tile("abc", "123", "DEF")
        var content2 = Tile("x")
        var content3 = Tile("y")
        var content4 = Tile("z")

        // Act
        val panel = Panel(
            content1.withOffset(Pos.ORIGIN),
            content2.withOffset(Pos.ORIGIN),
            content3.withOffset(Pos.downRight(1)),
            content4.withOffset(Pos.downRight(2))
        )

        // Assert
        assertDisplay(panel, "xbc", "1y3", "DEz")
    }

    @Test
    fun testOverlayWithNegativeOffset() {
        // Arrange
        var content1 = Tile("abc", "123", "DEF")
        var content2 = Tile("x")
        var content3 = Tile("y")
        var content4 = Tile("z")

        // Act
        val panel = Panel(
            content1.withOffset(Pos.ORIGIN),
            content2.withOffset(Pos.ORIGIN),
            content3.withOffset(Pos.upLeft(1)),
            content4.withOffset(Pos.upLeft(2))
        )

        // Assert
        assertDisplay(panel, "z    ", " y   ", "  xbc", "  123", "  DEF")
    }
}