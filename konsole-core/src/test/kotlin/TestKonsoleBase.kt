import atrico.kotlib.konsole.core.Color
import atrico.kotlib.testing.TestBase

abstract class TestKonsoleBase : TestBase() {
    private val colorCodes = Color.values().map { it.baseCode }.toIntArray()

    /**
     * Get a random color
     */
    protected val randomColor get() = Color.fromValue(colorCodes[random.nextInt(colorCodes.size)])!!
}