package atrico.kotlib

/**
 * Class to create a hash of multiple objects
 */
class Hash private constructor(val value: Int) {

    /**
     * Add objects to this hash
     */
    fun add(objects: Iterable<Any?>): Hash =
        Hash(objects.fold(value) { hash, obj ->
            ITERATION * hash + (obj?.hashCode() ?: 1)
        })

    /**
     * Add objects to this hash (vararg)
     */
    fun add(vararg objects: Any?) = add(objects.asIterable())

    companion object {
        private const val INITIAL = 17
        private const val ITERATION = 31

        /**
         * Seed with initial value
         */
        operator fun invoke() = Hash(INITIAL)

        /**
         * Seed with multiple values
         */
        operator fun invoke(objects: Iterable<Any?>) = invoke().add(objects)

        /**
         * Seed with multiple values (vararg)
         */
        operator fun invoke(vararg objects: Any?) = invoke(objects.asIterable())

        /**
         * Shortcut to the value
         */
        fun calculate(objects: Iterable<Any?>) = invoke(objects).value

        /**
         * Shortcut to the value (vararg)
         */
        fun calculate(vararg objects: Any?) = calculate(objects.asIterable())
    }
}