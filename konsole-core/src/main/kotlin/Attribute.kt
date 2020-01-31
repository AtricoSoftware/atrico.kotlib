package atrico.kotlib.konsole.core

class Attribute private constructor(private val types: Map<AttributeType, SetOrReset>) {
    private constructor(setOrReset: SetOrReset, type: AttributeType) : this(mapOf(type to setOrReset))

    private val isResetAll by lazy { AttributeType.values().all { types[it] is SetOrReset.Reset } }
    private val toString by lazy {
        if (isResetAll) "Reset.All" else types.entries.joinToString(",") { it.value.display(it.key) }
    }

    val ansiString: String by lazy { createString() }

    /**
     * Attribute which will reset all attributes set by this
     * Reset attributes are ignored
     */
    val reset: Attribute by lazy{Attribute(types.entries.filter { it.value is SetOrReset.Set }.map{it.key to SetOrReset.Reset()}.toMap())}

    /**
     * Combine this attribute with [rhs]
     */
    operator fun plus(rhs: Attribute): Attribute = Attribute(types + rhs.types)

    override fun equals(other: Any?) = other is Attribute && other.toString == toString
    override fun hashCode() = toString.hashCode()
    override fun toString() = toString

    private fun createString() = if (isResetAll) createString(listOf(0)) else createString(
        types.entries.map {
            when (it.key) {
                AttributeType.BOLD -> 1 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.DIM -> 2 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.UNDERLINE -> 4 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.BLINK -> 5 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.INVERT -> 7 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.HIDDEN -> 8 + if (it.value is SetOrReset.Reset) 20 else 0
                AttributeType.FOREGROUND_COLOR -> if (it.value is SetOrReset.Reset) 39 else it.value.color!!.baseCode
                AttributeType.BACKGROUND_COLOR -> (if (it.value is SetOrReset.Reset) 39 else it.value.color!!.baseCode) + BG_JUMP
            }
        })

    private fun createString(values: Iterable<Int>) =
        if (values.any()) values.joinToString(";", "$ESCAPE[", "m") else ""

    // region Constructors

    object Set {
        val bold: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.BOLD) }
        val dim: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.DIM) }
        val underline: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.UNDERLINE) }
        val blink: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.BLINK) }
        val invert: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.INVERT) }
        val hidden: Attribute by lazy { Attribute(SetOrReset.Set(), AttributeType.HIDDEN) }

        fun foregroundColor(color: Color): Attribute = Attribute(SetOrReset.Set(color), AttributeType.FOREGROUND_COLOR)
        fun backgroundColor(color: Color): Attribute = Attribute(SetOrReset.Set(color), AttributeType.BACKGROUND_COLOR)

    }

    object Reset {
        val all: Attribute by lazy { Attribute(AttributeType.values().map { it to SetOrReset.Reset() }.toMap()) }
        val bold: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.BOLD) }
        val dim: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.DIM) }
        val underline: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.UNDERLINE) }
        val blink: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.BLINK) }
        val invert: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.INVERT) }
        val hidden: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.HIDDEN) }

        val foregroundColor: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.FOREGROUND_COLOR) }
        val backgroundColor: Attribute by lazy { Attribute(SetOrReset.Reset(), AttributeType.BACKGROUND_COLOR) }

    }

    // endregion Constructors

    companion object {
        val none by lazy { Attribute(emptyMap()) }
        /** Escape char */
        private val ESCAPE = '\u001B'
        /** Offset to foreground codes to produce background ansi strings */
        private const val BG_JUMP = 10

        /**
         * Combine these [attributes] into a single attribute
         */
        fun combine(attributes: Iterable<Attribute>) = attributes.fold(none) { acc, attr -> acc + attr }

        /**
         * Combine these [attributes] into a single attribute (vararg)
         */
        fun combine(vararg attributes: Attribute) = combine(attributes.asIterable())

        /**
         * Parse an ansi code into the attribute (or null if not value)
         */
        fun parse(text: String): Attribute? = parseRegexMatch(ansiRegex.matchEntire(text))

        /**
         * Strip the ansi codes from a string
         * @return Simple string with no codes
         */
        fun stripAttributes(text: String): String = ansiRegex.split(text).joinToString("")

        /** Regex for parsing ANSI control strings */
        private val ansiRegex = Regex("${ESCAPE}\\[(\\d+(;\\d+)*)m")

        /**
         * Check a match result (on [ansiRegex]) and return color code or null
         */
        private fun parseRegexMatch(match: MatchResult?): Attribute? =
            match?.groups?.get(1)?.value?.split(';')?.mapNotNull { fromValue(it.toInt()) }?.fold(none) { acc, attr -> acc + attr }

        /**
         * Create attribute from its baseCode
         */
        private fun fromValue(value: Int): Attribute? =
            when (value) {
                1 -> Set.bold
                2 -> Set.dim
                4 -> Set.underline
                5 -> Set.blink
                7 -> Set.invert
                8 -> Set.hidden
                0 -> Reset.all
                21 -> Reset.bold
                22 -> Reset.dim
                24 -> Reset.underline
                25 -> Reset.blink
                27 -> Reset.invert
                28 -> Reset.hidden
                39 -> Reset.foregroundColor
                49 -> Reset.backgroundColor
                else -> {
                    Color.fromValue(value)?.let { Set.foregroundColor(it) }
                        ?: Color.fromValue(value - BG_JUMP)?.let { Set.backgroundColor(it) }
                }
            }
    }

    private sealed class SetOrReset(val color: Color?) {
        abstract fun display(type: AttributeType): String
        class Set(color: Color? = null) : SetOrReset(color) {
            override fun display(type: AttributeType) = "Set.$type${color?.let { "($color)" } ?: ""}"
        }

        class Reset : SetOrReset(null) {
            override fun display(type: AttributeType) = "Reset.$type"
        }
    }

    private enum class AttributeType {
        BOLD,
        DIM,
        UNDERLINE,
        BLINK,
        INVERT,
        HIDDEN,
        FOREGROUND_COLOR,
        BACKGROUND_COLOR;

        override fun toString() = name.split('_').joinToString("") { it.toLowerCase().capitalize() }
    }
}

