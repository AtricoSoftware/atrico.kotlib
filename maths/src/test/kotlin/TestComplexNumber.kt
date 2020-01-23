package atrico.kotlib.maths

import atrico.kotlib.number.*
import atrico.kotlib.testing.isFalse
import atrico.kotlib.testing.isTrue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import kotlin.math.sqrt
import kotlin.random.Random

class TestComplexNumber {

    private val doublesHelper = DoublesHelper(3)

    @Test
    fun testConstruction() {
        // Arrange
        val real: Number = Random.nextDouble()
        val imag: Number = Random.nextDouble()

        // Act
        val result = ComplexNumber(real, imag)
        println(result)

        // Assert
        assertThat("real", result.real, equalTo(real))
        assertThat("imag", result.imag, equalTo(imag))
    }


    @Test
    fun testToStringBothPositive() {
        // Arrange
        val real = Random.nextInt(1, 100)
        val imag = Random.nextInt(1, 100)

        // Act
        val result = ComplexNumber(real, imag).toString()

        // Assert
        assertThat(
            "format",
            result,
            equalTo("(${doublesHelper.format(real)} + ${doublesHelper.format(imag)}j)")
        )
    }

    @Test
    fun testToStringNegativeImag() {
        // Arrange
        val real = Random.nextInt(1, 100)
        val imag = Random.nextInt(-100, -1)

        // Act
        val result = ComplexNumber(real, imag).toString()

        // Assert
        assertThat(
            "format",
            result,
            equalTo("(${doublesHelper.format(real)} - ${doublesHelper.format(abs(imag))}j)")
        )
    }

    // region Comparison

    @Test
    fun testEqual() {
        // Arrange
        val real = Random.nextDouble()
        val imag = Random.nextDouble()
        val lhs = ComplexNumber(real, imag)
        val rhs = ComplexNumber(real, imag)
        println(lhs)
        println(rhs)

        // Act
        val resultEq = lhs == rhs
        val resultNeq = lhs != rhs

        // Assert
        assertThat("==", resultEq, isTrue())
        assertThat("!=", resultNeq, isFalse())

    }

    @Test
    fun testNotEqual() {
        val real = Random.nextDouble()
        val imag = real + 1
        val lhs = ComplexNumber(real, imag)
        val rhs = ComplexNumber(imag, real)
        println(lhs)
        println(rhs)

        // Act
        val resultEq = lhs == rhs
        val resultNeq = lhs != rhs

        // Assert
        assertThat("==", resultEq, isFalse())
        assertThat("!=", resultNeq, isTrue())

    }

    // endregion

    // region Arithmetic

    @Test
    fun testAdd() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhsR: Number = Random.nextDouble()
        val rhsI: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)
        val rhs = ComplexNumber(rhsR, rhsI)

        // Act
        val result = lhs + rhs

        // Assert
        assertThat("real", result.real, equalTo(lhsR + rhsR))
        assertThat("imag", result.imag, equalTo(lhsI + rhsI))
    }

    @Test
    fun testSubtract() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhsR: Number = Random.nextDouble()
        val rhsI: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)
        val rhs = ComplexNumber(rhsR, rhsI)

        // Act
        val result = lhs - rhs

        // Assert
        assertThat("real", result.real, equalTo(lhsR - rhsR))
        assertThat("imag", result.imag, equalTo(lhsI - rhsI))
    }

    @Test
    fun testMultiply() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhsR: Number = Random.nextDouble()
        val rhsI: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)
        val rhs = ComplexNumber(rhsR, rhsI)
        val expectedR = lhsR * rhsR - lhsI * rhsI
        val expectedI = lhsR * rhsI + lhsI * rhsR

        // Act
        val result = lhs * rhs

        // Assert
        assertThat("real", result.real, equalTo(expectedR))
        assertThat("imag", result.imag, equalTo(expectedI))
    }

    @Test
    fun testMultiplyByReal() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhs: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)

        // Act
        val result = lhs * rhs

        // Assert
        assertThat("real", result.real, equalTo(lhsR * rhs))
        assertThat("imag", result.imag, equalTo(lhsI * rhs))
    }

    @Test
    fun testDivide() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhsR: Number = Random.nextDouble()
        val rhsI: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)
        val rhs = ComplexNumber(rhsR, rhsI)
        val expectedR = (lhsR * rhsR + lhsI * rhsI) / (rhs.real * rhs.real + rhs.imag * rhs.imag)
        val expectedI = (-lhsR * rhsI + lhsI * rhsR) / (rhs.real * rhs.real + rhs.imag * rhs.imag)
        val expected = ComplexNumber(expectedR, expectedI)
        // Act
        val result = lhs / rhs

        // Assert
        assertThat("divide", result, equalTo(expected))
    }

    @Test
    fun testDivideExample() {
        // Arrange
        val lhs = ComplexNumber(2.0, 5.0)
        val rhs = ComplexNumber(4.0, -1.0)
        val expected = ComplexNumber(3.0 / 17.0, 22.0 / 17.0)

        // Act
        val result = lhs / rhs

        // Assert
        assertThat("divide", result, equalTo(expected))
    }

    @Test
    fun testDivideByReal() {
        // Arrange
        val lhsR: Number = Random.nextDouble()
        val lhsI: Number = Random.nextDouble()
        val rhs: Number = Random.nextDouble()
        val lhs = ComplexNumber(lhsR, lhsI)

        // Act
        val result = lhs / rhs

        // Assert
        assertThat("real", result.real, equalTo(lhsR / rhs))
        assertThat("imag", result.imag, equalTo(lhsI / rhs))
    }

    // endregion

    @Test
    fun testModulus() {
        // Arrange
        val real = Random.nextDouble()
        val imag = Random.nextDouble()

        // Act
        val result = ComplexNumber(real, imag).modulus()

        // Assert
        assertThat("modulus", result, equalTo(sqrt((real * real + imag * imag))))
    }

    @Test
    fun testModulusSquared() {
        // Arrange
        val real: Number = Random.nextDouble()
        val imag: Number = Random.nextDouble()

        // Act
        val result = ComplexNumber(real, imag).modulusSquared()

        // Assert
        assertThat("modulus", result, equalTo(real * real + imag * imag))
    }

    @Test
    fun testConjugate() {
        // Arrange
        val real = Random.nextDouble()
        val imag = Random.nextDouble()
        val expected = ComplexNumber(real, -imag)

        // Act
        val result = ComplexNumber(real, imag).conjugate()

        // Assert
        assertThat("reciprocal", result, equalTo(expected))
    }

    @Test
    fun testReciprocal() {
        // Arrange
        val real = Random.nextDouble()
        val imag = Random.nextDouble()
        val expectedR = real / (real * real + imag * imag)
        val expectedI = -imag / (real * real + imag * imag)
        val expected = ComplexNumber(expectedR, expectedI)

        // Act
        val result = ComplexNumber(real, imag).reciprocal()

        // Assert
        assertThat("reciprocal", result, equalTo(expected))
    }

}
