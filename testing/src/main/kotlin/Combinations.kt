package atrico.kotlib.testing

import java.util.*

// Calculate the combinations of a set
fun <T> calculateCombinations(items: Collection<T>, count: Int): Sequence<Iterable<T>> {
    fun itemIterator(items: Collection<T>): Sequence<Iteration<T>> {
        return sequence {
            val queue = ArrayDeque(items)
            while (!queue.isEmpty()) {
                yield(Iteration(queue.pop(), queue))
            }
        }
    }
    return calculateImpl(items, count) { col -> itemIterator(col) }
}

data class Iteration<T>(val item: T, val remaining: Collection<T>)
private fun <T> calculateImpl(
    items: Collection<T>,
    count: Int,
    itemIterator: (Collection<T>) -> Sequence<Iteration<T>>
): Sequence<Iterable<T>> {

    fun calculate(items: Collection<T>, count: Int): Sequence<Iterable<T>> {
        return sequence {
            for (iteration in itemIterator(items)) {
                if (count == 1) yield(listOf(iteration.item))
                else {
                    for (tail in calculate(iteration.remaining, count - 1)) {
                        yield(listOf(iteration.item) + tail)
                    }
                }
            }
        }
    }

    // Validate the params
    if (items.count() == 0) throw IllegalArgumentException("Elements cannot be empty")
    if (count < 1) throw IllegalArgumentException("Count must be greater than 0")
    if (count > items.count()) throw IllegalArgumentException("Count must be less than or equal to elements count")
    // Calculate value
    return calculate(items, count)
}
