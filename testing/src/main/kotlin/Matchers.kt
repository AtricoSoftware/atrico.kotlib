package atrico.kotlib.testing

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo

/**
 * Returns a matcher that reports if a boolean value is true.
 */
fun isTrue(): Matcher<Boolean?> = equalTo(true)

/**
 * Returns a matcher that reports if a boolean value is false.
 */
fun isFalse(): Matcher<Boolean?> = equalTo(false)
