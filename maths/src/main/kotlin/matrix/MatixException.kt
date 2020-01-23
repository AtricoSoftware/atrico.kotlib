package atrico.kotlib.maths.matrix

//
/**
 * Rows or columns must match for given operation
 * @param operation Operation being performed ("Multiply", "Add", etc)
 * @param reason Reoson for failure ("Row must match column", etc)
 */
class SizeMismatchException(operation: String, reason: String) : Exception("Size mismatch: For $operation, $reason") {
}