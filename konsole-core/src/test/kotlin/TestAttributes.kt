import atrico.kotlib.konsole.core.Attribute
import atrico.kotlib.konsole.core.Color
import atrico.kotlib.konsole.core.Konsole
import atrico.kotlib.konsole.core.stripAttributes
import atrico.kotlib.testing.containsInAnyOrder
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class TestAttributes : TestKonsoleBase() {
    @Test
    fun testNullAttribute() {
        // Arrange
        val attr = Attribute.none

        // Act
        val ansi = attr.ansiString
        val toString = attr.toString()

        // Assert
        assertThat("ansi", ansi, equalTo(""))
        assertThat("toString", toString, equalTo(""))
    }

    @TestFactory
    fun testAnsiStrings() = ansiCodes.map { (attribute, ansi) ->
        DynamicTest.dynamicTest("Attribute $attribute produces ANSI string: ${ansi.substring(1)}") {
            println("$attribute <-> ${ansi.substring(1)}")
            // Act
            val result = attribute.ansiString
            println(result.substring(1))
            val parsed = Attribute.parse(result)
            println(parsed)

            // Assert
            assertAnsi("ANSI", result, ansi)
            assertThat("Parsed", parsed, equalTo(attribute))
        }
    }

    @TestFactory
    fun testCombined() = combinationAnsiCodes.map { (name, attributes, expected) ->
        DynamicTest.dynamicTest(
            "$name: Combined attributes ${attributes.joinToString(",")} produces ANSI string: ${expected.substring(
                1
            )}"
        ) {
            println("${attributes.joinToString(",")} <-> ${expected.substring(1)}")
            // Arrange
            // Act
            val result = attributes.fold(attributes.first()) { acc, attr -> acc + attr }
            println(result)
            val parsed = Attribute.parse(result.ansiString)
            println(parsed)

            // Assert
            assertAnsi("result", result.ansiString, expected)
            assertThat("Parsed", parsed, equalTo(result))
        }
    }

    @TestFactory
    fun testCombine() = combinationAnsiCodes.map { (name, attributes, _) ->
        DynamicTest.dynamicTest("$name: Combined attributes ${attributes.joinToString(",")}") {
            println(attributes.joinToString(","))
            // Arrange
            val expected = attributes.fold(Attribute.none) { acc, attr -> acc + attr }
            // Act
            val result = Attribute.combine(attributes)
            println(result)

            // Assert
            assertThat("result", result, equalTo(expected))
        }
    }

    @Test
    fun testParseInvalid() {
        // Arrange
        val text = randomString()

        // Act
        val result = Attribute.parse(text)

        // Assert
        assertThat("No Attribute", result, absent())
    }

    private fun assertAnsi(msg: String, actual: String, expected: String) {
        val actualPrefix = actual.substring(0, 2)
        val actualSuffix = actual.substring(actual.length - 1)
        val actualValues = actual.substring(2, actual.length - 1).split(';').map { it.toInt() }
        val expectedPrefix = expected.substring(0, 2)
        val expectedSuffix = expected.substring(expected.length - 1)
        val expectedValues = expected.substring(2, expected.length - 1).split(';').map { it.toInt() }
        assertThat("$msg: Prefix", actualPrefix, equalTo(expectedPrefix))
        assertThat("$msg: Suffix", actualSuffix, equalTo(expectedSuffix))
        assertThat("$msg: Values", actualValues, containsInAnyOrder(expectedValues))
    }

    @Test
    fun testStripAttributesNoAttributes() {
        // Arrange
        val text = randomString()

        // Act
        val result = Attribute.stripAttributes(text)
        val resultExtension = text.stripAttributes()

        // Assert
        assertThat("Func", result, equalTo(text))
        assertThat("Extension", resultExtension, equalTo(text))
    }

    @Test
    fun testStripColors() {
        // Arrange
        val text = randomString()
        val withForeground = Konsole.foreground(text, Color.RED)
        val withBackground = Konsole.background(text, Color.BLUE)
        val withBoth = Konsole.color(text, Color.BLACK, Color.WHITE)

        // Act
        val resultForeground = Attribute.stripAttributes(withForeground)
        val resultForegroundExtension = withForeground.stripAttributes()
        val resultBackground = Attribute.stripAttributes(withBackground)
        val resultBackgroundExtension = withBackground.stripAttributes()
        val resultBoth = Attribute.stripAttributes(withBoth)
        val resultBothExtension = withBoth.stripAttributes()

        // Assert
        assertThat("Foreground", resultForeground, equalTo(text))
        assertThat("Foreground Extension", resultForegroundExtension, equalTo(text))
        assertThat("Background", resultBackground, equalTo(text))
        assertThat("Background Extension", resultBackgroundExtension, equalTo(text))
        assertThat("Both", resultBoth, equalTo(text))
        assertThat("Both Extension", resultBothExtension, equalTo(text))
    }

    @TestFactory
    fun testStripCombined() = combinationAnsiCodes
        .map { (name, attributes, _) -> Pair(name, attributes.fold(Attribute.none) { acc, attr -> acc + attr }) }
        .map { (name, attribute) ->
            DynamicTest.dynamicTest("$name: Strip attribute $attribute") {
                println(attribute)
                // Arrange
                val text = randomString()
                var withAttributes = Konsole.withAttributes(text, attribute)

                // Act
                val result = Attribute.stripAttributes(withAttributes)
                val resultExtension = withAttributes.stripAttributes()
                println(result)
                println(resultExtension)

                // Assert
                assertThat("result", result, equalTo(text))
                assertThat("result extension", resultExtension, equalTo(text))
            }
        }

    // region Test cases

    // All attributes
    private val allAttributes = sequence {
        yield(Attribute.Set.bold)
        yield(Attribute.Set.dim)
        yield(Attribute.Set.underline)
        yield(Attribute.Set.blink)
        yield(Attribute.Set.invert)
        yield(Attribute.Set.hidden)
        Color.values().forEach {
            yield(Attribute.Set.foregroundColor(it))
            yield(Attribute.Set.backgroundColor(it))
        }
        yield(Attribute.Reset.bold)
        yield(Attribute.Reset.dim)
        yield(Attribute.Reset.underline)
        yield(Attribute.Reset.blink)
        yield(Attribute.Reset.invert)
        yield(Attribute.Reset.hidden)
        yield(Attribute.Reset.foregroundColor)
        yield(Attribute.Reset.backgroundColor)
    }.toList()

    // Map of attribute to its reset
    private val attributeResets = sequence {
        yield(Attribute.Set.bold to Attribute.Reset.bold)
        yield(Attribute.Set.dim to Attribute.Reset.dim)
        yield(Attribute.Set.underline to Attribute.Reset.underline)
        yield(Attribute.Set.blink to Attribute.Reset.blink)
        yield(Attribute.Set.invert to Attribute.Reset.invert)
        yield(Attribute.Set.hidden to Attribute.Reset.hidden)
        Color.values().forEach {
            yield(Attribute.Set.foregroundColor(it) to Attribute.Reset.foregroundColor)
            yield(Attribute.Set.backgroundColor(it) to Attribute.Reset.backgroundColor)
        }
    }.toMap()

    // Map of attribute to its overrides
    private val attributeOverrides = sequence {
        Color.values().forEach { it1 ->
            val otherColors = Color.values().filter { it != it1 }
            yield(Attribute.Set.foregroundColor(it1) to otherColors.map { it2 -> Attribute.Set.foregroundColor(it2) })
            yield(Attribute.Set.backgroundColor(it1) to otherColors.map { it2 -> Attribute.Set.backgroundColor(it2) })
        }
    }.toMap()

    // Get combine-able attributes which do not affect the [given] list (or each other)
    private fun getCombineAttribute(vararg given: Attribute): Attribute =
        getCombineAttributes(1, given.asIterable()).first()

    private fun getCombineAttributes(count: Int, vararg given: Attribute) =
        getCombineAttributes(count, given.asIterable())

    private fun getCombineAttributes(count: Int, given: Iterable<Attribute>): Iterable<Attribute> {
        fun getExcludes(attribute: Attribute): Set<Attribute> {
            val exclude = HashSet<Attribute>()
            // No duplicates
            exclude.add(attribute)
            // No resets
            attributeResets[attribute]?.apply { exclude.add(this) }
            // No "reveres resets"
            attributeResets.entries.filter { it.value == attribute }.forEach { exclude.add(it.key) }
            // No multiple colors
            if (attribute.toString().startsWith("Set.ForegroundColor") || attribute.toString() == "Reset.ForegroundColor") {
                Color.values().forEach { color -> exclude.add(Attribute.Set.foregroundColor(color)) }
            } else if (attribute.toString().startsWith("Set.BackgroundColor") || attribute.toString() == "Reset.BackgroundColor") {
                Color.values().forEach { color -> exclude.add(Attribute.Set.backgroundColor(color)) }
            }
            return exclude
        }

        val attributes = HashSet<Attribute>()
        val exclude = HashSet<Attribute>()
        exclude.add(Attribute.Reset.all)
        given.forEach { exclude.addAll(getExcludes(it)) }
        while (attributes.size < count) {
            val newAttr = allAttributes[random.nextInt(allAttributes.size)]
            if (!exclude.contains(newAttr)) {
                attributes.add(newAttr)
                exclude.addAll(getExcludes(newAttr))
            }
        }
        return attributes
    }

    private fun stripValue(attribute: Attribute) =
        attribute.ansiString.substring(2, attribute.ansiString.length - 1).toInt()

    data class AttributeTestCase(val attribute: Attribute, val text: String)

    private val ansiCodes = listOf(
        AttributeTestCase(Attribute.Set.bold, "\u001B[1m"),
        AttributeTestCase(Attribute.Set.dim, "\u001B[2m"),
        AttributeTestCase(Attribute.Set.underline, "\u001B[4m"),
        AttributeTestCase(Attribute.Set.blink, "\u001B[5m"),
        AttributeTestCase(Attribute.Set.invert, "\u001B[7m"),
        AttributeTestCase(Attribute.Set.hidden, "\u001B[8m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.BLACK), "\u001B[30m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.RED), "\u001B[31m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.GREEN), "\u001B[32m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.YELLOW), "\u001B[33m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.BLUE), "\u001B[34m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.MAGENTA), "\u001B[35m"),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.CYAN), "\u001B[36m"),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_GRAY),
            "\u001B[37m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.DARK_GRAY),
            "\u001B[90m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_RED),
            "\u001B[91m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_GREEN),
            "\u001B[92m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_YELLOW),
            "\u001B[93m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_BLUE),
            "\u001B[94m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_MAGENTA),
            "\u001B[95m"
        ),
        AttributeTestCase(
            Attribute.Set.foregroundColor(Color.LIGHT_CYAN),
            "\u001B[96m"
        ),
        AttributeTestCase(Attribute.Set.foregroundColor(Color.WHITE), "\u001B[97m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.BLACK), "\u001B[40m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.RED), "\u001B[41m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.GREEN), "\u001B[42m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.YELLOW), "\u001B[43m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.BLUE), "\u001B[44m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.MAGENTA), "\u001B[45m"),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.CYAN), "\u001B[46m"),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_GRAY),
            "\u001B[47m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.DARK_GRAY),
            "\u001B[100m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_RED),
            "\u001B[101m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_GREEN),
            "\u001B[102m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_YELLOW),
            "\u001B[103m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_BLUE),
            "\u001B[104m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_MAGENTA),
            "\u001B[105m"
        ),
        AttributeTestCase(
            Attribute.Set.backgroundColor(Color.LIGHT_CYAN),
            "\u001B[106m"
        ),
        AttributeTestCase(Attribute.Set.backgroundColor(Color.WHITE), "\u001B[107m"),
        AttributeTestCase(Attribute.Reset.all, "\u001B[0m"),
        AttributeTestCase(Attribute.Reset.bold, "\u001B[21m"),
        AttributeTestCase(Attribute.Reset.dim, "\u001B[22m"),
        AttributeTestCase(Attribute.Reset.underline, "\u001B[24m"),
        AttributeTestCase(Attribute.Reset.blink, "\u001B[25m"),
        AttributeTestCase(Attribute.Reset.invert, "\u001B[27m"),
        AttributeTestCase(Attribute.Reset.hidden, "\u001B[28m"),
        AttributeTestCase(Attribute.Reset.foregroundColor, "\u001B[39m"),
        AttributeTestCase(Attribute.Reset.backgroundColor, "\u001B[49m")
    )

    data class AttributeCombinationTestCase(val name: String, val attributes: Iterable<Attribute>, val text: String)

    private val combinationAnsiCodes = sequence {
        // Combine-able without alteration
        repeat(10) {
            val attrs = getCombineAttributes(3)
            val values = attrs.map { stripValue(it) }.toList()
            yield(
                AttributeCombinationTestCase(
                    "Able to combine",
                    attrs,
                    "\u001B[${values[0]};${values[1]};${values[2]}m"
                )
            )
        }
        allAttributes.forEach {
            val extra = getCombineAttribute(it)
            yield(
                AttributeCombinationTestCase(
                    "Duplicates",
                    listOf(it, extra, it),
                    "\u001B[${stripValue(extra)};${stripValue(it)}m"
                )
            )
        }
        attributeResets.forEach {
            val extra = getCombineAttribute(it.key, it.value)
            yield(
                AttributeCombinationTestCase(
                    "Resets",
                    listOf(it.key, extra, it.value),
                    "\u001B[${stripValue(extra)};${stripValue(it.value)}m"
                )
            )
            yield(
                AttributeCombinationTestCase(
                    "Resets (reverse)",
                    listOf(it.value, extra, it.key),
                    "\u001B[${stripValue(extra)};${stripValue(it.key)}m"
                )
            )
            repeat(10) {
                yield(
                    AttributeCombinationTestCase(
                        "Reset All",
                        getCombineAttributes(3) + Attribute.Reset.all,
                        "\u001B[0m"
                    )
                )

            }
        }
        attributeOverrides.forEach {
            it.value.forEach { itOver ->
                val extra = getCombineAttribute(it.key, itOver)
                yield(
                    AttributeCombinationTestCase(
                        "Override color",
                        listOf(it.key, extra, itOver),
                        "\u001B[${stripValue(extra)};${stripValue(itOver)}m"
                    )
                )
            }
        }
    }.asIterable()
    // endregion Test cases

}

