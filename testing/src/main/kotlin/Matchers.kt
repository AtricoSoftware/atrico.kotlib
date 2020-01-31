package atrico.kotlib.testing

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.describe
import com.natpryce.hamkrest.equalTo

/**
 * Returns a matcher that reports if a boolean value is true.
 */
val isTrue: Matcher<Boolean?> = equalTo(true)

/**
 * Returns a matcher that reports if a boolean value is false.
 */
val isFalse: Matcher<Boolean?> = equalTo(false)

/**
 * Returns a matcher that matches not equal to
 */
fun <T> notEqualTo(expected: T?): Matcher<T?> = NotEqualToMatcher(expected)

/**
 * Matcher for not equal to
 */
private class NotEqualToMatcher<T>(private val expected: T?) : Matcher<T?> {
    override fun invoke(actual: T?): MatchResult = if (actual != expected) {
        MatchResult.Match
    } else {
        MatchResult.Mismatch("was: ${describe(actual)}")
    }

    override val description: String get() = "is not equal to ${describe(expected)}"
    override val negatedDescription: String get() = "is equal to ${describe(expected)}"
}

/**
 * Returns a matcher for matching elements of a collection
 * Simple rename for collection matcher
 */
fun <T> contains(elements: Iterable<T>): Matcher<Iterable<T>?> =
    object : BaseContainsMatcher<T>(elements) {
        override fun predicate(actual: Iterable<T>?): Errors<T> {
            val errors = containsInAnyOrderPredicate(actual)
            // Check order
            return errors.combine(outOfOrderPredicate(actual, errors))
        }

        private fun outOfOrderPredicate(actual: Iterable<T>?, errors: Errors<T>): Errors<T> {
            // Null always fails
            if (actual == null) return Errors(isNull = true)
            // Full descriptive comparison
            val expectedRemain = elements.toMutableList()
            errors.missing.forEach { expectedRemain.remove(it) }
            val actualRemain = actual.toMutableList()
            errors.extra.forEach { actualRemain.remove(it) }
            // Check for all in order
            if (equalTo(expectedRemain).invoke(actualRemain) == MatchResult.Match) return Errors()
            // Try removing elements
            for (count in 1 until actualRemain.count()) {
                for (comb in calculateCombinations(actualRemain, count)) {
                    if (matchWithRemovals(actualRemain, expectedRemain, comb)) return Errors(outOfOrder = comb.toList())
                }
            }
            return Errors(outOfOrder = actualRemain)
        }

        private fun matchWithRemovals(actual: List<T>, expected: List<T>, remove: Iterable<T>): Boolean {
            val actualRemain = actual.toMutableList()
            val expectedRemain = expected.toMutableList()
            remove.forEach {
                actualRemain.remove(it)
                expectedRemain.remove(it)
            }
            return equalTo(expectedRemain).invoke(actualRemain) == MatchResult.Match
        }
    }

/**
 * Returns a matcher for matching elements of a collection in any order
 * Collection must contain every element but in any order
 */
fun <T> containsInAnyOrder(elements: Iterable<T>): Matcher<Iterable<T>?> =
    object : BaseContainsMatcher<T>(elements) {
        override fun predicate(actual: Iterable<T>?) = containsInAnyOrderPredicate(actual)

        override val description: String get() = "${super.description} in any order"
    }

/**
 * Base class for collection matchers
 */
abstract class BaseContainsMatcher<T>(private val elements: Iterable<T>) : Matcher<Iterable<T>?> {
    override fun invoke(actual: Iterable<T>?): MatchResult {
        val error = predicate(actual).toErrorString()
        return if (error == null) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch(error)
        }
    }

    /**
     * Check for match
     * Return error message or null for match
     */
    protected abstract fun predicate(actual: Iterable<T>?): Errors<T>

    protected fun containsInAnyOrderPredicate(actual: Iterable<T>?): Errors<T> {
        // Null always fails
        if (actual == null) return Errors(isNull = true)
        // Full descriptive comparison
        val remainExpected = elements.toMutableList()
        actual.forEach { remainExpected.remove(it) }
        val remainActual = actual.toMutableList()
        elements.forEach { remainActual.remove(it) }
        return Errors(missing = remainExpected, extra = remainActual)
    }

    override val description: String get() = "contains ${describe(elements)}"
    override val negatedDescription: String get() = "does not contain: ${describe(elements)}"

    protected class Errors<T>(
        val isNull: Boolean = false,
        val missing: List<T> = emptyList(),
        val extra: List<T> = emptyList(),
        val outOfOrder: List<T> = emptyList()
    ) {
        fun combine(rhs: Errors<T>): Errors<T> = Errors(
            isNull || rhs.isNull,
            missing + rhs.missing,
            extra + rhs.extra,
            outOfOrder + rhs.outOfOrder
        )

        fun toErrorString(): String? {
            if (isNull) return "is null"
            val message = StringBuilder()
            if (missing.isNotEmpty()) message.append("Missing: $missing")
            if (extra.isNotEmpty()) {
                if (message.isNotEmpty()) message.append(", ")
                message.append("Extra: $extra")
            }
            if (outOfOrder.isNotEmpty()) {
                if (message.isNotEmpty()) message.append(", ")
                message.append("Out of Order: $outOfOrder")
            }
            return if (message.isNotEmpty()) message.toString() else null
        }

    }
}
