import atrico.kotlib.konsole.*
import atrico.kotlib.testing.TestBase
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestPos : TestBase() {
    // region Construction
    @Test
    fun testOrigin() {
        // Act
        val pos = Pos.ORIGIN

        // Assert
        assertThat("x", pos.x, equalTo(0))
        assertThat("y", pos.y, equalTo(0))
    }

    @Test
    fun testCtor() {
        // Arrange
        val x = randomValue()
        val y = randomValue()

        // Act
        val pos = Pos(x, y)

        // Assert
        assertThat("x", pos.x, equalTo(x))
        assertThat("y", pos.y, equalTo(y))
    }
    // endregion Construction

    // region Arithmetic

    @Test
    fun testPlus() {
        // Arrange
        val x1 = randomValue()
        val y1 = randomValue()
        val x2 = randomValue()
        val y2 = randomValue()
        val pos1 = Pos(x1, y1)
        val pos2 = Pos(x2, y2)

        // Act
        val result = pos1 + pos2
        println("$pos1 + $pos2 = $result")

        // Assert
        assertThat("x", result.x, equalTo(x1 + x2))
        assertThat("y", result.y, equalTo(y1 + y2))
    }

    @Test
    fun testMinus() {
        // Arrange
        val x1 = randomValue()
        val y1 = randomValue()
        val x2 = randomValue()
        val y2 = randomValue()
        val pos1 = Pos(x1, y1)
        val pos2 = Pos(x2, y2)

        // Act
        val result = pos1 - pos2
        println("$pos1 - $pos2 = $result")

        // Assert
        assertThat("x", result.x, equalTo(x1 - x2))
        assertThat("y", result.y, equalTo(y1 - y2))
    }

    @Test
    fun testTimes() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val scalar = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos * scalar
        println("$pos * $scalar = $result")

        // Assert
        assertThat("x", result.x, equalTo(x * scalar))
        assertThat("y", result.y, equalTo(y * scalar))
    }
    // endregion Arithmetic

    // region Displacement

    @Test
    fun testUp() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.up(delta)
        println("$pos Up($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x))
        assertThat("y", result.y, equalTo(y - delta))
    }

    @Test
    fun testDown() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.down(delta)
        println("$pos Down($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x))
        assertThat("y", result.y, equalTo(y + delta))
    }

    @Test
    fun testLeft() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.left(delta)
        println("$pos Left($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x - delta))
        assertThat("y", result.y, equalTo(y))
    }

    @Test
    fun testRight() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.right(delta)
        println("$pos Right($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x + delta))
        assertThat("y", result.y, equalTo(y))
    }

    @Test
    fun testUpLeft() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.upLeft(delta)
        println("$pos UpLeft($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x - delta))
        assertThat("y", result.y, equalTo(y - delta))
    }

    @Test
    fun testUpRight() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.upRight(delta)
        println("$pos UpRight($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x + delta))
        assertThat("y", result.y, equalTo(y - delta))
    }

    @Test
    fun testDownLeft() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.downLeft(delta)
        println("$pos DownLeft($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x - delta))
        assertThat("y", result.y, equalTo(y + delta))
    }

    @Test
    fun testDownRight() {
        // Arrange
        val x = randomValue()
        val y = randomValue()
        val delta = randomValue()
        val pos = Pos(x, y)

        // Act
        val result = pos.downRight(delta)
        println("$pos DownRight($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(x + delta))
        assertThat("y", result.y, equalTo(y + delta))
    }
    // endregion Displacement

    // region Directions

    @Test
    fun testUpDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.up(delta)
        println("Up($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(0))
        assertThat("y", result.y, equalTo(-delta))
    }

    @Test
    fun testDownDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.down(delta)
        println("Down($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(0))
        assertThat("y", result.y, equalTo(delta))
    }

    @Test
    fun testLeftDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.left(delta)
        println("Left($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(-delta))
        assertThat("y", result.y, equalTo(0))
    }

    @Test
    fun testRightDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.right(delta)
        println("Right($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(delta))
        assertThat("y", result.y, equalTo(0))
    }

    @Test
    fun testUpLeftDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.upLeft(delta)
        println("UpLeft($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(-delta))
        assertThat("y", result.y, equalTo(-delta))
    }

    @Test
    fun testUpRightDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.upRight(delta)
        println("UpRight($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(delta))
        assertThat("y", result.y, equalTo(-delta))
    }

    @Test
    fun testDownLeftDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.downLeft(delta)
        println("DownLeft($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(-delta))
        assertThat("y", result.y, equalTo(delta))
    }

    @Test
    fun testDownRightDir() {
        // Arrange
        val delta = randomValue()

        // Act
        val result = Pos.downRight(delta)
        println("DownRight($delta) = $result")

        // Assert
        assertThat("x", result.x, equalTo(delta))
        assertThat("y", result.y, equalTo(delta))
    }
    // endregion Directions

    private fun randomValue() = random.nextInt(-5, 5)
}