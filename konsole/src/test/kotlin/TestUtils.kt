import atrico.kotlib.konsole.Pos
import atrico.kotlib.konsole.getExtent
import atrico.kotlib.testing.TestBase
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class TestUtils : TestBase() {
    @Test
    fun testExtentEmpty() {
        // Arrange
        val positions = emptyList<Pos>()

        // Act
        val extent = getExtent(positions)

        // Assert
        assertThat("Top Left", extent.topLeft, equalTo(Pos.ORIGIN))
        assertThat("Width", extent.width, equalTo(0))
        assertThat("Height", extent.height, equalTo(0))
    }
}